package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;
import java.util.concurrent.ThreadLocalRandom;

public class GamesDAOMemory implements GamesDAO {

    private ArrayList<GameData> dataList = new ArrayList<>();

    public GamesDAOMemory() {
//        dataList.add(new GameData("gamer", null, 1234, "newgame", new ChessGame()));
    }

    @Override
    public void updateGame(GameData data) throws Exception {
        deleteGame(data.gameID());
        insertGame(data);
    }

    @Override
    public int createGame(String gameName) {
        int gameID = 0;
        do {
            gameID = ThreadLocalRandom.current().nextInt(1000, 10000);
        } while (getGame(gameID) != null);
        GameData newGame = new GameData(null, null, gameID, gameName, new ChessGame());
        dataList.add(newGame);
        return gameID;
    }

    public void insertGame(GameData game) {
        dataList.add(game);
    }

    @Override
    public void deleteGame(int gameID) throws DataNotFoundException {
        GameData target = getGame(gameID);
        if (target != null){
            dataList.remove(target);
        } else {
            throw new DataNotFoundException("GameID %d not found!".formatted(gameID));
        }
    }

    public GameData getGame(int gameID){
        for (GameData currGame : dataList){
            if (currGame.gameID() == gameID){
                return currGame;
            }
        }
        return null;
    }

    @Override
    public ArrayList<GameData> getGameList(){
        return dataList;
    }

    public void clearAll() {
        dataList = new ArrayList<>();
    }
}
