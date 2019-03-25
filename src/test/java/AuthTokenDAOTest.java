import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.AuthToken;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class AuthTokenDAOTest {
    Database db;
    AuthToken bestToken;
    AuthToken secondToken;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        bestToken = new AuthToken("token123" ,"user123");
        secondToken = new AuthToken("token456","user456");
        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    /*
    Tests bestUser, attempts to find bestUser in the table, and
    verifies that the bestUser was in fact entered into the table
    (i.e. compareUser is not null), and that all the information
    is correct.
     */

    @Test
    public void insertPass() throws Exception {
        AuthToken compareToken = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(bestToken);
            compareToken = aDao.find(bestToken.getToken());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            e.printStackTrace();
            db.closeConnection(false);
        }
        assertNotNull(compareToken);
        assertEquals(compareToken,bestToken);
    }

    /*
    Tries to insert the same user twice. The test should fail, because
    usernames should be unique. Also tests that no data is remains in
    the table after the test, because all entered information should be
    rolled back once the error is encountered.
     */
    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        db.clearTables();
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(bestToken);
            aDao.insert(bestToken);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);
        AuthToken compareToken = bestToken;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            compareToken = aDao.find(bestToken.getToken());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareToken);

    }

    /*
    Enters two distinct users into the database and attempts
    to find them. Verifies that null is not returned, and that
    the distinct users found in the table match the distinct
    users which were entered.
     */
    @Test
    public void findPass() throws Exception {
        AuthToken compareToken1 = null;
        AuthToken compareToken2 = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(bestToken);
            aDao.insert(secondToken);
            compareToken1 = aDao.find(bestToken.getToken());
            compareToken2 = aDao.find("token456");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareToken1);
        assertNotNull(compareToken2);
        assertEquals(bestToken,compareToken1);
        assertEquals(secondToken, compareToken2);
    }

    /*
    Tries to find a user by entering the first name
    instead of the user name. bestUser is inserted
    into the database, which has user name nathan123,
    but trying to find a user with name Nathan is
    attempted, and should return null. A boolean is also
    used to verify that the test user is not null
    simply due to a data access or SQL error.
     */
    @Test
    public void findFail() throws Exception {
        AuthToken test = bestToken;
        db.clearTables();
        boolean noErrors = false;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO aDao = new AuthTokenDAO(conn);
            aDao.insert(bestToken);
            test = aDao.find("notatoken");
            db.closeConnection(true);
            noErrors = true;
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assert(noErrors);
        assertNull(test);
    }

    @Test
    public void clearPass() throws Exception {
        db.clearTables();
        AuthToken token = bestToken;
        AuthToken token2 = secondToken;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO adao = new AuthTokenDAO(conn);
            adao.insert(bestToken);
            adao.insert(secondToken);
            adao.clear();
            token = adao.find(bestToken.getUserName());
            token2 = adao.find(secondToken.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
        }
        assertNull(token);
        assertNull(token2);
    }

    @Test
    public void clearPass2() throws Exception {
        db.clearTables();
        boolean success = false;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO adao = new AuthTokenDAO(conn);
            adao.clear();
            db.closeConnection(true);
            success = true;
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }

        assert(success);
    }

}
