package server;

import java.util.Objects;

public class Response {

    int success;

    public Response(int success) {
        this.success = success;
    }

    public int getSuccess() {
        return success;
    }

    public void setSuccess(int success) {
        this.success = success;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Response response = (Response) o;
        return getSuccess() == response.getSuccess();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getSuccess());
    }
}
