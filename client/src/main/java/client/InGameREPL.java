package client;

public class InGameREPL extends Repl{

    public InGameREPL(){}

    public State loop(){
        return State.IN_GAME;
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public void help() {

    }
}
