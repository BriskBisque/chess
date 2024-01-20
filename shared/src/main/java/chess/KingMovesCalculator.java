package chess;

import java.util.ArrayList;
import java.util.Collection;

public class KingMovesCalculator extends PieceMovesCalculator{

    public KingMovesCalculator(ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
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

}
