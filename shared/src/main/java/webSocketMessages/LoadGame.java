package webSocketMessages;

import java.util.Objects;

public class LoadGame extends ServerMessage{
    public String game;

    public LoadGame(String game){
        super(ServerMessageType.LOAD_GAME);
        this.game = game;
    }

    @Override
    public String toString() {
        return "LoadGame{" +
                "game='" + game + '\'' +
                ", serverMessageType=" + serverMessageType +
                '}';
    }
}
