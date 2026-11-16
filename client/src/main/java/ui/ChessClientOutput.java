package ui;

public class ChessClientOutput {

    private outKind out;

    public ChessClientOutput(){
        out = outKind.SYSTEM;
    }

    public ChessClientOutput(String kind){
        switch (kind){
            case "System" -> out = outKind.SYSTEM;
            case "File" -> out = outKind.FILE;
        }
    }

    public void print(String message){
        if (out == outKind.SYSTEM){
            System.out.print(message);
        }
    }

    public void printline(String message){
        if (out == outKind.SYSTEM){
            System.out.println(message);
        }
    }



    private enum outKind {
        SYSTEM,
        FILE
    }
}
