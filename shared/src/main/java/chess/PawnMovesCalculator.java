package chess;

import java.util.ArrayList;
import java.util.Collection;

public class PawnMovesCalculator extends PieceMovesCalculator{

    public int[][] newPositionsDisplacement;
    public int startRow;
    public int[] startDisplacement;
    public ChessPiece.PieceType[] promoTypes = new ChessPiece.PieceType[] {ChessPiece.PieceType.QUEEN, ChessPiece.PieceType.BISHOP, ChessPiece.PieceType.KNIGHT, ChessPiece.PieceType.ROOK};

    public PawnMovesCalculator (ChessGame.TeamColor pieceColor, ChessPiece.PieceType type) {
        super(pieceColor, type);
        if (this.color == ChessGame.TeamColor.BLACK) {
            this.newPositionsDisplacement = new int[][] {{-1, 0}, {-1, -1}, {-1, 1}};
            this.startRow = 7;
            this.startDisplacement = new int[] {-2, 0};
        } else {
            this.newPositionsDisplacement = new int[][] {{1, 0}, {1, -1}, {1, 1}};
            this.startRow = 2;
            this.startDisplacement = new int[] {2, 0};
        }
    }

    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition){
        Collection<ChessMove> possibleMoves = new ArrayList<>();
        boolean checkFrontMove = false;
        for(int[] displace: this.newPositionsDisplacement){
            int newRow = myPosition.getRow() + displace[0];
            int newCol = myPosition.getColumn() + displace[1];
            if (this.checkBounds(newRow, newCol, board)) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if ((displace[1] != 0) && (board.getPiece(newPosition) != null) && (board.getPiece(newPosition).color != this.color)) {
                    possibleMoves.addAll(this.addPromo(board, myPosition, newPosition));
                }
                if ((displace[1] == 0) && (board.getPiece(newPosition) == null)) {
                    if (checkBounds(newRow, newCol, board)) {
                        checkFrontMove = true;
                        possibleMoves.addAll(this.addPromo(board, myPosition, newPosition));
                    }
                }
            }
        }
        if (myPosition.getRow() == this.startRow){
            int newRow = myPosition.getRow() + this.startDisplacement[0];
            int newCol = myPosition.getColumn() + this.startDisplacement[1];
            if ((newRow < 9 && newRow > 0) && (newCol < 9 && newCol > 0) && checkFrontMove) {
                ChessPosition newPosition = new ChessPosition(newRow, newCol);
                if ((board.getPiece(newPosition) == null)) {
                    possibleMoves.add(new ChessMove(myPosition, newPosition, null));
                }
            }
        }
        return possibleMoves;
    }

    public Collection<ChessMove> addPromo(ChessBoard board, ChessPosition myPosition, ChessPosition newPosition){
        Collection<ChessMove> tempMoves = new ArrayList<>();
        if ((newPosition.getRow() < 9 && newPosition.getRow() > 0) && (newPosition.getColumn() < 9 && newPosition.getColumn() > 0)) {
            if ((this.color == ChessGame.TeamColor.BLACK) && (newPosition.getRow() == 1)) {
                for (ChessPiece.PieceType promoType : this.promoTypes) {
                    if ((promoType != ChessPiece.PieceType.PAWN) && (promoType != ChessPiece.PieceType.KING)) {
                        tempMoves.add(new ChessMove(myPosition, newPosition, promoType));
                    }
                }
            } else if ((this.color == ChessGame.TeamColor.WHITE) && (newPosition.getRow() == 8)){
                for (ChessPiece.PieceType promoType : this.promoTypes) {
                    if ((promoType != ChessPiece.PieceType.PAWN) && (promoType != ChessPiece.PieceType.KING)) {
                        tempMoves.add(new ChessMove(myPosition, newPosition, promoType));
                    }
                }
            } else {
                tempMoves.add(new ChessMove(myPosition, newPosition, null));
            }
        }
        return tempMoves;
    }
}
