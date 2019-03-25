package dao;

import model.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * The object for interacting with the Users table of the database
 */
public class UserDAO {
    private Connection conn;

    /**
     * A constructor function which also sets up the connection
     * @param conn The connection to be used
     */
    public UserDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts the given user into the Users table of the database
     * @param user The user to be inserted in the table
     * @return Returns <code>true</code> if the user is inserted successfully, <code>false</code> otherwise
     * @throws DataAccessException Throws the exception if the user is not inserted into the table
     */
    public boolean insert(User user) throws DataAccessException {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO Users (Username, Password, Email, FirstName, LastName, " +
                "Gender, PersonID) VALUES(?,?,?,?,?,?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, user.getUserName());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getEmail());
            stmt.setString(4, user.getFirstName());
            stmt.setString(5, user.getLastName());
            stmt.setString(6, user.getGender());
            stmt.setString(7, user.getPersonID());

            stmt.executeUpdate();
        } catch (SQLException e) {
            commit = false;
            e.printStackTrace();
            throw new DataAccessException("Invalid data entered.");
        }

        return commit;
    }

    /**
     * Finds the user with the specified username in the databse
     * @param username The desired username of the user to be found within the database
     * @return Returns the User with the matching username if one is found in the database, returns null otherwise
     * @throws DataAccessException Throws the exception if an error occurs while interacting with the database
     */
    public User find(String username) throws DataAccessException {
        User user = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM Users WHERE Username = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                user = new User(rs.getString("Username"), rs.getString("Password"),
                        rs.getString("Email"), rs.getString("FirstName"), rs.getString("LastName"),
                        rs.getString("Gender"), rs.getString("PersonID"));
                return user;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding user");
        }
        return null;
    }

    /**
     * Deletes all User data from the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM Users;";
        try(PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.executeUpdate();
        }
        catch(SQLException e){
            throw new DataAccessException("Error encountered while accessing database");
        }
    }

    /**
     * Deletes all data in the database associated with the given username
     * @param username The username of the user to be deleted
     * @throws DataAccessException Throws the exception if there is an error deleting from the database
     */
    public void delete(String username) throws DataAccessException
    {
        String sql = "DELETE FROM Users WHERE Username = \'" + username + "\';";
        try(PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
        catch(SQLException e) {
            throw new DataAccessException("Error encountered while deleting from database");
        }
    }
}
