package dataaccess;

import chess.ChessGame;
import model.GameData;

import java.util.ArrayList;

public class GamesDAOMemory implements GamesDAO {

    private ArrayList<GameData> dataList = new ArrayList<>();

    public GamesDAOMemory() {
        dataList.add(new GameData("gamer", null, 1234, "newgame", new ChessGame()));
    }

    @Override
    public void updateGame(GameData data) {
        for (GameData currGame : dataList){
            if (currGame.gameID() == data.gameID()){
                dataList.remove(currGame);
                dataList.add(data);
                return;
            }
        }
    }

    @Override
    public void insertGame(GameData data) {
        dataList.add(data);
    }

    @Override
    public void deleteGame() {
        dataList = new ArrayList<>();
    }

    public int getNumOfGames(){
        return dataList.size();
    }

    @Override
    public ArrayList<GameData> listGames(){
        return dataList;
    }

    public void clearAll() {
        dataList = new ArrayList<>();
    }
}
