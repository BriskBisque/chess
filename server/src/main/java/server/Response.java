package server;

import java.util.Objects;

public class Response {

    boolean success;
    String message;

    public Response(boolean success, String message) {
        this.success = success;
        this.message = message;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return isSuccess() == response.isSuccess() && Objects.equals(getMessage(), response.getMessage());
    }

    @Override
    public int hashCode() {
        return Objects.hash(isSuccess(), getMessage());
    }
}
