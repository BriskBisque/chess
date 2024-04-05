package webSocketMessages;

public class Leave extends UserGameCommand{
    int gameID;

    public Leave(String authToken, int gameID){
        super(authToken);
        setCommandType(CommandType.LEAVE);
        this.gameID = gameID;
    }
}
