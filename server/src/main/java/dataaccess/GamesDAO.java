package dataaccess;

import model.GameData;

import java.util.ArrayList;

public interface GamesDAO {
    void insertGame(GameData data);
    void deleteGame();
    void updateGame(GameData data);
    int getNumOfGames();
    ArrayList<GameData> listGames();
}
