import chess.*;

import dataAccess.*;
import server.Server;
import service.Service;

public class Main {
    public static void main(String[] args) throws DataAccessException {
        var piece = new ChessPiece(ChessGame.TeamColor.WHITE, ChessPiece.PieceType.PAWN);
        System.out.println("♕ 240 Chess Server: " + piece);
        Server server = new Server();
        server.run(0);
//        AuthDAO gameDAO = SQLAuthDAO.getInstance();
//        gameDAO.destroy();
    }
}