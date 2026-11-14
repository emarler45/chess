package client;

import chess.*;
import ui.EscapeSequences;

public class ClientMain {
    public static void main(String[] args) {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        new ChessClient("http://localhost:8080").run();
    }

    private static void testTextRendering(){
        System.out.println("♕ 240 Chess Client: " + "\n" + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " 2 " +
                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.EMPTY +
                EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_WHITE + EscapeSequences.WHITE_KING +
                EscapeSequences.RESET_BG_COLOR +  "\n" + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " 1 " +
                EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.WHITE_QUEEN +
                EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.BLACK_QUEEN +
                EscapeSequences.RESET_BG_COLOR + "\n" + EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
                EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " # " + EscapeSequences.SET_TEXT_COLOR_BLACK +
//                EscapeSequences.EMPTY +
                " a   b   c  " +
                EscapeSequences.RESET_BG_COLOR);
    }
}
