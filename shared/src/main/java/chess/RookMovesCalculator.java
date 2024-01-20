package chess;

import java.util.Collection;

public class RookMovesCalculator extends PieceMovesCalculator{

    public RookMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }
}
