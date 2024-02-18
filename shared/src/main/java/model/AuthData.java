package model;

import java.util.Objects;

public class AuthData {
    String authToken;
    String username;

    public AuthData(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthData authData = (AuthData) o;
        return Objects.equals(getAuthToken(), authData.getAuthToken()) && Objects.equals(getUsername(), authData.getUsername());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthToken(), getUsername());
    }
}
