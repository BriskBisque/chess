package chess;

import java.util.ArrayList;
import java.util.Collection;

public class QueenMovesCalculator extends PieceMovesCalculator{

    public QueenMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
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
}
