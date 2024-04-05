package webSocketMessages;

import chess.ChessGame;

public class JoinPlayer extends UserGameCommand{
    ChessGame.TeamColor teamColor;

    public JoinPlayer(String authToken, int gameID, ChessGame.TeamColor teamColor){
        super(authToken);
        setCommandType(CommandType.JOIN_PLAYER);
        this.gameID = gameID;
        this.teamColor = teamColor;
    }
}
