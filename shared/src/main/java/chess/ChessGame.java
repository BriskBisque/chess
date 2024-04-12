package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {

    public TeamColor teamTurn;
    public ChessBoard board;
    public ChessBoard clonedBoard;
    public ChessMove lastMove;
    public ChessPiece takenPiece;
    public boolean isResigned;

    public ChessGame() {
        this.board = new ChessBoard();
        this.teamTurn = TeamColor.WHITE;
        this.clonedBoard = new ChessBoard();
        this.isResigned = false;
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamTurn;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamTurn = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = this.board.getPiece(startPosition);
        if (piece != null) {
            Collection<ChessMove> pieceMoves = piece.pieceMoves(this.board, startPosition);
            Collection<ChessMove> validPieceMoves = new ArrayList<>();
            this.setClonedBoard();
            for (ChessMove move: pieceMoves){
                this.clonedMakeMove(move);
                if (!this.clonedBoardInCheck(piece.getTeamColor())){
                    validPieceMoves.add(move);
                }
                this.undoMove(this.clonedBoard);
            }
            return validPieceMoves;
        } else {
            return null;
        }
    }

    public void setClonedBoard(){
        for (int i = 0; i < this.board.pieces.length; i++){
            for (int j = 0; j < this.board.pieces[i].length; j++){
                ChessPosition position = new ChessPosition(i+1, j+1);
                this.clonedBoard.addPiece(position, this.board.getPiece(position));
            }
        }
    }

    public boolean clonedBoardInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = this.getKingPosition(teamColor, this.clonedBoard);
        ChessPiece[][] pieces = this.clonedBoard.pieces;
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                if (pieces[i][j] != null && pieces[i][j].color != teamColor){
                    ChessPosition piecePosition = new ChessPosition(i+1, j+1);
                    Collection<ChessMove> pieceMoves = pieces[i][j].pieceMoves(this.clonedBoard, piecePosition);
                    for (ChessMove move: pieceMoves){
                        if (move.endPos.equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public void clonedMakeMove(ChessMove move){
        ChessPiece movedPiece = this.clonedBoard.getPiece(move.getStartPosition());
        this.takenPiece = this.clonedBoard.getPiece(move.getEndPosition());
        this.clonedBoard.addPiece(move.getEndPosition(), movedPiece);
        this.clonedBoard.addPiece(move.getStartPosition(), null);
        this.lastMove = move;
    }

    public void undoMove(ChessBoard board){
        ChessPiece movedPiece = board.getPiece(this.lastMove.getEndPosition());
        board.addPiece(this.lastMove.getEndPosition(), this.takenPiece);
        board.addPiece(this.lastMove.getStartPosition(), movedPiece);
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPiece movedPiece = this.board.getPiece(move.getStartPosition());
        if (this.checkMoveBounds(move) || movedPiece == null || movedPiece.color != this.teamTurn || !this.moveIsValid(move, this.validMoves(move.startPos))){
            throw new InvalidMoveException();
        }
        this.takenPiece = this.board.getPiece(move.getEndPosition());
        if (movedPiece.type == ChessPiece.PieceType.PAWN) {
            if (move.promoPiece != null){
                movedPiece.type = move.promoPiece;
            }
        }
        this.board.addPiece(move.getEndPosition(), movedPiece);
        this.board.addPiece(move.getStartPosition(), null);
        this.lastMove = move;
        if (this.teamTurn == TeamColor.BLACK){
            this.teamTurn = TeamColor.WHITE;
        } else {
            this.teamTurn = TeamColor.BLACK;
        }
    }

    public boolean checkMoveBounds(ChessMove move){
        return move.endPos.getRow() > 8 && move.endPos.getRow() <= 0 && move.endPos.getColumn() > 8 && move.endPos.getColumn() <= 0;
    }

    public boolean moveIsValid(ChessMove move, Collection<ChessMove> validMoves){
        for (ChessMove validMove: validMoves){
            if (validMove.equals(move)){
                return true;
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = this.getKingPosition(teamColor, this.board);
        ChessPiece[][] pieces = this.board.pieces;
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                if (pieces[i][j] != null && pieces[i][j].color != teamColor){
                    ChessPosition piecePosition = new ChessPosition(i+1, j+1);
                    Collection<ChessMove> pieceMoves = pieces[i][j].pieceMoves(this.board, piecePosition);
                    for (ChessMove move: pieceMoves){
                        if (move.endPos.equals(kingPosition)) {
                            return true;
                        }
                    }
                }
            }
        }
        return false;
    }

    public ChessPosition getKingPosition(TeamColor teamColor, ChessBoard board){
        ChessPiece[][] pieces = board.pieces;
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                if (pieces[i][j] != null && pieces[i][j].color == teamColor && pieces[i][j].type == ChessPiece.PieceType.KING){
                    return new ChessPosition(i+1, j+1);
                }
            }
        }
        return null;
    }

    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        if (this.isInCheck(teamColor)) {
            return this.checkForAnyMove(teamColor);
        } else {
            return false;
        }
    }

    public boolean checkForAnyMove(TeamColor teamColor){
        ChessPiece[][] pieces = this.board.pieces;
        for (int i = 0; i < pieces.length; i++){
            for (int j = 0; j < pieces[i].length; j++){
                if (pieces[i][j] != null && pieces[i][j].color == teamColor && this.validMoves(new ChessPosition(i+1, j+1)) != null){
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        if (!this.isInCheck(teamColor) && teamColor == this.teamTurn) {
            return this.checkForAnyMove(teamColor);
        } else {
            return false;
        }
    }

    /**
     * Sets this game's chessboard with a given board
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
        return this.board;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        ChessGame chessGame = (ChessGame) o;
        return getTeamTurn() == chessGame.getTeamTurn() && Objects.equals(getBoard(), chessGame.getBoard());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getTeamTurn(), getBoard());
    }

    @Override
    public String toString() {
        return "ChessGame{" +
                "teamTurn=" + teamTurn +
                ", board=" + board +
                '}';
    }

    public boolean isResigned() {
        return isResigned;
    }

    public void setResigned(boolean resigned) {
        isResigned = resigned;
    }
}
