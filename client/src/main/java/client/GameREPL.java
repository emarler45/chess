package client;

import chess.ChessBoard;
import chess.ChessGame;
import ui.BoardRenderer;
import ui.ChessClientOutput;
import ui.EscapeSequences;

import java.util.Arrays;

public class GameREPL extends Repl {


    private final Integer gameID;
    private final ServerFacade facade;
    private final BoardRenderer renderer = new BoardRenderer();

    public GameREPL(ChessClientOutput out, ServerFacade facade, Integer gameID){
        super(out);
        this.gameID = gameID;
        this.facade = facade;
    }

    public String doREPL() {
        super.out.print(getPrompt());
        String input = scanner.nextLine();
        return eval(input);
    }


    private String eval(String input) {
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "board" -> {
                    ChessBoard board = new ChessBoard();
                    board.resetBoard();
                    yield renderer.renderBoard(board, ChessGame.TeamColor.WHITE);
                }
                case "help" -> "help commands";
                case "leave" -> "leave";
                default -> "Command not recognized";
            };
        } catch (Exception e) {
            throw new ResponseException("Error updating game");
        }
    }

    private ChessBoard getBoard(int gameID){
        return null;
    }

    public String getPrompt() {
        return "[game] " + EscapeSequences.SET_TEXT_COLOR_BLUE +  ">>>" + EscapeSequences.RESET_TEXT_COLOR + " " ;
    }
}
