package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator extends ChessPiece{

    public PieceMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.type == PieceType.KING) {
            KingMovesCalculator kingMoveCalc = new KingMovesCalculator(this.color, this.type);
            return kingMoveCalc.pieceMoves(board, myPosition);
        } else if (this.type == PieceType.QUEEN){
            QueenMovesCalculator queenMoveCalc = new QueenMovesCalculator(this.color, this.type);
            return queenMoveCalc.pieceMoves(board, myPosition);
        } else if (this.type == PieceType.BISHOP){
            RookMovesCalculator rookMovesCalc = new RookMovesCalculator(this.color, this.type);
            return rookMovesCalc.pieceMoves(board, myPosition);
        } else if (this.type == PieceType.KNIGHT){
            KnightMovesCalculator knightMovesCalc = new KnightMovesCalculator(this.color, this.type);
            return knightMovesCalc.pieceMoves(board, myPosition);
        } else if (this.type == PieceType.ROOK){
            RookMovesCalculator rookMovesCalc = new RookMovesCalculator(this.color, this.type);
            return rookMovesCalc.pieceMoves(board, myPosition);
        } else if (this.type == PieceType.PAWN){
            PawnMovesCalculator pawnMovesCalc = new PawnMovesCalculator(this.color, this.type);
            return pawnMovesCalc.pieceMoves(board, myPosition);
        }
        throw new RuntimeException("Not implemented");
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
