package chess;

import java.util.Collection;

public class KnightMovesCalculator extends PieceMovesCalculator{

    public KnightMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }
}
