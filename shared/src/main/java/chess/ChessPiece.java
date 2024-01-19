package chess;

import java.util.Collection;
import java.util.ArrayList;

/**
 * Represents a single chess piece
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessPiece {

    public ChessGame.TeamColor color;
    public ChessPiece.PieceType type;

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
    public ChessGame.TeamColor getTeamColor() { return this.color; }

    /**
     * @return which type of chess piece this piece is
     */
    public PieceType getPieceType() { return this.type; }

    /**
     * Calculates all the positions a chess piece can move to
     * Does not take into account moves that are illegal due to leaving the king in
     * danger
     *
     * @return Collection of valid moves
     */
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.type == PieceType.KING) {
            return kingMove(board, myPosition);
        } else if (this.type == PieceType.QUEEN){
            return queenMove(board, myPosition);
        } else if (this.type == PieceType.BISHOP){
            throw new RuntimeException("Not implemented");
        } else if (this.type == PieceType.KNIGHT){
            throw new RuntimeException("Not implemented");
        } else if (this.type == PieceType.ROOK){
            throw new RuntimeException("Not implemented");
        } else if (this.type == PieceType.PAWN){
            throw new RuntimeException("Not implemented");
        }
        throw new RuntimeException("Not implemented");
    }

    public Collection<ChessMove> kingMove(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int[][] newPositionsDisplacement = {{-1, 0}, {0, -1}, {1, 0}, {0, 1}};
        for(int[] displace: newPositionsDisplacement){
            int newRow = myPosition.row + displace[0];
            int newCol = myPosition.col + displace[1];
            if ((newRow < 8 && newRow >= 0) && (newCol < 8 && newCol >= 0)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if (board.getPiece(newPosition) != null) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return possibleMoves;
    }

    public Collection<ChessMove> queenMove(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int newRow = myPosition.row;
        int newCol = myPosition.col;
        // down and to the right
        possibleMoves.addAll(checkDirection(1, 1, myPosition, board));
        // down
        possibleMoves.addAll(checkDirection(1, 0, myPosition, board));
        // down to the left
        possibleMoves.addAll(checkDirection(1, -1, myPosition, board));
        // left
        possibleMoves.addAll(checkDirection(0, -1, myPosition, board));
        // left and up
        possibleMoves.addAll(checkDirection(-1, -1, myPosition, board));
        // up
        possibleMoves.addAll(checkDirection(-1, 0, myPosition, board));
        // up and right
        possibleMoves.addAll(checkDirection(-1, 1, myPosition, board));
        // right
        possibleMoves.addAll(checkDirection(0, 1, myPosition, board));
        return possibleMoves;
    }

    public Collection<ChessMove> checkDirection(int changeRow, int changeCol, ChessPosition myPosition, ChessBoard board){
        Collection<ChessMove> tempMoves = new ArrayList<>();
        int newRow = myPosition.row;
        int newCol = myPosition.col;
        while ((newRow < 8 && newRow >= 0) && (newCol < 8 && newCol >= 0)) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPosition) != null) {
                tempMoves.add(new ChessMove(myPosition, newPosition, null));
            } else {
                tempMoves.add(new ChessMove(myPosition, newPosition, null));
                break;
            }
            newRow = newRow + changeRow;
            newCol = newCol + changeCol;
        }
        return tempMoves;
    }
}

