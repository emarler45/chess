package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GamesDAO {
    // Adds new game to db
    void insertGame(GameData data);
    // removes game from db
    void deleteGame(int gameID);
    // retrieves game from db
    GameData getGame(int gameID);
    // change record with matching GameData.gameID with data
    void updateGame(GameData data);
    // get total number of games in db
    int getNumOfGames();
    // list the games in the db
    ArrayList<GameData> listGames();
    // remove all games from the db
    void clearAll();
}
