package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PieceMovesCalculator {

    public ChessGame.TeamColor color;
    public ChessPiece.PieceType type;

    public ChessPiece(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        this.color = pieceColor;
        this.type = type;
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        if (this.type == PieceType.KING) {
            KingMovesCalculator kingMoveCalc = new KingMovesCalculator(this.color, this.type);
            return kingMoveCalc.pieceMoves(board, myPosition);
        } else if (this.type == PieceType.QUEEN){
            QueenMovesCalculator queenMoveCalc = new QueenMovesCalculator(this.color, this.type);
            return queenMoveCalc.pieceMoves(board, myPosition);
        } else if (this.type == PieceType.BISHOP){
            BishopMovesCalculator bishopMovesCalc = new BishopMovesCalculator(this.color, this.type);
            return bishopMovesCalc.pieceMoves(board, myPosition);
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
        int newRow = myPosition.getRow() + changeRow;
        int newCol = myPosition.getColumn() + changeCol;
        while (checkBounds(newRow, newCol, board)) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPosition) == null) {
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

    // checks if the space is valid
    public boolean checkBounds(int newRow, int newCol, ChessBoard board){
        if ((newRow < 9 && newRow > 0) && (newCol < 9 && newCol > 0)) {
            ChessPosition newPosition = new ChessPosition(newRow, newCol);
            if (board.getPiece(newPosition) != null) {
                return this.color != board.getPiece(newPosition).color;
            } else {
                return true;
            }
        } else {
            return false;
        }
    }
}
