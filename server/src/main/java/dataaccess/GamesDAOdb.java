package dataaccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
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
        if (doesNotExist(gameID)) {throw new DataNotFoundException("Game with id %d not found".formatted(gameID));}
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
                        return readGame(resSet);
                    }
                }
            }
        } catch (SQLException sqle){
            throw new DataAccessException("Faailed to access database: %s".formatted(sqle.getMessage()));
        }
        return null;
    }

    private GameData readGame(ResultSet rset) throws SQLException {
        ChessGame gameFromJson = new Gson().fromJson(rset.getString("game"), ChessGame.class);
        return new GameData(
                rset.getString("white"),
                rset.getString("black"),
                rset.getInt("id"),
                rset.getString("name"),
                gameFromJson
        );
    }


    @Override
    public void updateGame(GameData data) throws Exception {
        int gameID = data.gameID();
        if (doesNotExist(gameID)) {throw new DataNotFoundException("Game with id %d not found".formatted(gameID));}
        String statement = "UPDATE games SET white = ?, black = ?, game = ? WHERE id = ?";
        var gameJson = gameToJson(data.game());
        executeStatement(statement, data.whiteUsername(), data.blackUsername(), gameJson, data.gameID());
    }

    private boolean doesNotExist(int id) throws DataAccessException {
        return (getGame(id) == null);
    }


    @Override
    public ArrayList<GameData> getGameList() throws DataAccessException {
        ArrayList<GameData> resultList = new ArrayList<>();
        String getStatement = "SELECT id, white, black, name, game FROM games";
        try (var conn = DatabaseManager.getConnection();
             var prepStatement = conn.prepareStatement(getStatement);
             ResultSet resSet = prepStatement.executeQuery()){
            while (resSet.next()){
                resultList.add(readGame(resSet));
            }
        } catch (SQLException sqle) {
            throw new DataAccessException("Failed to read database: %s".formatted(sqle.getMessage()));
        }
        return resultList;
    }

    @Override
    public void clearAll() throws DataAccessException {
        String statement = "TRUNCATE games";
        executeStatement(statement);
    }

    public void executeStatement(String statement, Object... params) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            try (var prepStatement = conn.prepareStatement(statement)) {
                for (int i = 0; i < params.length; i++){
                    Object param = params[i];
                    switch (param) {
                        case Integer j -> prepStatement.setInt(i+1, j);
                        case String s -> prepStatement.setString(i+1, s);
                        case null -> prepStatement.setNull(1+i, Types.NULL);
                        default -> throw new IllegalArgumentException("Unexpected value: " + param);
                    }
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
