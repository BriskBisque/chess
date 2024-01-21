package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{

    public QueenMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        int[][] newPositionsDisplacement = {{1, 0}, {1, 1}, {0, 1}, {-1, 1}, {-1, 0}, {-1, -1}, {0, -1}, {1, -1}};
        for (int[] displace: newPositionsDisplacement){
            possibleMoves.addAll(checkDirection(displace[0], displace[1], myPosition, board));
        }
        return possibleMoves;
    }
}
