package client;

import chess.*;
import ui.EscapeSequences;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        new ChessClient("http://localhost:8080").run();
    }
}
