package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * A class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    private ChessBoard board = new ChessBoard();
    private TeamColor currTeam = TeamColor.WHITE;

    private ArrayList<ChessPosition> whitePos = new ArrayList<>();
    private ArrayList<ChessPosition> blackPos = new ArrayList<>();

    private ChessPosition blackKingPos = new ChessPosition(8, 5);
    private ChessPosition whiteKingPos = new ChessPosition(1, 5);

    public ChessGame() {
        int[] rows = new int[]{1, 2, 7, 8};
        for (int row : rows) {
            for (int i = 1; i <= 8; i++){
                if (row < 4){
                    whitePos.add(new ChessPosition(row, i));
                } else {
                    blackPos.add(new ChessPosition(row, i));
                }
            }
        }
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return currTeam;
    }

    /**
     * Sets which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        currTeam = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets all valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessBoard boardAtStart = board;
        ChessPiece currPiece = board.getPiece(startPosition);
        if (currPiece == null) return null;
        return currPiece.pieceMoves(board, startPosition);
        // Use isInCheck to determine which moves help and which dont
    }

    /**
     * Makes a move in the chess game
     *
     * @param move chess move to perform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        ChessPiece pieceToMove = board.getPiece(start);
        if (pieceToMove == null || pieceToMove.getTeamColor() != currTeam) throw new InvalidMoveException();
        Collection<ChessMove> validList = validMoves(start);
        if (validList.contains(move)){
            board.removePiece(start);
            if (move.getPromotionPiece() != null){
                board.addPiece(end, new ChessPiece(currTeam, move.getPromotionPiece()));
            } else {
                board.addPiece(end, pieceToMove);
            }
        } else throw new InvalidMoveException();
        if (pieceToMove.getPieceType() == ChessPiece.PieceType.KING){
            if (pieceToMove.getTeamColor() == TeamColor.WHITE){
                whiteKingPos = move.getEndPosition();
            } else {
                blackKingPos = move.getEndPosition();
            }
        }
        currTeam = (currTeam == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        TeamColor enemyColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
        ChessPosition friendlyKingPos = (teamColor == TeamColor.WHITE) ? whiteKingPos : blackKingPos;
        Collection<ChessPosition> enemyMoves = getTeamPos(enemyColor);
        return (enemyMoves.contains(friendlyKingPos));
    }

    private Collection<ChessPosition> getTeamPos(TeamColor color){
        ArrayList<ChessPosition> teamPos = (color == TeamColor.WHITE) ? whitePos : blackPos;
        ArrayList<ChessPosition> posToReturn = new ArrayList<>();
        for (ChessPosition piecePos : teamPos){
            ChessPiece currPiece = board.getPiece(piecePos);
            Collection<ChessMove> currMoves = currPiece.pieceMoves(board, piecePos);
            for (ChessMove move : currMoves){
                posToReturn.add(move.getEndPosition());
            }
        }
        return posToReturn;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return board;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        ChessGame chessGame = (ChessGame) o;
        return Objects.equals(board, chessGame.board) && currTeam == chessGame.currTeam;
    }

    @Override
    public int hashCode() {
        return Objects.hash(board, currTeam);
    }
}
