package dataAccessTests;

import dataAccess.AuthDAO;
import dataAccess.DataAccessException;
import dataAccess.SQLAuthDAO;
import model.AuthData;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SQLAuthDAOTest {

    static private AuthDAO authDao;

    @AfterEach
    void clear() {
        assertDoesNotThrow(() -> authDao.clear());
    }

    @BeforeAll
    static void getInstance() {
        assertDoesNotThrow(() -> authDao = SQLAuthDAO.getInstance());
    }

    @Test
    void createAuthOne() {
        AuthData auth1 = assertDoesNotThrow(() -> authDao.createAuth("username"));
        assertNotNull(auth1.authToken());
    }

    @Test
    void createAuthTwo() {
        AuthData auth1 = assertDoesNotThrow(() -> authDao.createAuth("username"));
        AuthData auth2 = assertDoesNotThrow(() -> authDao.createAuth("username"));
        assertNotEquals(auth1.authToken(), auth2.authToken());
    }

    @Test
    void insertAuthOne() {
        AuthData auth1 = assertDoesNotThrow(() -> authDao.createAuth("username"));
        assertDoesNotThrow(() -> authDao.insertAuth(auth1));
    }

    @Test
    void insertAuthTwo() {
        assertThrows(DataAccessException.class, () -> {
            authDao.insertAuth(new AuthData(null, null));
        });
    }

    @Test
    void getAuthOne() {
        AuthData expected = assertDoesNotThrow(() -> authDao.createAuth("username"));
        assertDoesNotThrow(() -> authDao.insertAuth(expected));
        String actual = assertDoesNotThrow(() -> authDao.getAuth(expected.username()));
        assertEquals(expected.authToken(), actual);
    }

    @Test
    void getAuthTwo() {
        String actual = assertDoesNotThrow(() -> authDao.getAuth(null));
        assertNull(actual);
    }

    @Test
    void authExistsOne() {
        AuthData expected = assertDoesNotThrow(() -> authDao.createAuth("username"));
        assertDoesNotThrow(() -> authDao.insertAuth(expected));
        assertTrue(assertDoesNotThrow(() -> authDao.authExists(expected.authToken())));
    }

    @Test
    void authExistsTwo() {
        assertFalse(assertDoesNotThrow(() -> authDao.authExists(null)));
    }

    @Test
    void deleteAuthOne() {
        AuthData expected = assertDoesNotThrow(() -> authDao.createAuth("username"));
        assertDoesNotThrow(() -> authDao.insertAuth(expected));
        assertDoesNotThrow(() -> authDao.deleteAuth(expected.authToken()));
        assertFalse(assertDoesNotThrow(() -> authDao.authExists(expected.authToken())));
    }

    @Test
    void deleteAuthTwo() {
        assertThrows(DataAccessException.class, () -> {
            authDao.deleteAuth("heheheeh");
        });
    }

//    @Test
//    void updateAuthOne() {
//        AuthData initialAuth = assertDoesNotThrow(() -> authDao.createAuth("username"));
//        AuthData expected = new AuthData("your mom", "username");
//        assertDoesNotThrow(() -> authDao.insertAuth(initialAuth));
//        assertDoesNotThrow(() -> authDao.updateAuth(expected));
//
//        String actual = assertDoesNotThrow(() -> authDao.getAuth(expected.username()));
//        assertEquals(expected.authToken(), actual);
//    }
//
//    @Test
//    void updateAuthTwo() {
//        int rowsAffected = assertDoesNotThrow(() -> authDao.updateAuth(new AuthData("hehehehe", "username")));
//        assertEquals(0, rowsAffected);
//    }

    @Test
    void getUserOne() {
        AuthData expected = assertDoesNotThrow(() -> authDao.createAuth("username"));
        assertDoesNotThrow(() -> authDao.insertAuth(expected));
        String actual = assertDoesNotThrow(() -> authDao.getUser(expected.authToken()));

        assertEquals(expected.username(), actual);
    }

    @Test
    void getUserTwo() {
        String actual = assertDoesNotThrow(() -> authDao.getUser(null));

        assertNull(actual);
    }
}