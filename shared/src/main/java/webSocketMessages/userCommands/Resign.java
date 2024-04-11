package webSocketMessages.userCommands;

public class Resign extends UserGameCommand{

    public Resign(String authToken, int gameID){
        super(authToken);
        setCommandType(CommandType.RESIGN);
        this.gameID = gameID;
    }
}
