package client;

public class SignedInREPL extends Repl{

    public SignedInREPL(){}

    public State loop(){
        return State.SIGNED_IN;
    }

    @Override
    public String eval(String input) {
        return "";
    }

    @Override
    public void help() {

    }
}
