//package client;
//
//import chess.ChessBoard;
//import chess.ChessGame;
//import server.ResponseException;
//import ui.ChessClientOutput;
//import ui.EscapeSequences;
//
//import java.util.Arrays;
//import java.util.Scanner;
//
//public class LoggedInREPL extends Repl{
//
//    String authToken;
//    String currentUserName;
//
//
//    public LoggedInREPL(ChessClientOutput output){
//        super(output);
//    }
//
//    public String doREPL(){
//        out.print(getPrompt());
//        String input = scanner.nextLine();
//        return eval(input);
//    }
//
////    private String eval(String input) {
////        try {
////            String[] tokens = input.toLowerCase().split(" ");
////            String cmd = (tokens.length > 0) ? tokens[0] : "help";
////            String[] params = Arrays.copyOfRange(tokens, 1, tokens.length);
//////            return switch (cmd) {
//////                case "logout" -> logout();
//////                case "list" -> listGames();
//////                case "join" -> joinGame(params);
//////                case "observe" -> watchGame(params);
//////                case "create" -> createGame(params);
//////                default -> "Command not recognized";
////            };
////        } catch (Exception e) {
////            throw new ResponseException("Error updating game");
////        }
////    }
//
//    private String logout(){
//        assertLoggedIn();
//        facade.logout(authToken);
//        this.authToken = null;
//        this.currentUserName = null;
//        currState = State.LOGGED_OUT;
//        return "Successfully logged out!";
//    }
//
//
//    public String getPrompt() {
//        String name = "[" + currentUserName + "]";
//        return name + " " + EscapeSequences.SET_TEXT_COLOR_BLUE +  ">>>" + EscapeSequences.RESET_TEXT_COLOR + " " ;
//    }
//
//}
