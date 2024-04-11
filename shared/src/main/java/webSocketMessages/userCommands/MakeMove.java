package webSocketMessages.userCommands;

import chess.ChessMove;

public class MakeMove extends UserGameCommand{
    ChessMove move;

    public MakeMove(String authToken, int gameID, ChessMove move){
        super(authToken);
        setCommandType(CommandType.MAKE_MOVE);
        this.gameID = gameID;
        this.move = move;
    }
}
