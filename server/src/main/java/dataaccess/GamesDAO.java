package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GamesDAO {
    // Adds new game to db
    void createGame(int gameID, String name);
    // removes game from db
    void deleteGame(int gameID) throws DataNotFoundException;
    // retrieves game from db
    GameData getGame(int gameID);
    // change record with matching GameData.gameID with data
    void updateGame(GameData data) throws Exception;
    // get total number of games in db
    int getNumOfGames();
    // return list of games in db
    ArrayList<GameData> getGameList();
    // remove all games from the db
    void clearAll();
}
