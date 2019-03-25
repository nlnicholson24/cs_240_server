package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import model.Event;
import model.Person;

/**
 * The class for interacting with the Events table of the database
 */
public class EventDAO {
    private Connection conn;

    /**
     * The constructor for the object
     * @param conn The connection to be used
     */
    public EventDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts an event into the Events table of the database
     * @param event The event to be inserted
     * @return Returns <code>true</code> if the event was successfully inserted, <code>false</code> otherwise
     * @throws DataAccessException Throws the exception if the event is not inserted
     */
    public boolean insert(Event event) throws DataAccessException {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Events (EventID, Descendant, PersonID, Latitude, Longitude, " +
                "Country, City, EventType, Year) VALUES(?,?,?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, event.getEventID());
            stmt.setString(2, event.getDescendant());
            stmt.setString(3, event.getPersonID());
            stmt.setDouble(4, event.getLatitude());
            stmt.setDouble(5, event.getLongitude());
            stmt.setString(6, event.getCountry());
            stmt.setString(7, event.getCity());
            stmt.setString(8, event.getEventType());
            stmt.setInt(9, event.getYear());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            e.printStackTrace();
            throw new DataAccessException("Error encountered while inserting into the database");
        }

        return commit;
    }

    /**
     * Finds the event in the Events table of the database with the given ID
     * @param eventID The ID of the event which is to be found
     * @return Returns the event with the specified ID, if it is in the table, returns null otherwise
     * @throws DataAccessException Throws the exception if there is an error interacting with the database
     */
    public Event find(String eventID) throws DataAccessException {
        Event event = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE EventID = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, eventID);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                event = new Event(rs.getString("EventID"), rs.getString("Descendant"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                return event;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        return null;
    }

    /**
     * Deletes all Event data from the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Events;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while interacting with the database");
        }
    }

    /**
     * Returns all events for all family members of the specified user
     * @param descendant The username of the user to be identified
     * @return Returns an array of Events; all events of all family members of the user
     */
    public Event[] familyEvents(String descendant) throws DataAccessException
    {
        ArrayList<Event> events = new ArrayList<Event>();
        ResultSet rs = null;
        String sql = "SELECT * FROM Events WHERE Descendant = \'" + descendant + "\';";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            rs = stmt.executeQuery();
            while (rs.next() == true) {
                Event event = new Event(rs.getString("EventID"), rs.getString("Descendant"),
                        rs.getString("PersonID"), rs.getFloat("Latitude"), rs.getFloat("Longitude"),
                        rs.getString("Country"), rs.getString("City"), rs.getString("EventType"),
                        rs.getInt("Year"));
                events.add(event);
            }

            Event[] array = new Event[events.size()];
            array = events.toArray(array);
            return array;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding persons");
        }
    }

    /**
     * Deletes all events from the database associated with the given user
     * @param username The descendant of the events to be deleted
     * @throws DataAccessException If there is an error deleting
     */
    public void deleteFromUser(String username) throws DataAccessException {
        String sql = "DELETE FROM Events WHERE Descendant = \'" + username + "\';";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while interacting with the database");
        }
    }
}
