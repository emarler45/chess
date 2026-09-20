package chess;


import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * A chessboard that can hold and rearrange chess pieces.
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessBoard {

    private ChessPiece[][] squares = new ChessPiece[8][8];

    public ChessBoard() {
        
    }

    /**
     * Adds a chess piece to the chessboard
     *
     * @param position where to add the piece to
     * @param piece    the piece to add
     */
    public void addPiece(ChessPosition position, ChessPiece piece) {
        squares[position.getRow()-1][position.getColumn()-1] = piece;
    }

    /**
     * Gets a chess piece on the chessboard
     *
     * @param position The position to get the piece from
     * @return Either the piece at the position, or null if no piece is at that
     * position
     */
    public ChessPiece getPiece(ChessPosition position) {
        return squares[position.getRow()-1][position.getColumn()-1];
    }


    public void removePiece(ChessPosition position) {
        squares[position.getRow()-1][position.getColumn()-1] = null;
    }

    /**
     * Sets the board to the default starting board
     * (How the game of chess normally starts)
     */
    public void resetBoard() {
        squares = new ChessPiece[8][8];
        int counter = 0;
        for (ChessPiece.PieceType type : ChessPiece.PieceType.values()){
            if (counter == 3 || counter == 4){
                addPiece(new ChessPosition(1, 1+counter), new ChessPiece(ChessGame.TeamColor.WHITE, type));
                addPiece(new ChessPosition(8, 1+counter), new ChessPiece(ChessGame.TeamColor.BLACK, type));
                counter++;
            } else if (counter == 5){
                for (int i = 1; i <= 8; i++){
                    addPiece(new ChessPosition(2, i), new ChessPiece(ChessGame.TeamColor.WHITE, type));
                    addPiece(new ChessPosition(7, i), new ChessPiece(ChessGame.TeamColor.BLACK, type));
                }
            } else {
                addPiece(new ChessPosition(1, 1 + counter), new ChessPiece(ChessGame.TeamColor.WHITE, type));
                addPiece(new ChessPosition(1, 8 - counter), new ChessPiece(ChessGame.TeamColor.WHITE, type));
                addPiece(new ChessPosition(8, 1 + counter), new ChessPiece(ChessGame.TeamColor.BLACK, type));
                addPiece(new ChessPosition(8, 8 - counter), new ChessPiece(ChessGame.TeamColor.BLACK, type));
                counter++;
            }
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessBoard that = (ChessBoard) o;
        return Objects.deepEquals(squares, that.squares);
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(squares);
    }

    @Override
    public String toString() {
        return "ChessBoard{" +
                "squares=" + Arrays.toString(squares) +
                '}';
    }
}
