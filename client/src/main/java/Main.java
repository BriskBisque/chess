import ui.Board;
import ui.Client;

public class Main {
    public static void main(String[] args) {
        Client client = new Client();
        try {
            client.preLoginUI();
//            Board.main(null);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}