package client;

import server.ResponseException;
import server.ServerFacade;
import ui.ChessClientOutput;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    private String currentUserName;
    private State currState = State.SIGNED_OUT;
    private String authToken;
    private ServerFacade facade;
    private final Repl initialRepl = new InitialREPL();
    private final Repl signedInRepl = new SignedInREPL();
    private final Repl playGameRepl = new InGameREPL();
    private final Repl repl = new Repl();
    private final ChessClientOutput out;
    public Scanner scanner = new Scanner(System.in);



    public ChessClient(String url, String outKind){
        facade = new ServerFacade(url);
        out = new ChessClientOutput(outKind);
    }

    public ChessClient(String url){
        facade = new ServerFacade(url);
        out = new ChessClientOutput();
    }

    public void run() {
        out.print("Welcome to ekm45's Chess Game!");
        while (currState != State.QUIT) {
            switch (currState) {
                case SIGNED_OUT -> loggedOut();
                case SIGNED_IN -> loggedIn();
                case IN_GAME -> inGame();
            }
        }
        System.out.println("Goodbye!");
    }

    private void loggedOut(){
        while (currState == State.SIGNED_OUT) {
            repl.help(currState);
            repl.printPrompt();
        }
    }


    private String eval(String input){
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "logout" -> signOut();
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "create" -> createGame(params);
                case "delete" -> deleteGames();
                case "quit" -> "quit";
                default -> help();
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }
    }

}
