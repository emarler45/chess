package client;

import ui.EscapeSequences;

import java.util.Scanner;

public class Repl {

    public Repl() {
    }


    public String help(State state){
        return "These are the available commands:" + switch (state){
            case SIGNED_OUT -> """
                    help
                    register <username> <password> <email>
                    login <username> <password>
                    quit
                    """;
            case SIGNED_IN -> """
                    help
                    list
                    join <gameID> <color>
                    watch <gameID>
                    create <gameName>
                    """;
            case IN_GAME -> """
                    something
                    """;
            default -> null;
        };
    }

    public void printPrompt() {
        System.out.print("\n" + EscapeSequences.EMPTY + ">>> " + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }
}
