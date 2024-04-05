package webSocketMessages;

public class Resign extends UserGameCommand{
    int gameID;

    public Resign(String authToken, int gameID){
        super(authToken);
        setCommandType(CommandType.RESIGN);
        this.gameID = gameID;
    }
}
