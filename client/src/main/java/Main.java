import ui.Board;
import ui.Client;
import ui.Repl;

import static java.lang.Character.getNumericValue;

public class Main {
    public static void main(String[] args) {
        try {
            Repl repl = new Repl("http://localhost:" + 8080);
            repl.run();
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
//        String input = "a1";
//        int letter = 9-(getNumericValue(input.charAt(0))-9);
//        System.out.println(letter);
//        int number = getNumericValue(input.charAt(1));
//        System.out.print(number);
    }
}