package client;

import service.*;
import ui.ChessClientOutput;
import ui.EscapeSequences;

import java.util.Arrays;
import java.util.Objects;
import java.util.Scanner;

public class ChessClient {

    private String currentUserName;
    private State currState = State.LOGGED_OUT;
    private String authToken;
    private final ServerFacade facade;
    private GameREPL gameREPL;
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
        out.printline("Welcome to ekm45's Chess Game!");
        out.printline(help());
        while (currState != State.QUIT) {
            String result;
            if (currState == State.IN_GAME){
                result = doInGameLoop();
            } else {
                result = doREPL();
            }
            out.printline(result);
        }
        out.printline("Goodbye!");
    }

    private String eval(String input){
        try {
            String[] tokens = input.toLowerCase().split(" ");
            String cmd = (tokens.length > 0) ? tokens[0] : "help";
            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
            return switch (cmd) {
                case "help" -> help();

                case "login" -> login(params);
                case "register" -> register(params);
                case "delete" -> deleteGames();
                case "quit" -> quit();

                case "logout" -> logout();
                case "list" -> listGames();
                case "join" -> joinGame(params);
                case "observe" -> watchGame(params);
                case "create" -> createGame(params);
                default -> "Command not recognized";
            };
        } catch (ResponseException ex) {
            return ex.getMessage();
        }
    }

    private String doREPL() {
        out.print(getPrompt());
        String input = scanner.nextLine();
        return eval(input);
    }

    private String doInGameLoop(){
        String result = gameREPL.doREPL();
        if (Objects.equals(result, "leave")){
            currState = State.LOGGED_IN;
            return "Leaving game!";
        }
        return result;
    }


    private String login(String... params) throws ResponseException{
        try {
            assertLoggedOut();
            assertParamsPresent(2, params.length);
            LoginResult result = facade.login(params[0], params[1]);
            currState = State.LOGGED_IN;
            this.authToken = result.authToken();
            this.currentUserName = result.username();
            return "Successfully logged in as %s".formatted(result.username());
        } catch (InvalidParametersException e) {
            throw new ResponseException(e.getMessage() + "login <username> <password>");
        }
    }

    private String register(String... params){
        try {
            assertLoggedOut();
            assertParamsPresent(3, params.length);
            RegisterResult result = facade.register(params[0], params[1], params[2]);
            this.authToken = result.authToken();
            this.currentUserName = result.username();
            currState = State.LOGGED_IN;
            return "Successful registered and logged in as %s".formatted(result.username());
        } catch (InvalidParametersException e) {
            throw new ResponseException(e.getMessage() + "register <username> <password> <email>\n" +
                    "The email must be valid.");
        }
    }

    private String logout(){
        assertLoggedIn();
        facade.logout(authToken);
        this.authToken = null;
        this.currentUserName = null;
        currState = State.LOGGED_OUT;
        return "Successfully logged out!";
    }

    private String deleteGames(){
        assertLoggedOut();
        facade.delete();
        return "Successfully cleared database!";
    }

    private String listGames(){
        assertLoggedIn();
        StringBuilder builder = new StringBuilder();
        ListGamesResult games = facade.listGames(authToken);
        builder.append("Available games:\n");
        for (GameInfo info : games.games()){
            String whitePlayer = (info.whiteUsername() == null) ? "" : info.whiteUsername();
            String blackPlayer = (info.blackUsername() == null) ? "" : info.blackUsername();
            String game = info.gameName() + "\n\t ID: %d".formatted(info.gameID()) +
                        "\n\t White Player: " + whitePlayer +
                        "\n\t Black Player: " + blackPlayer;
            builder.append(game).append("\n\n");
        }
        return builder.toString();
    }

    private String joinGame(String... params){
        try {
            assertLoggedIn();
            assertParamsPresent(2, params.length);
            int gameID = Integer.parseInt(params[0]);
            String playerColor = params[1].toUpperCase();
            facade.joinGame(authToken, gameID, playerColor);
            gameREPL = new GameREPL(out, facade, null);
            currState = State.IN_GAME;
            return "Successfully joined game %d as %s".formatted(gameID, params[1].toUpperCase());
        } catch (NumberFormatException e) {
            throw new ResponseException("Game ID must be a number!");
        } catch (InvalidParametersException e) {
            throw new ResponseException(e.getMessage() + "join <ID> [WHITE|BLACK]");
        }
    }

    private String watchGame(String... params){
        try {
            assertLoggedIn();
            assertParamsPresent(1, params.length);
            int gameID = Integer.parseInt(params[0]);
            gameREPL = new GameREPL(out, facade, null);
            currState = State.IN_GAME;
            return "Observing game %d".formatted(gameID);
        } catch (InvalidParametersException e) {
            throw new ResponseException(e.getMessage() + "observe <ID>");
        } catch (NumberFormatException e) {
            throw new ResponseException("Game ID must be a number!");
        }
    }

    private String createGame(String... params){
        try {
            assertLoggedIn();
            assertParamsPresent(1, params.length);
            CreateGameResult result = facade.createGame(authToken, params[0]);
            return "Successfully created game %s with ID %d".formatted(params[0], result.gameID());
        } catch (InvalidParametersException e){
            throw new ResponseException(e.getMessage() + "create <name>");
        }
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

    private void assertParamsPresent(int exp, int actual){
        if (actual < exp){
            throw new InvalidParametersException("Command format: ");
        }
    }

    public String getPrompt() {
        String name = (currState == State.LOGGED_IN) ? "[" + currentUserName + "]" : "[logged out]";
        return name + " " + EscapeSequences.SET_TEXT_COLOR_BLUE +  ">>>" + EscapeSequences.RESET_TEXT_COLOR + " " ;
    }
}
