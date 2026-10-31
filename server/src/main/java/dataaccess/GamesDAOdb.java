package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class GamesDAOdb implements GamesDAO {

    public GamesDAOdb() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public int createGame(String name) throws Exception {
        if (sanitized(name)){
            var newGame = gameToJson(new ChessGame());
            String statement = "INSERT INTO games (name, game) VALUES (?, ?)";
            try (var conn = DatabaseManager.getConnection()){
                try (var prepStatement = conn.prepareStatement(statement, Statement.RETURN_GENERATED_KEYS)){
                    prepStatement.setString(1, name);
                    prepStatement.setString(2, newGame);
                    prepStatement.executeUpdate();
                    ResultSet resSet = prepStatement.getGeneratedKeys();
                    if (resSet.next()){
                        return resSet.getInt(1);
                    }
                }
            } catch (SQLException sqle) {
                throw new DataAccessException("Failed to update database: %s".formatted(sqle.getMessage()));
            }
            throw new Exception("GameID failed to retrieve");
        }
        throw new IllegalArgumentException("Invalid Game Name");
    }

    private String gameToJson(ChessGame game){
        return new Gson().toJson(game);
    }

    @Override
    public void deleteGame(int gameID) throws DataNotFoundException {

    }

    @Override
    public GameData getGame(int gameID) {
        return null;
    }

    @Override
    public void updateGame(GameData data) throws Exception {

    }


    @Override
    public ArrayList<GameData> getGameList() {
        return null;
    }

    @Override
    public void clearAll() throws DataAccessException {
        String statement = "TRUNCATE games";
        executeStatement(statement);
    }

    public void executeStatement(String statement, String... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepStatement = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++){
                    String param = params[i];
                    prepStatement.setString(i+1, param);
                }
                prepStatement.executeUpdate();
            }
        } catch (SQLException sqle){
            throw new DataAccessException("Unable to update database: %s".formatted(sqle.getMessage()));
        }
    }


    private boolean sanitized(String gameName) throws IllegalArgumentException {
        if (!gameName.matches("\\w+")) {throw new IllegalArgumentException("Invalid Game Name.");}
        else {return true;}
    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }
}
