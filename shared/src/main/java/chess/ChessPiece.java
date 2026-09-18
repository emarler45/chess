package chess;

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


//    private boolean rookHelper()
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
        return switch (pieceToMove.type) {
            case PieceType.KING -> {
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
            yield movesToReturn;}
            case PieceType.PAWN -> null;
            case PieceType.ROOK -> {
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
                yield movesToReturn;}
            case PieceType.KNIGHT -> null;
            case PieceType.BISHOP -> null;
            case PieceType.QUEEN -> null;
        };
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
