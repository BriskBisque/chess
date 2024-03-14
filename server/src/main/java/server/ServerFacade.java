package server;

import com.google.gson.Gson;
import dataAccess.DataAccessException;
import exception.ResponseException;
import model.GameResult;
import model.ListGameResult;
import model.LoginData;
import model.Pet;
import model.UserData;

import java.io.*;
import java.net.*;
import java.util.Collection;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }


    public UserData registerUser(UserData user) throws DataAccessException {
        var path = "/user";
        return this.makeRequest("POST", path, user, UserData.class);
    }

    public UserData loginUser(LoginData loginData) throws DataAccessException {
        var path = "/session";
        return this.makeRequest("POST", path, loginData, UserData.class);
    }

    public void logoutUser(String authToken) throws DataAccessException {
        var path = "/session";
        this.makeRequest("DELETE", path, authToken, null);
    }

    public void deleteAll() throws DataAccessException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

    public Collection<GameResult> listGames(String authToken) throws DataAccessException {
        var path = "/game";
        return this.makeRequest("GET", path, authToken, ListGameResult.class).games();
    }

    public int createGame(String authToken, String gameName){
        var path = "/game";
        this.makeRequest("POST", path, )
    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws DataAccessException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (Exception ex) {
            throw new DataAccessException(ex.getMessage());
        }
    }


    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, DataAccessException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            throw new DataAccessException("failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }


    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
