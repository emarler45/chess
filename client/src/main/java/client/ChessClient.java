package client;

import com.google.gson.Gson;
import server.ResponseException;
import server.ServerFacade;
import service.*;
import ui.ChessClientOutput;

import java.util.Arrays;
import java.util.Scanner;

public class ChessClient {

    private String currentUserName;
    private State currState = State.LOGGED_OUT;
    private String authToken;
    private final ServerFacade facade;
    private final Repl repl = new Repl();
    private GameREPL gameREPL;
    private final ChessClientOutput out;
    public Scanner scanner = new Scanner(System.in);



    public ChessClient(String url, String outKind){
        facade = new ServerFacade(url);
        out = new ChessClientOutput(outKind);
        gameREPL = new GameREPL(out, facade, null);
    }

    public ChessClient(String url){
        facade = new ServerFacade(url);
        out = new ChessClientOutput();
    }

    public void run() {
        out.print("Welcome to ekm45's Chess Game!");
        help();
        while (currState != State.QUIT) {
            if (currState == State.IN_GAME){
                gameREPL.loop();
            } else {
                repl.printPrompt();
                String input = scanner.nextLine();
                String result = eval(input);
                out.print(result);
            }
        }
        out.print("Goodbye!");
    }

    private String eval(String input){
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "login" -> login(params);
                case "register" -> register(params);
                case "quit" -> quit();

                case "logout" -> logout();
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> watchGame(params);
                case "create" -> createGame(params);
                case "delete" -> deleteGames();
                default -> "Command not recognized";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String login(String... params) throws ResponseException{
        assertLoggedOut();
        LoginResult result = facade.login(params[0], params[1]);
        currState = State.LOGGED_IN;
        this.authToken = result.authToken();
        return "Successfully logged in as %s".formatted(result.username());
    }

    private String register(String... params){
        assertLoggedOut();
        RegisterResult result = facade.register(params[0], params[1], params[2]);
        currState = State.LOGGED_IN;
        this.authToken = result.authToken();
        return "Successful registered and logged in as %s".formatted(result.username());
    }

    private String logout(){
        assertLoggedIn();
        facade.logout(authToken);
        currState = State.LOGGED_OUT;
        this.authToken = null;
        return "Successfully logged out!";
    }

    private String deleteGames(){
        facade.delete();
        return "Successfully cleared database!";
    }

    private String listGames(){
        assertLoggedIn();
        StringBuilder builder = new StringBuilder();
        ListGamesResult games = facade.listGames(authToken);
        Gson gson = new Gson();
        builder.append("Available games:\n");
        for (GameInfo info : games.games()){
            builder.append(gson.toJson(info)).append("\n");
        }
        return builder.toString();
    }

    private String joinGame(String... params){
        try {
            assertLoggedIn();
            int gameID = Integer.parseInt(params[0]);
            facade.joinGame(authToken, gameID, params[1]);
            gameREPL = new GameREPL(out, facade, gameID);
            currState = State.IN_GAME;
            return "Successfully joined game %d as %s".formatted(gameID, params[1].toUpperCase());
        } catch (NumberFormatException e) {
            throw new ResponseException("Game ID must be a number!");
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new ResponseException("Command format: join <ID> [WHITE|BLACK]");
        }
    }

    private String watchGame(String... params){
        assertLoggedIn();
        int gameID = Integer.parseInt(params[0]);
        gameREPL = new GameREPL(out, facade, gameID);
        currState = State.IN_GAME;
        return "Observing game %d".formatted(gameID);
    }

    private String createGame(String... params){
        assertLoggedIn();
        CreateGameResult result = facade.createGame(authToken, params[0]);
        return "Successfully created game %s with ID %d".formatted(params[0], result.gameID());
    }

    public String help(){
        return "These are the available commands:\n" + switch (currState){
            case LOGGED_OUT -> """
                    help
                    register <username> <password> <email>
                    login <username> <password>
                    quit
                    """;
            case LOGGED_IN -> """
                    help
                    list
                    join <gameID> <[WHITE|BLACK]> (case insensitive)
                    observe <gameID>
                    create <gameName>
                    """;
            case IN_GAME -> """
                    something
                    """;
            default -> null;
        };
    }

    private String quit(){
        if (currState == State.IN_GAME){
            return "You must leave your game first!";
        }
        if (currState == State.LOGGED_IN){
            return "You must log out first!";
        }
        currState = State.QUIT;
        return "Quitting...";
    }

    private boolean isLoggedIn(){
        return currState == State.LOGGED_IN;
    }

    private void assertLoggedIn(){
        if (!isLoggedIn()){
            throw new ResponseException("You are not logged in!");
        }
    }

    private void assertLoggedOut(){
        if (isLoggedIn()){
            throw new ResponseException("You are already logged in!");
        }
    }
}
