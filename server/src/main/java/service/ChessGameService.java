package service;

import dataaccess.AuthDAO;
import dataaccess.AuthDAOMemory;
import dataaccess.GamesDAO;
import dataaccess.GamesDAOMemory;

import java.util.concurrent.ThreadLocalRandom;

public class ChessGameService {

    private final AuthDAO authdao = new AuthDAOMemory();
    private final GamesDAO gamesdao = new GamesDAOMemory();

    public ChessGameService() {

    }


}
