package service;

import dataAccess.MemoryUserDAO;
import dataAccess.UserDAO;

public class Service {
    static UserDAO dao;

    public Service(){
        dao = new MemoryUserDAO();
    }

    public String getUser(String username){
        String databaseUser = dao.select(username);
        if (databaseUser == null){
            this.
        }
    }

    public void createUser(){

    }
}
