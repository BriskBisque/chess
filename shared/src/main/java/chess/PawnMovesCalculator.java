package chess;

import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator{

    public PawnMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }
}
