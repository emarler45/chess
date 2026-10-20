package service;

import dataaccess.*;

public class ChessClearService {

    private final UserDAO user = new UserDAOMemory();
    private final GamesDAO games = new GamesDAOMemory();
    private final AuthDAO auth = new AuthDAOMemory();

    public ChessClearService(){}

    public void clearAll(){

    }
}
