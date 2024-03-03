package dataAccess;

import model.GameData;
import model.ObserversData;

import java.sql.SQLException;
import java.util.Collection;

import static java.sql.Statement.RETURN_GENERATED_KEYS;
import static java.sql.Types.NULL;

public class SQLGameDAO implements GameDAO{

    private static SQLGameDAO instance;

    public static synchronized GameDAO getInstance() throws DataAccessException {
        if (instance == null){
            instance = new SQLGameDAO();
        }
        return instance;
    }

    public SQLGameDAO() throws DataAccessException {
        String[] createStatements = {
                """
            CREATE TABLE IF NOT EXISTS  game (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` TEXT DEFAULT NULL,
              `blackUsername` TEXT DEFAULT NULL,
              `gameName` varchar(256) NOT NULL,
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`),
              INDEX(gameName)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE game";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public void insertGame(GameData game) {

    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        return null;
    }

    @Override
    public Collection<GameData> listGames() {
        return null;
    }

    @Override
    public void updateGame(GameData game) throws DataAccessException {

    }

    @Override
    public GameData createGame(String gameName) {
        return null;
    }

    @Override
    public void addObserver(GameData game, String authToken) {

    }

    @Override
    public Collection<GameData> getGames() {
        return null;
    }

    @Override
    public Collection<ObserversData> getObserversData() {
        return null;
    }

//    private final String[] createStatements = {
//            """
//            CREATE TABLE IF NOT EXISTS  game (
//              `gameID` int NOT NULL AUTO_INCREMENT,
//              `whiteUsername` TEXT NOT NULL,
//              'blackUsername' TEXT NOT NULL,
//              'gameName' TEXT NOT NULL,
//              `game` TEXT DEFAULT NULL,
//              PRIMARY KEY (`gameID`),
//              INDEX(gameName)
//            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
//            """
//    };
//
//    static int executeUpdate(String statement, Object... params) throws DataAccessException {
//        try (var conn = DatabaseManager.getConnection()) {
//            try (var ps = conn.prepareStatement(statement, RETURN_GENERATED_KEYS)) {
//                for (var i = 0; i < params.length; i++) {
//                    var param = params[i];
//                    if (param instanceof String p) ps.setString(i + 1, p);
//                    else if (param instanceof Integer p) ps.setInt(i + 1, p);
//                    else if (param == null) ps.setNull(i + 1, NULL);
//                }
//                ps.executeUpdate();
//
//                var rs = ps.getGeneratedKeys();
//                if (rs.next()) {
//                    return rs.getInt(1);
//                }
//
//                return 0;
//            }
//        } catch (SQLException e) {
//            throw new DataAccessException(String.format("unable to update database: %s, %s", statement, e.getMessage()));
//        }
//    }
//
//    void configureDatabase() throws DataAccessException {
//        DatabaseManager.createDatabase();
//        try (var conn = DatabaseManager.getConnection()) {
//            for (var statement : createStatements) {
//                try (var preparedStatement = conn.prepareStatement(statement)) {
//                    preparedStatement.executeUpdate();
//                }
//            }
//        } catch (Exception ex) {
//            throw new DataAccessException(String.format("Unable to configure database: %s", ex.getMessage()));
//        }
//    }
}
