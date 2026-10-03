package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
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

    private ChessPosition blackKingPos;
    private ChessPosition whiteKingPos;

    public ChessGame() {
        board.resetBoard();
        setPiecePos();
    }

    private void setPiecePos() {
        whitePos = new ArrayList<>();
        blackPos = new ArrayList<>();
        for (int i = 1; i <= 8; i++){
            for (int j = 1; j <= 8; j++){
                ChessPosition currPos = new ChessPosition(i , j);
                ChessPiece targetPiece = board.getPiece(currPos);
                if (targetPiece != null){
                    if (targetPiece.getTeamColor() == TeamColor.WHITE){
                        whitePos.add(currPos);
                        if (targetPiece.getPieceType() == ChessPiece.PieceType.KING){
                            whiteKingPos = currPos;
                        }
                    } else {
                        blackPos.add(currPos);
                        if (targetPiece.getPieceType() == ChessPiece.PieceType.KING){
                            blackKingPos = currPos;
                        }
                    }
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
        ChessPiece currPiece = board.getPiece(startPosition);
        if (currPiece == null) return null;
        Collection<ChessMove> allMoves = currPiece.pieceMoves(board, startPosition);
        allMoves.removeIf(currMove -> !testMove(currMove, currPiece));
        return allMoves;
    }

    /**
     * Makes a move and checks if it's legal
     * @param move move to test
     * @param piece piece to move
     * @return True if check is resolved, False if not
    */
    private boolean testMove(ChessMove move, ChessPiece piece){
        ChessBoard boardAtStart = new ChessBoard(board);
        ChessPosition start = move.getStartPosition();
        ChessPosition end = move.getEndPosition();
        if (move.getPromotionPiece() != null){
            board.addPiece(end, new ChessPiece(currTeam, move.getPromotionPiece()));
        } else {
            board.addPiece(end, piece);
        }
        board.removePiece(start);
        boolean checkResolved = !isInCheck(piece.getTeamColor());
        if (piece.getPieceType() == ChessPiece.PieceType.KING){
            if (piece.getTeamColor() == TeamColor.WHITE){
                ChessPosition oldWhiteKingPos = new ChessPosition(whiteKingPos.getRow(), whiteKingPos.getColumn());
                whiteKingPos = end;
                checkResolved = !isInCheck(piece.getTeamColor());
                whiteKingPos = oldWhiteKingPos;
            } else {
                ChessPosition oldBlackKingPos = new ChessPosition(blackKingPos.getRow(), blackKingPos.getColumn());
                blackKingPos = end;
                checkResolved = !isInCheck(piece.getTeamColor());
                blackKingPos = oldBlackKingPos;
            }
        }
        setBoard(boardAtStart);
        return checkResolved;
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
            if (board.getPiece(end) != null){
                if (currTeam == TeamColor.WHITE){
                    blackPos.remove(end);
                } else {
                    whitePos.remove(end);
                }
            }
            if (move.getPromotionPiece() != null){
                board.addPiece(end, new ChessPiece(currTeam, move.getPromotionPiece()));
            } else {
                board.addPiece(end, pieceToMove);
            }
        } else throw new InvalidMoveException();
        if (currTeam == TeamColor.WHITE){
            if (pieceToMove.getPieceType() == ChessPiece.PieceType.KING){
                whiteKingPos = end;
            }
            whitePos.add(end);
            whitePos.remove(start);
        } else {
            if (pieceToMove.getPieceType() == ChessPiece.PieceType.KING){
                blackKingPos = end;
            }
            blackPos.add(end);
            blackPos.remove(start);
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
        Collection<ChessPosition> enemyMoves = getTeamAtkPos(enemyColor);
        return enemyMoves != null && (enemyMoves.contains(friendlyKingPos));
    }

    private Collection<ChessPosition> getTeamAtkPos(TeamColor color){
        ArrayList<ChessPosition> teamPos = (color == TeamColor.WHITE) ? whitePos : blackPos;
        ArrayList<ChessPosition> posToReturn = new ArrayList<>();
        for (ChessPosition piecePos : teamPos){
            ChessPiece currPiece = board.getPiece(piecePos);
            Collection<ChessMove> currMoves = currPiece.pieceMoves(board, piecePos);
            for (ChessMove move : currMoves){
                posToReturn.add(move.getEndPosition());
            }
        }
        return (posToReturn.isEmpty()) ? null : posToReturn;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        ArrayList<ChessPosition> friendPos = (teamColor == TeamColor.WHITE) ? whitePos : blackPos;
        ArrayList<ChessMove> possibleMoves = new ArrayList<>();
        if (isInCheck(teamColor)){
            for (ChessPosition pos : friendPos){
                ChessPiece currPiece = board.getPiece(pos);
                possibleMoves.addAll(validMoves(pos));
//                possibleMoves.addAll(currPiece.pieceMoves(board, pos));
            }
            return (possibleMoves.isEmpty());
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves while not in check.
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        ArrayList<ChessPosition> friendlyPos = (teamColor == TeamColor.WHITE) ? whitePos : blackPos;
        ArrayList<ChessMove> validMovesList = new ArrayList<>();
        for (ChessPosition piecePos : friendlyPos){
            validMovesList.addAll(validMoves(piecePos));
        }
        return validMovesList.isEmpty() && !isInCheck(teamColor);
    }

    /**
     * Sets this game's chessboard to a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.board = board;
        setPiecePos();
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
