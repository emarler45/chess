package dataaccess;

import model.GameData;

import java.util.ArrayList;

public class GamesDAOdb implements GamesDAO {

    public GamesDAOdb() throws DataAccessException {
        configureDatabase();
    }

    @Override
    public void createGame(int gameID, String name) {

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
    public void clearAll() {

    }

    public void configureDatabase() throws DataAccessException {
        DatabaseManager.createDatabase();
        DatabaseManager.createTables();
    }
}
