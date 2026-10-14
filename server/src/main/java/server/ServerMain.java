package server;

import chess.*;

import java.awt.*;

public class ServerMain {
    public static void main(String[] args) {
//        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
//        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        int port = 8080;
        server.run(port);
        System.out.println("Server live on port " + port);
    }
}
