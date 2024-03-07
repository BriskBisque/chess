package dataAccess;

import chess.ChessGame;
import com.google.gson.Gson;
import model.GameData;
import model.GameResult;

import java.util.ArrayList;
import java.util.Collection;

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
            CREATE TABLE IF NOT EXISTS  games (
              `gameID` int NOT NULL AUTO_INCREMENT,
              `whiteUsername` TEXT DEFAULT NULL,
              `blackUsername` TEXT DEFAULT NULL,
              `gameName` TEXT DEFAULT NULL CHECK (`gameName` <> ''),
              `game` TEXT DEFAULT NULL,
              PRIMARY KEY (`gameID`)
            ) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci
            """
        };
        DatabaseManager.configureDatabase(createStatements);
    }
    @Override
    public void clear() throws DataAccessException {
        var statement = "TRUNCATE games";
        DatabaseManager.executeUpdate(statement);
    }

    @Override
    public GameData createGame(String gameName) throws DataAccessException {
        GameData game = new GameData(0, null, null, gameName, new ChessGame());
        var statement = "INSERT INTO games (whiteUsername, blackUsername, gameName, game) VALUES (?, ?, ?, ?)";
        String gameJson = new Gson().toJson(game.game());
        var id = DatabaseManager.executeUpdate(statement, game.whiteUsername(), game.blackUsername(), game.gameName(), gameJson);
        return new GameData(id, game.whiteUsername(), game.blackUsername(), game.gameName(), game.game());
    }

    @Override
    public GameData getGame(int gameID) throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games WHERE gameID=?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setInt(1, gameID);
                try (var rs = ps.executeQuery()) {
                    if (rs.next()) {
                        var gameJson = rs.getString("game");
                        ChessGame game = new Gson().fromJson(gameJson, ChessGame.class);
                        var whiteUsername = rs.getString("whiteUsername");
                        var blackUsername = rs.getString("blackUsername");
                        var gameName = rs.getString("gameName");
                        return new GameData(gameID, whiteUsername, blackUsername, gameName, game);
                    }
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
        return null;
    }

    @Override
    public Collection<GameResult> listGames() throws DataAccessException {
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "SELECT * FROM games";
            try (var ps = conn.prepareStatement(statement)) {
                try (var rs = ps.executeQuery()) {
                    Collection<GameResult> games = new ArrayList<>();
                    while (rs.next()) {
                        int gameID = rs.getInt("gameID");
                        String whiteUsername = rs.getString("whiteUsername");
                        String blackUsername = rs.getString("blackUsername");
                        String gameName = rs.getString("gameName");
                        games.add(new GameResult(gameID, whiteUsername, blackUsername, gameName));
                    }
                    return games;
                }
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    @Override
    public void updateGame(GameData game, String color) throws DataAccessException {
        String blankUsername;
        String username;
        if (color.equals("BLACK")) {
            blankUsername = "blackUsername";
            username = game.blackUsername();
        } else if (color.equals("WHITE")) {
            blankUsername = "whiteUsername";
            username = game.whiteUsername();
        } else {
            throw new DataAccessException("Bad color");
        }
        try (var conn = DatabaseManager.getConnection()) {
            var statement = "UPDATE games SET " + blankUsername + " = ? WHERE gameID = ?";
            try (var ps = conn.prepareStatement(statement)) {
                ps.setString(1, username);
                ps.setInt(2, game.gameID());
                int rowsAffected = ps.executeUpdate();
            }
        } catch (Exception e) {
            throw new DataAccessException(String.format("Unable to read data: %s", e.getMessage()));
        }
    }

    public void destroy() throws DataAccessException {
        var statement = "DROP TABLE games";
        DatabaseManager.executeUpdate(statement);
    }
}
