package client;

import server.ServerFacade;

public class ChessClient {

    private String currentUserName;
    private final ServerFacade facade;
    private State currState = State.SIGNED_OUT;


    public ChessClient(String url){
        facade = new ServerFacade(url);
    }

    public void run() {
        Repl initialRepl = new InitialREPL();
        Repl signedInRepl = new SignedInREPL();
        Repl playGameRepl = new InGameREPL();
        System.out.println("Welcome to ekm45's Chess Game! Here are the possible commands:");
        while (currState != State.QUIT) {
            switch (currState) {
                case SIGNED_OUT -> currState = initialRepl.loop();
                case SIGNED_IN -> currState = signedInRepl.loop();
                case IN_GAME -> currState = playGameRepl.loop();
            }
        }
        System.out.println("Goodbye!");
    }

}
