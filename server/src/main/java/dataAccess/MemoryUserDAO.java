package dataAccess;

import java.util.ArrayList;

public class MemoryUserDAO {

    private Collection<String> data = new ArrayList<>();

    public MemoryUserDAO(){

    }

    public String select(String input){
        for (String item: data){
            if (item.equals(input)){
                return item;
            }
        }
        return null;
    }
}
