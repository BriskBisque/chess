package webSocketMessages;

public class JoinObserver extends UserGameCommand{

    public JoinObserver(String authToken, int gameID){
        super(authToken);
        setCommandType(CommandType.JOIN_OBSERVER);
        this.gameID = gameID;
    }
}
