package ui;

import chess.*;

import java.util.Objects;

public class BoardRenderer {

    private static final String WHITE_LETTER_BORDER = EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
            EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " # " +
            EscapeSequences.SET_TEXT_COLOR_BLACK + " a   b   c  d   e  f   g   h " +
            EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " # " +
            EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";
    private static final String BLACK_LETTER_BORDER = EscapeSequences.SET_BG_COLOR_LIGHT_GREY +
            EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY + " # " +
            EscapeSequences.SET_TEXT_COLOR_BLACK + " h   g   f   e   d   c   b   a  " +
            EscapeSequences.RESET_BG_COLOR + EscapeSequences.RESET_TEXT_COLOR + "\n";

    public BoardRenderer(){}

    public String renderBoard(ChessBoard board, ChessGame.TeamColor team){
        int start;
        int end;
        int step;
        String letterBorder;
        String currSquareFormat = EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
        StringBuilder builder = new StringBuilder();
        if (team == ChessGame.TeamColor.WHITE){
            start = 1;
            end = 8;
            step = 1;
            letterBorder = WHITE_LETTER_BORDER;
        } else {
            start = 8;
            end = 1;
            step = -1;
            letterBorder = BLACK_LETTER_BORDER;
        }
        builder.append(letterBorder);
        for (int i = start; i <= end; i = i + step){
            builder.append(getBorderNumber(i));
            for (int j = start; j <= end; j = j + step){
                ChessPosition currPos = new ChessPosition(i, j);
                builder.append(currSquareFormat);
                builder.append(mapPiece(board.getPiece(currPos)));
                currSquareFormat = flipSquareFormatting(currSquareFormat);
            }
            builder.append(getBorderNumber(i));
            builder.append(EscapeSequences.RESET_BG_COLOR + "\n");
            currSquareFormat = flipSquareFormatting(currSquareFormat);
        }
        builder.append(letterBorder);
        return builder.toString();
    }

    private String getBorderNumber(int num){
        return EscapeSequences.SET_TEXT_COLOR_BLACK + EscapeSequences.SET_BG_COLOR_LIGHT_GREY + " %d ".formatted(num);
    }

    private String flipSquareFormatting(String toFlip){
        if (Objects.equals(toFlip, EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY)){
            return EscapeSequences.SET_BG_COLOR_BLACK + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
        } else {
            return EscapeSequences.SET_BG_COLOR_WHITE + EscapeSequences.SET_TEXT_COLOR_LIGHT_GREY;
        }
    }

    private String mapPiece(ChessPiece piece){
        if (piece == null){
            return EscapeSequences.EMPTY;
        }
        return switch (piece.getTeamColor()){
            case WHITE -> mapPieceToStringWhite(piece);
            case BLACK -> mapPieceToStringBlack(piece);
        };
    }

    private String mapPieceToStringWhite(ChessPiece piece){
        return switch (piece.getPieceType()){
            case KING -> EscapeSequences.WHITE_KING;
            case QUEEN -> EscapeSequences.WHITE_QUEEN;
            case ROOK -> EscapeSequences.WHITE_ROOK;
            case KNIGHT -> EscapeSequences.WHITE_KNIGHT;
            case BISHOP -> EscapeSequences.WHITE_BISHOP;
            case PAWN -> EscapeSequences.WHITE_PAWN;
        };
    }

    private String mapPieceToStringBlack(ChessPiece piece){
        return switch (piece.getPieceType()){
            case KING -> EscapeSequences.BLACK_KING;
            case QUEEN -> EscapeSequences.BLACK_QUEEN;
            case ROOK -> EscapeSequences.BLACK_ROOK;
            case KNIGHT -> EscapeSequences.BLACK_KNIGHT;
            case BISHOP -> EscapeSequences.BLACK_BISHOP;
            case PAWN -> EscapeSequences.BLACK_PAWN;
        };
    }

}
