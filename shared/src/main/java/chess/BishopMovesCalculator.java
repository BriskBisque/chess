package chess;

import java.util.Collection;

public class BishopMovesCalculator extends PieceMovesCalculator{

    public BishopMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        throw new RuntimeException("Not implemented");
    }
}
