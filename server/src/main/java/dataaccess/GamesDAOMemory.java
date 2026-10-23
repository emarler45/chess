package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

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
    public void createGame(int gameID, String gameName) {
        GameData newGame = new GameData(null, null, gameID, gameName, new ChessGame());
        dataList.add(newGame);
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

    public int getNumOfGames(){
        return dataList.size();
    }

    @Override
    public ArrayList<GameData> getGameList(){
        return dataList;
    }

    public void clearAll() {
        dataList = new ArrayList<>();
    }
}
