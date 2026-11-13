package client;

import java.util.Scanner;

public class InitialREPL extends Repl{

    private final Scanner scanner = super.scanner;

    public InitialREPL(){}

    @Override
    public State loop(){
        help();
        String action = "";
        while (true){
            super.printPrompt();
            String response = scanner.nextLine();
            try {
                return eval(response);
            } catch (Exception e){
                System.out.println("There was an error.");
            }
        }
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public void help(){
        String msg = """
                    These are the available commands:
                    help
                    register <username> <password> <email>
                    login <username> <password>
                    quit
                    """;
    }

    public State quit(){
        return State.QUIT;
    }
}
