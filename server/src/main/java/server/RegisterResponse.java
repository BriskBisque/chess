package server;

import spark.Response;

import java.util.Objects;

public class RegisterResponse extends Response {
    String authToken;

    public RegisterResponse(String authToken, boolean success, String message) {
        super();
        this.authToken = authToken;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterResponse that = (RegisterResponse) o;
        return Objects.equals(getAuthToken(), that.getAuthToken());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAuthToken());
    }
}
