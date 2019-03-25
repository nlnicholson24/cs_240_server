import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import dao.PersonDAO;
import model.Event;
import model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class EventDAOTest {
    Database db;
    Event bestEvent;
    Event secondEvent;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        bestEvent = new Event("eventID","desc","pID",-2.2f,
                3.3f,"cambodia","utah","birth",1922);
        secondEvent = new Event("eventID2","desc","pID2",-2.4f,
                3.7f,"cambodia2","utah2","birth2",1952);
        db.createTables();
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }

    /*
    Tests bestUser, attempts to find bestUser in the table, and
    verifies that the bestPerson was in fact entered into the table
    (i.e. comparePerson is not null), and that all the information
    is correct. bestPerson has null for the father and mother, which
    should not cause an error.
     */
    @Test
    public void insertPass() throws Exception {
        Event compareEvent = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();

            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);
            compareEvent = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareEvent);
        assertEquals(bestEvent,compareEvent);
    }

    /*
    Tries to insert a user without a first name. The test
    should fail, as first names cannot be null. Tests that
    the correct exceptions were thrown on this error.
     */
    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        bestEvent.setEventID(null);

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);
        Event compareEvent = bestEvent;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            compareEvent = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        bestEvent.setEventID("eventID");
        assertNull(compareEvent);
    }

    /*
    Enters two distinct persons into the database and attempts
    to find them. Verifies that null is not returned, and that
    the distinct persons found in the table match the distinct
    persons which were entered.
     */
    @Test
    public void findPass() throws Exception {
        Event first_test = null;
        Event second_test = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);
            eDao.insert(secondEvent);
            first_test = eDao.find(bestEvent.getEventID());
            second_test = eDao.find(secondEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(first_test);
        assertNotNull(second_test);
        assertEquals(bestEvent,first_test);
        assertEquals(secondEvent, second_test);
    }

    /*
    Tries to find a person that has never been entered
    into the database. Verifies that no other errors
    occurred. Verifies that that searching for the imaginary
    person returns null.
    */
    @Test
    public void findFail() throws Exception {
        Event test = bestEvent;
        db.clearTables();
        boolean noErrors = false;
        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);
            test = eDao.find("notaperson");
            db.closeConnection(true);
            noErrors = true;
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assert(noErrors);
        assertNull(test);
    }

    /*
    Inserts bestUser into the database, and verifies
    that it was correctly inserted into the database.
    Then clear is called on the user table, and it is
    verified that bestUser's information is no longer
    contained in the database.
     */
    @Test
    public void clearPass() throws Exception {
        Event compareEvent = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            EventDAO eDao = new EventDAO(conn);
            eDao.insert(bestEvent);
            compareEvent = eDao.find(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(compareEvent);
        assertEquals(bestEvent,compareEvent);

        try {
            Connection connect = db.openConnection();
            EventDAO temp = new EventDAO(connect);
            temp.clear();
            compareEvent = temp.find(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(compareEvent);
    }

    /*
    Inserts two users into the database. Verifies that both were
    inserted correctly. The second user is deleted, and then it is
    that the user with the second username was deleted, while the
    other was not. It is also verified that the deletion was not
    caused by any other exceptions occurring.
     */
    @Test
    public void clearFail() throws Exception{
        db.clearTables();
        Event test = null;

        try {
            Connection conn = db.openConnection();
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.insert(bestEvent);
            test = eventDAO.find(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNotNull(test);
        assertEquals(bestEvent,test);

        try{
            Connection conn = db.openConnection();
            EventDAO eventDAO = new EventDAO(conn);
            eventDAO.clear();
            test = eventDAO.find(bestEvent.getEventID());
            eventDAO.insert(bestEvent);
            eventDAO.insert(bestEvent);
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNull(test);

        try {
            Connection conn = db.openConnection();
            EventDAO eventDAO = new EventDAO(conn);
            test = eventDAO.find(bestEvent.getEventID());
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNotNull(test);
        assertEquals(test,bestEvent);
    }

    @Test
    public void deleteFromUserPass () throws Exception {
        db.clearTables();
        Event event = null;
        try {
            Connection conn = db.openConnection();
            EventDAO edao = new EventDAO(conn);
            edao.insert(bestEvent);
            edao.deleteFromUser(bestEvent.getDescendant());
            event = edao.find(bestEvent.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(event);
    }

    @Test
    public void deleteFromUsersFail() throws Exception {
        db.clearTables();
        Event event = bestEvent;
        try {
            Connection conn = db.openConnection();
            EventDAO edao = new EventDAO(conn);
            edao.insert(bestEvent);
            edao.deleteFromUser("nottheusername");
            event = edao.find(bestEvent.getEventID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(event,bestEvent);
    }

    @Test
    public void familyPass() throws Exception {
        db.clearTables();
        Event[] events = new Event[2];
        try {
            Connection conn = db.openConnection();
            EventDAO edao = new EventDAO(conn);
            edao.insert(bestEvent);
            edao.insert(secondEvent);
            events = edao.familyEvents(bestEvent.getDescendant());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        if (!(events[0].equals(bestEvent)))
        {
            assertEquals(events[1],bestEvent);
            assertEquals(events[0],secondEvent);
        }
        else {
            assertEquals(events[0], bestEvent);
            assertEquals(events[1],secondEvent);
        }
    }

    @Test
    public void familyFail() throws Exception {
        db.clearTables();
        Event[] events = new Event[2];
        try {
            Connection conn = db.openConnection();
            EventDAO edao = new EventDAO(conn);
            edao.insert(bestEvent);
            edao.insert(secondEvent);
            events = edao.familyEvents("notAUser");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(events.length, 0);
    }
}
