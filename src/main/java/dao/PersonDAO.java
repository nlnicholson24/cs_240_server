package dao;

import model.Person;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * The class for interacting with the Persons table of the database
 */
public class PersonDAO {
    private Connection conn;

    /**
     * A constructor function which sets up the connection to be used
     * @param conn The connection to be used
     */
    public PersonDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts the given person object into the Persons table of the database
     * @param person The person object to be inserted
     * @return Returns <code>true</code> if the object is inserted successfully, <code>false</code> otherwise
     * @throws DataAccessException Throws the exception if the person is not successfully inserted
     */
    public boolean insert(Person person) throws DataAccessException {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Persons (PersonID, Descendant, FirstName, LastName, Gender, " +
                "Father, Mother, Spouse) VALUES(?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, person.getPersonID());
            stmt.setString(2, person.getDescendant());
            stmt.setString(3, person.getFirstName());
            stmt.setString(4, person.getLastName());
            stmt.setString(5, person.getGender());
            stmt.setString(6, person.getFather());
            stmt.setString(7, person.getMother());
            stmt.setString(8, person.getSpouse());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }

        return commit;
    }

    /**
     * Find the person object in the database with the matching ID
     * @param personID The ID of the person object to be returned
     * @return Returns a PersonResult with the given ID one is in the database, returns null otherwise
     * @throws DataAccessException Throws the exception if there is an error interacting with the database
     */
    public Person find(String personID) throws DataAccessException {
        Person person = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE PersonID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                person = new Person(rs.getString("PersonID"), rs.getString("Descendant"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("Father"), rs.getString("Mother"), rs.getString("Spouse"));
                return person;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding person");
        }
        return null;
    }

    /**
     * Deletes all Person data from the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Persons;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while interacting with the database");
        }
    }

    /**
     * Deletes all persons from the database associated with the given user
     * @param username The descendant of the persons to be deleted
     * @throws DataAccessException If there is an error deleting from the database
     */
    public void deleteFromUser(String username) throws DataAccessException {
        String sql = "DELETE FROM Persons WHERE Descendant = \'" + username + "\';";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while interacting with the database");
        }
    }

    /**
     * Returns an array containing all family members of the user with the specified username
     * @param descendant The username of the user whose family will be returned
     * @return Returns an array of Persons- the family members of the user
     */
    public Person[] family(String descendant) throws DataAccessException
    {
        ArrayList<Person> persons = new ArrayList<Person>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Persons WHERE Descendant = \'" + descendant + "\';";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();
            while (rs.next() == true) {
                Person person = new Person(rs.getString("PersonID"), rs.getString("Descendant"),
                        rs.getString("FirstName"), rs.getString("LastName"), rs.getString("Gender"),
                        rs.getString("Father"), rs.getString("Mother"), rs.getString("Spouse"));
                persons.add(person);
            }

            Person[] array = new Person[persons.size()];
            array = persons.toArray(array);
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding persons");
        }
    }
}
