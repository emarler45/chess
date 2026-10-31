package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GamesDAO {
    // Adds new game to db
    int createGame(String name) throws Exception;
    // removes game from db
    void deleteGame(int gameID) throws Exception;
    // retrieves game from db
    GameData getGame(int gameID) throws DataAccessException;
    // change record with matching GameData.gameID with data
    void updateGame(GameData data) throws Exception;
    // return list of games in db
    ArrayList<GameData> getGameList() throws DataAccessException;
    // remove all games from the db
    void clearAll() throws DataAccessException;
}
