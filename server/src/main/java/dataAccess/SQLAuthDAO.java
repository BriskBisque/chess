package dataAccess;

import model.AuthData;

import java.util.UUID;

public class SQLAuthDAO implements AuthDAO{

    private static SQLAuthDAO instance;

    public static synchronized AuthDAO getInstance() throws DataAccessException {
        if (instance == null){
            instance = new SQLAuthDAO();
        }
        return instance;
    }

    public SQLAuthDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  auth (
              `username` varchar(255) NOT NULL,
              `authToken` varchar(255) NOT NULL,
              PRIMARY KEY (`authToken`),
              INDEX(`username`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }

    @Override
    public void insertAuth(AuthData authData) throws DataAccessException {
        var statement = "INSERT INTO auth (username, authToken) VALUES (?, ?)";
        var id = DatabaseManager.executeUpdate(statement, authData.username(), authData.authToken());
    }

    @Override
    public String getAuth(String username) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken FROM auth WHERE username=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("authToken");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    public boolean authExists(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT authToken FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return false;
    }

    @Override
    public void deleteAuth(String authToken) throws DataAccessException {
        if (authExists(authToken)) {
            var statement = "DELETE FROM auth WHERE authToken=?";
            DatabaseManager.executeUpdate(statement, authToken);
        } else {
            throw new DataAccessException("auth doesn't exist");
        }
    }

    @Override
    public AuthData createAuth(String username){
        String authToken = UUID.randomUUID().toString();
        return new AuthData(authToken, username);
    }

    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE auth";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public String getUser(String authToken) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT username FROM auth WHERE authToken=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, authToken);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        return rs.getString("username");
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }
}
