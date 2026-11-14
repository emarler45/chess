package client;

import ui.EscapeSequences;

public class Repl {

    public Repl() {
    }



    public void printPrompt() {
        System.out.print("\n" + EscapeSequences.EMPTY + ">>> " + EscapeSequences.SET_TEXT_COLOR_BLUE);
    }
}
