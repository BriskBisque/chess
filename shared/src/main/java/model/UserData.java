package model;

import java.util.Objects;

public record UserData(String username, String password, String email) {
    public Object getUsername() {
        return this.username();
    }
}