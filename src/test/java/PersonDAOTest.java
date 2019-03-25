import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import dao.UserDAO;
import model.Person;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.sql.Connection;

import static org.junit.Assert.*;

public class PersonDAOTest {
    Database db;
    Person bestPerson;
    Person secondPerson;

    @Before
    public void setUp() throws Exception {
        db = new Database();
        bestPerson = new Person("a4z75nich","nathan123","Nathan",
                "Nicholson","m",null,null,"a23iiij");
        secondPerson = new Person("a34iiij","nathan123","Jackie",
                "Nicholson","f","moneya3a3","gem4e7z", "a4z75nich");
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
        Person comparePerson = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();

            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(bestPerson);
            comparePerson = pDao.find(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(comparePerson);
        assertEquals(bestPerson,comparePerson);
    }

    /*
    Tries to insert a user without a first name. The test
    should fail, as first names cannot be null. Tests that
    the correct exceptions were thrown on this error.
     */
    @Test
    public void insertFail() throws Exception {
        boolean didItWork = true;
        bestPerson.setFirstName(null);

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(bestPerson);
            db.closeConnection(didItWork);
        } catch (DataAccessException e) {
            db.closeConnection(false);
            didItWork = false;
        }
        assertFalse(didItWork);
        Person comparePerson = bestPerson;
        try {
            Connection conn = db.openConnection();
            PersonDAO eDao = new PersonDAO(conn);
            comparePerson = eDao.find(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        bestPerson.setFirstName("Nathan");
        assertNull(comparePerson);
    }

    /*
    Enters two distinct persons into the database and attempts
    to find them. Verifies that null is not returned, and that
    the distinct persons found in the table match the distinct
    persons which were entered.
     */
    @Test
    public void findPass() throws Exception {
        Person first_test = null;
        Person second_test = null;
        db.clearTables();
        Person secondPerson = new Person("a34iiij","john58","Johnathan",
                "Thomas","f","moneya3a3","gem4e7z", null);

        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(bestPerson);
            pDao.insert(secondPerson);
            first_test = pDao.find(bestPerson.getPersonID());
            second_test = pDao.find(secondPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(first_test);
        assertNotNull(second_test);
        assertEquals(bestPerson,first_test);
        assertEquals(secondPerson, second_test);
    }

    /*
    Tries to find a person that has never been entered
    into the database. Verifies that no other errors
    occurred. Verifies that that searching for the imaginary
    person returns null.
    */
    @Test
    public void findFail() throws Exception {
        Person test = bestPerson;
        db.clearTables();
        boolean noErrors = false;
        try {
            Connection conn = db.openConnection();
            PersonDAO pDao = new PersonDAO(conn);
            pDao.insert(bestPerson);
            test = pDao.find("notaperson");
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
        Person comparePerson = null;
        db.clearTables();

        try {
            Connection conn = db.openConnection();
            PersonDAO uDao = new PersonDAO(conn);
            uDao.insert(bestPerson);
            comparePerson = uDao.find(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNotNull(comparePerson);
        assertEquals(bestPerson,comparePerson);

        try {
            Connection connect = db.openConnection();
            PersonDAO temp = new PersonDAO(connect);
            temp.clear();
            comparePerson = temp.find(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }

        assertNull(comparePerson);
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
        Person test = null;

        try {
            Connection conn = db.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            personDAO.insert(bestPerson);
            test = personDAO.find(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNotNull(test);
        assertEquals(bestPerson,test);

        try{
            Connection conn = db.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            personDAO.clear();
            test = personDAO.find(bestPerson.getPersonID());
            personDAO.insert(bestPerson);
            personDAO.insert(bestPerson);
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNull(test);

        try {
            Connection conn = db.openConnection();
            PersonDAO personDAO = new PersonDAO(conn);
            test = personDAO.find(bestPerson.getPersonID());
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNotNull(test);
        assertEquals(test,bestPerson);
    }

    @Test
    public void deleteFromUserPass () throws Exception {
        db.clearTables();
        Person person = null;
        try {
            Connection conn = db.openConnection();
            PersonDAO pdao = new PersonDAO(conn);
            pdao.insert(bestPerson);
            pdao.deleteFromUser(bestPerson.getDescendant());
            person = pdao.find(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertNull(person);
    }

    @Test
    public void deleteFromUsersFail() throws Exception {
        db.clearTables();
        Person person = bestPerson;
        try {
            Connection conn = db.openConnection();
            PersonDAO pdao = new PersonDAO(conn);
            pdao.insert(bestPerson);
            pdao.deleteFromUser("nottheusername");
            person = pdao.find(bestPerson.getPersonID());
            db.closeConnection(true);
        } catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(person,bestPerson);
    }

    @Test
    public void familyPass() throws Exception {
        db.clearTables();
        Person[] persons = new Person[2];
        try {
            Connection conn = db.openConnection();
            PersonDAO pdao = new PersonDAO(conn);
            pdao.insert(bestPerson);
            pdao.insert(secondPerson);
            persons = pdao.family(bestPerson.getDescendant());
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        if (!(persons[0].equals(bestPerson)))
        {
            assertEquals(persons[1],bestPerson);
            assertEquals(persons[0],secondPerson);
        }
        else {
            assertEquals(persons[0], bestPerson);
            assertEquals(persons[1],secondPerson);
        }
    }

    @Test
    public void familyFail() throws Exception {
        db.clearTables();
        Person[] persons = new Person[2];
        try {
            Connection conn = db.openConnection();
            PersonDAO pdao = new PersonDAO(conn);
            pdao.insert(bestPerson);
            pdao.insert(secondPerson);
            persons = pdao.family("notAUser");
            db.closeConnection(true);
        }
        catch (DataAccessException e) {
            db.closeConnection(false);
        }
        assertEquals(persons.length, 0);
    }
}
