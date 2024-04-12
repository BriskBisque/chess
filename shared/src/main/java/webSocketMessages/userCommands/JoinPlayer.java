package webSocketMessages.userCommands;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    ChessGame.TeamColor playerColor;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor playerColor){
        super(authToken);
        setCommandType(CommandType.JOIN_PLAYER);
        this.gameID = gameID;
        this.playerColor = playerColor;
    }

    public ChessGame.TeamColor getPlayerColor(){return this.playerColor;}
}
