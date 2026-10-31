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
    public void deleteGame(int gameID) throws Exception {
        String statement = "DELETE FROM games WHERE id = ?";
        executeStatement(statement, gameID);
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        String statement = "SELECT * FROM games WHERE id = ?";
        try (var conn = DatabaseManager.getConnection()){
            try (var prepStatement = conn.prepareStatement(statement)){
                prepStatement.setInt(1, gameID);
                try (ResultSet resSet = prepStatement.executeQuery()) {
                    if (resSet.next()){
                        return readGame(resSet, gameID);
                    }
                }
            }
        } catch (SQLException sqle){
            throw new DataAccessException("Faailed to access database: %s".formatted(sqle.getMessage()));
        }
        return null;
    }

    private GameData readGame(ResultSet rset, int id) throws SQLException {
        ChessGame gameFromJson = new Gson().fromJson(rset.getString("game"), ChessGame.class);
        return new GameData(
                rset.getString("white"),
                rset.getString("black"),
                id,
                rset.getString("name"),
                gameFromJson
        );
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
        executeStatement(statement, null);
    }

    public void executeStatement(String statement, Integer id) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepStatement = conn.prepareStatement(statement)) {
                if (id != null) {
                    prepStatement.setInt(1, id);
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
