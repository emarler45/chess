package service;

import chess.ChessGame;
import dataaccess.AuthDAO;
import dataaccess.AuthDAOMemory;
import dataaccess.GamesDAO;
import dataaccess.GamesDAOMemory;
import model.GameData;

import java.util.concurrent.ThreadLocalRandom;

public class ChessGameService {

    private final AuthDAO authdao = new AuthDAOMemory();
    private final GamesDAO gamesdao = new GamesDAOMemory();

    public ChessGameService() {

    }

    public CreateGameResult createGame(String token, CreateGameRequest r) throws UnauthorizedException{
        if (r.gameName().isEmpty()){
            throw new IllegalArgumentException("Game name must not be blank.");
        }
        int gameID = 0;
        if (authdao.verify(token)){
            do {
                gameID = ThreadLocalRandom.current().nextInt(1000, 10000);
            } while (gamesdao.getGame(gameID) != null);
            gamesdao.insertGame(gameID, r.gameName());
        } else {
            throw new UnauthorizedException("Invalid Token");
        }
        return new CreateGameResult(gameID);
    }
}
