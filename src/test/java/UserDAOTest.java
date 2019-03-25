import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import model.User;

import java.sql.Connection;

import org.junit.*;
import static org.junit.Assert.*;

public class UserDAOTest {
    Database db;
    User bestUser;
    User secondUser;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        bestUser = new User("nathan123","welcome2","themonkey@gmail.com",
                "Nathan","Nicholson","m","a437zg6");
        secondUser = new User("nathan456","welcome2","thecow@gmail.com",
                "Sweeney","Todd","f","45jemi35");
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
        User compareUser = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(bestUser);
            compareUser = uDao.find(bestUser.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareUser);
        assertEquals(bestUser,compareUser);
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
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(bestUser);
            uDao.insert(bestUser);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);
        User compareTest = bestUser;
        try {
            Connection conn = db.openConnection();
            UserDAO eDao = new UserDAO(conn);
            compareTest = eDao.find(bestUser.getUserName());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(compareTest);

    }

    /*
    Enters two distinct users into the database and attempts
    to find them. Verifies that null is not returned, and that
    the distinct users found in the table match the distinct
    users which were entered.
     */
    @Test
    public void findPass() throws Exception {
        User compareUser1 = null;
        User compareUser2 = null;
        db.clearTables();


        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(bestUser);
            uDao.insert(secondUser);
            compareUser1 = uDao.find(bestUser.getUserName());
            compareUser2 = uDao.find("nathan456");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareUser1);
        assertNotNull(compareUser2);
        assertEquals(bestUser,compareUser1);
        assertEquals(secondUser, compareUser2);
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
        User test = bestUser;
        db.clearTables();
        boolean noErrors = false;
        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(bestUser);
            test = uDao.find("Sweeney");
            db.closeConnection(true);
            noErrors = true;
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assert(noErrors);
        assertNull(test);
    }

    /*
    Inserts two users into the database. Verifies that both were
    inserted correctly. The second user is deleted, and then it is
    that the user with the second username was deleted, while the
    other was not. It is also verified that the deletion was not
    caused by any other exceptions occurring.
     */
    @Test
    public void deletePass() throws Exception {
        User test = null;
        User second_test = null;
        User secondUser = new User("nathan456","welcome2","thecow@gmail.com",
                "Sweeney","Todd","f","45jemi35");
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.insert(bestUser);
            uDao.insert(secondUser);
            test = uDao.find(bestUser.getUserName());
            second_test = uDao.find("nathan456");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(test);
        assertNotNull(second_test);
        assertEquals(secondUser,second_test);
        assertEquals(bestUser,test);

        boolean noErrors = false;

        try {
            Connection conn = db.openConnection();
            UserDAO uDao = new UserDAO(conn);
            uDao.delete("nathan456");
            second_test = uDao.find(bestUser.getUserName());
            test = uDao.find("nathan456");
            db.closeConnection(true);
            noErrors = true;
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(second_test);
        assertEquals(bestUser,second_test);
        assertNull(test);
        assert(noErrors);
    }

    /*
    Inserts a user into the database. Verifies that the
    user was inserted successfully. Deletes the user, verifies,
    that was deleted, but then attempts to insert the same
    user twice, causing an error, which rolls back the changes.
    Then verifies that the user is no longer deleted.
     */
    @Test
    public void deleteFail() throws Exception {
        db.clearTables();
        User first_test = null;
        User second_test = bestUser;

        try {
            Connection conn = db.openConnection();
            UserDAO uDAO = new UserDAO(conn);
            uDAO.insert(bestUser);
            first_test = uDAO.find(bestUser.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNotNull(first_test);
        assertEquals(first_test,bestUser);

        try {
            Connection conn = db.openConnection();
            UserDAO uDAO = new UserDAO(conn);
            uDAO.delete(bestUser.getUserName());
            second_test = uDAO.find(bestUser.getUserName());
            uDAO.insert(bestUser);
            uDAO.insert(bestUser);
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNull(second_test);

        try {
            Connection conn = db.openConnection();
            UserDAO userDAO = new UserDAO(conn);
            second_test = userDAO.find(bestUser.getUserName());
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNotNull(second_test);
        assertEquals(second_test,bestUser);
    }

    @Test
    public void clearPass() throws Exception {
        db.clearTables();
        User user = bestUser;
        User user2 = secondUser;
        try {
            Connection conn = db.openConnection();
            UserDAO udao = new UserDAO(conn);
            udao.insert(bestUser);
            udao.insert(secondUser);
            udao.clear();
            user = udao.find(bestUser.getUserName());
            user2 = udao.find(secondUser.getUserName());
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
        }
        assertNull(user);
        assertNull(user2);
    }

    @Test
    public void clearPass2() throws Exception {
        db.clearTables();
        boolean success = false;
        try {
            Connection conn = db.openConnection();
            UserDAO udao = new UserDAO(conn);
            udao.clear();
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
