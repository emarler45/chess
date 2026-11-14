package client;

import chess.ChessBoard;
import chess.ChessGame;
import server.ServerFacade;
import ui.BoardRenderer;
import ui.ChessClientOutput;

import java.util.Arrays;

public class GameREPL extends Repl{

    private ChessClientOutput out;
    private final Integer gameID;
    private final ServerFacade facade;
    private final BoardRenderer renderer = new BoardRenderer();

    public GameREPL(ChessClientOutput out, ServerFacade facade, Integer gameID){
        this.out = out;
        this.gameID = gameID;
        this.facade = facade;
    }


    public void loop(){
        renderer.renderBoard(new ChessBoard(), ChessGame.TeamColor.WHITE);
    }


    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
        } catch (Exception e) {
        }
        return null;
    }

    private ChessBoard getBoard(int gameID){
        return null;
    }
}
