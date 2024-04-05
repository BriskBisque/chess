package webSocketMessages;

public class LoadGame extends ServerMessage{
    public String game;

    public LoadGame(String game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }
}
