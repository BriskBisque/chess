package service;

import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;

import java.util.UUID;

public class Service {
    static UserDAO dao;

    public Service(){
        dao = new MemoryUserDAO();
    }

    public String getUser(String username, String password){
        String databaseUser = dao.select(username);
        if (databaseUser == null){
            this.createUser(username, password);
            dao.insert();
            return this.createAuth(username);
        } else {
            return
        }
    }

    public void createUser(String username, String password){
        String authToken = UUID.randomUUID().toString();
        dao.insert()
    }

    public String createAuth(String username){

    }
}
