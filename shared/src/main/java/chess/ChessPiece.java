package chess;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.ArrayList;
import java.util.Objects;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    private final ChessGame.TeamColor color;
    private final ChessPiece.PieceType type;
    private ArrayList<ChessMove> moves;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    /**
     * The various different chess piece options
     */
    public enum PieceType {
        KING,
        QUEEN,
        BISHOP,
        KNIGHT,
        ROOK,
        PAWN
    }

    /**
     * @return Which team this chess piece belongs to
     */
    public ChessGame.TeamColor getTeamColor() {
        return color;
    }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() {
        return type;
    }


    private ArrayList<ChessMove> kingFunction(int startRow, int startCol, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> movesToReturn = new ArrayList<>();
        for (int i = -1; i <= 1; i++) {
            int endRow = i+startRow;
            if (endRow < 8 && endRow > 1) {
                for (int j = -1; j <= 1; j++) {
                    int endCol = j+startCol;
                    if (endCol < 8 && endCol > 1) {
                        ChessPiece destinationPiece = board.getPiece(new ChessPosition(endRow, endCol));
                        if (destinationPiece == null || destinationPiece.getTeamColor() != getTeamColor()){
                            movesToReturn.add(new ChessMove(myPosition, new ChessPosition(startRow + i, startCol + j), null));
                        }
                    }
                }
            }
        }
        return movesToReturn;
    }

    private ArrayList<ChessMove> pawnFunction(int pawnStep, int startRow, int startCol, ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movesToReturn = new ArrayList<>();
        // if piece is white it needs to move down, else move up

        int targetRow = pawnStep + startRow;
        ChessPiece targetPiece = board.getPiece(new ChessPosition(targetRow, startCol));
        if (targetPiece == null) {
            if (targetRow == 1 || targetRow == 8){
                movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol), PieceType.BISHOP));
                movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol), PieceType.ROOK));
                movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol), PieceType.KNIGHT));
                movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol), PieceType.QUEEN));
            }
            else {
                movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol), null));
            }
            int advRow = (pawnStep == 1) ? 2 : 7;
            if (startRow == advRow && board.getPiece(new ChessPosition(targetRow + pawnStep, startCol)) == null) {
                movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow+pawnStep, startCol), null));
            }
        }
        if (startCol != 1) {
            ChessPiece toAttack = board.getPiece(new ChessPosition(targetRow, startCol - 1));
            if (toAttack != null && toAttack.getTeamColor() != getTeamColor()) {
                if (targetRow == 1 || targetRow == 8){
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol-1), PieceType.BISHOP));
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol-1), PieceType.ROOK));
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol-1), PieceType.KNIGHT));
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol-1), PieceType.QUEEN));
                }
                else {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol - 1), null));
                }
            }
        }
        if (startCol != 8) {
            ChessPiece toAttack = board.getPiece(new ChessPosition(targetRow, startCol + 1));
            if (toAttack != null && toAttack.getTeamColor() != getTeamColor()) {
                if (targetRow == 1 || targetRow == 8){
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol+1), PieceType.BISHOP));
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol+1), PieceType.ROOK));
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol+1), PieceType.KNIGHT));
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol+1), PieceType.QUEEN));
                }
                else {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(targetRow, startCol + 1), null));
                }
            }
        }
//                TODO if needed En Passant
//                int enPassantRow = (pawnStep == 1) ? 5 : 4;
//                if (startRow == enPassantRow){
//                    if (startCol != 1) {
//                    }
//                }
    return movesToReturn; }

    private ArrayList<ChessMove> rookFunction(int startRow, int startCol, ChessBoard board, ChessPosition myPosition) {
        ArrayList<ChessMove> movesToReturn = new ArrayList<>();
        boolean colGoUp = true;
        boolean colGoDown = true;
        boolean rowGoUp = true;
        boolean rowGoDown = true;
        for (int i = 1; i <= 7; i++) {
            int moveToVal = startRow + i;
            if (rowGoUp && moveToVal <= 8) {
                ChessPiece destinationPiece = board.getPiece(new ChessPosition(moveToVal, startCol));
                if (destinationPiece == null) {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(moveToVal, startCol), null));
                } else if (destinationPiece.getTeamColor() == getTeamColor()) {
                    rowGoUp = false;
                } else {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(moveToVal, startCol), null));
                    rowGoUp = false;
                }
            }
            moveToVal = startRow - i;
            if (rowGoDown && moveToVal >= 1) {
                ChessPiece destinationPiece = board.getPiece(new ChessPosition(moveToVal, startCol));
                if (destinationPiece == null) {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(moveToVal, startCol), null));
                } else if (destinationPiece.getTeamColor() == getTeamColor()) {
                    rowGoDown = false;
                } else {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(moveToVal, startCol), null));
                    rowGoDown = false;
                }
            }
            moveToVal = startCol + i;
            if (colGoUp && moveToVal <= 8) {
                ChessPiece destinationPiece = board.getPiece(new ChessPosition(startRow, moveToVal));
                if (destinationPiece == null) {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(startRow, moveToVal), null));
                } else if (destinationPiece.getTeamColor() == getTeamColor()) {
                    colGoUp = false;
                } else {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(startRow, moveToVal), null));
                    colGoUp = false;
                }
            }
            moveToVal = startCol - i;
            if (colGoDown && moveToVal >= 1) {
                ChessPiece destinationPiece = board.getPiece(new ChessPosition(startRow, moveToVal));
                if (destinationPiece == null) {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(startRow, moveToVal), null));
                } else if (destinationPiece.getTeamColor() == getTeamColor()) {
                    colGoDown = false;
                } else {
                    movesToReturn.add(new ChessMove(myPosition, new ChessPosition(startRow, moveToVal), null));
                    colGoDown = false;
                }
            }
        }
        return movesToReturn;
    }

    private ArrayList<ChessMove> knightFunction(int startRow, int startCol, ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movesToReturn = new ArrayList<>();
        int[] sides = new int[]{-1, 1};
        for (int side : sides) {
            for (int half : sides) {
                if (startRow + 2 * side >= 1 && startRow + 2 * side <= 8 && startCol + half >= 1 && startCol + half <= 8) {
                    ChessPosition targetPosUpOver = new ChessPosition(startRow + 2 * side, startCol + half);
                    if (board.getPiece(targetPosUpOver) == null || board.getPiece(targetPosUpOver).getTeamColor() != getTeamColor()) {
                        movesToReturn.add(new ChessMove(myPosition, targetPosUpOver, null));
                    }
                }
                if (startRow + side >= 1 && startRow + side <= 8 && startCol + 2 * half >= 1 && startCol + 2 * half <= 8) {
                    ChessPosition targetPosOverUp = new ChessPosition(startRow + side, startCol + 2 * half);
                    if (board.getPiece(targetPosOverUp) == null || board.getPiece(targetPosOverUp).getTeamColor() != getTeamColor()) {
                        movesToReturn.add(new ChessMove(myPosition, targetPosOverUp, null));
                    }
                }
            }
        }
        return movesToReturn;
    }

    private ArrayList<ChessMove> bishopFunction(int startRow, int startCol, ChessBoard board, ChessPosition myPosition){
        ArrayList<ChessMove> movesToReturn = new ArrayList<>();
        int[] dirs = {-1, 1};
        for (int i : dirs){
            for (int j : dirs){
                for (int k = 1; k < 8; k++){
                    if (startRow + k*i < 1 || startRow + k*i > 8 || startCol + k*j < 1 || startCol + k*j > 8){
                        break;
                    } else {
                        ChessPosition targetPos = new ChessPosition(startRow + k * i, startCol + k * j);
                        ChessPiece targetPiece = board.getPiece(targetPos);
                        if (targetPiece == null) {
                            movesToReturn.add(new ChessMove(myPosition, targetPos, null));
                        } else if (targetPiece.getTeamColor() == getTeamColor()){
                            break;
                        } else {
                            movesToReturn.add(new ChessMove(myPosition, targetPos, null));
                            break;
                        }
                    }
                }
            }
        }
        return movesToReturn;
    }
    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        ChessPiece pieceToMove = board.getPiece(myPosition);
        int startRow = myPosition.getRow();
        int startCol = myPosition.getColumn();
        ArrayList<ChessMove> movesToReturn = new ArrayList<>();
        switch (pieceToMove.type) {
            case PieceType.KING -> {
                movesToReturn = kingFunction(startRow, startCol, board, myPosition);
            }
            case PieceType.PAWN -> {
                int pawnStep = (pieceToMove.getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;
                movesToReturn = pawnFunction(pawnStep, startRow, startCol, board, myPosition);
                }
            case PieceType.ROOK -> {
                movesToReturn = rookFunction(startRow, startCol, board, myPosition);
            }
            case PieceType.KNIGHT -> {
                movesToReturn = knightFunction(startRow, startCol, board, myPosition);
            }
            case PieceType.BISHOP -> {
                movesToReturn = bishopFunction(startRow, startCol, board, myPosition);
            }
            case PieceType.QUEEN -> {
              movesToReturn = rookFunction(startRow, startCol, board, myPosition);
              movesToReturn.addAll(bishopFunction(startRow, startCol, board, myPosition));
            }
        }
        return movesToReturn;
    }

    @Override
    public boolean equals(Object o) {

        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessPiece that = (ChessPiece) o;
        return color == that.color && type == that.type && Objects.equals(moves, that.moves);
    }

    @Override
    public int hashCode() {
        return Objects.hash(color, type, moves);
    }

    @Override
    public String toString() {
        return "ChessPiece{" +
                "type=" + type +
                ", color=" + color +
                '}';
    }
}
