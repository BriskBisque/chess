package dataAccess;

import model.UserData;

import java.util.ArrayList;
import java.util.Collection;

public class MemoryUserDAO {

    private Collection<String> data;

    public MemoryUserDAO(){
        data = new ArrayList<>();
    }

    void insertUser(UserData u) throws DataAccessException{

    }

    public String select(String input){
        for (String item: data){
            if (item.equals(input)){
                return item;
            }
        }
        return null;
    }

    public void insert(String input){

    }
}
