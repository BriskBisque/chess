import ui.Board;
import ui.Client;
import ui.Repl;

public class Main {
    public static void main(String[] args) {
        Repl repl = new Repl("localhost:8080");
        try {
            Repl.run();
//            Board.main(null);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
}