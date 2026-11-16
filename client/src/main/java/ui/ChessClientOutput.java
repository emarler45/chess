package ui;

public class ChessClientOutput {

    private OutKind out;

    public ChessClientOutput(){
        out = OutKind.SYSTEM;
    }

    public ChessClientOutput(String kind){
        switch (kind){
            case "System" -> out = OutKind.SYSTEM;
            case "File" -> out = OutKind.FILE;
        }
    }

    public void print(String message){
        if (out == OutKind.SYSTEM){
            System.out.print(message);
        }
    }

    public void printline(String message){
        if (out == OutKind.SYSTEM){
            System.out.println(message);
        }
    }



    private enum OutKind {
        SYSTEM,
        FILE
    }
}
