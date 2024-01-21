package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator{

    public KnightMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int[][] newPositionsDisplacement = {{1, 2}, {1, -2}, {2, 1}, {2, -1}, {-1, 2}, {-1, -2}, {-2, 1}, {-2, -1}};
        for(int[] displace: newPositionsDisplacement){
            int newRow = myPosition.getRow() + displace[0];
            int newCol = myPosition.getColumn() + displace[1];
            if (checkBounds(newRow, newCol, board)){
                possibleMoves.add(new ChessMove(myPosition, new ChessPosition(newRow, newCol), null));
            }
        }
        return possibleMoves;
    }
}
