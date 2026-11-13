package client;

import ui.EscapeSequences;

import java.util.Scanner;

public abstract class Repl {

    public Scanner scanner = new Scanner(System.in);

    public abstract State loop();

    public abstract String eval(String input);

    public abstract void help();

    public void printPrompt() {
        System.out.print("\n" + EscapeSequences.EMPTY + ">>> " + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }
}
