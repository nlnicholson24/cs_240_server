package dao;

import model.AuthToken;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * The object for interacting with the AuthToken table of the database.
 */
public class AuthTokenDAO {
    private Connection conn;

    /**
     * Constructor for the object
     * @param conn Takes in a connection to be used for the object
     */
    public AuthTokenDAO(Connection conn)
    {
        this.conn = conn;
    }

    /**
     * Inserts an AuthToken into the AuthTokens table of the database
     * @param auth The token to be inserted
     * @return Returns <code>true</code> if the token was successfully inserted; <code>false</code> otherwise
     * @throws DataAccessException Throws the exception if the token is not successfully inserted
     */
    public boolean insert(AuthToken auth) throws DataAccessException {
        boolean commit = true;
        //We can structure our string to be similar to a sql command, but if we insert question
        //marks we can change them later with help from the statement
        String sql = "INSERT INTO AuthTokens (Token, Username) VALUES(?,?)";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            //Using the statements built-in set(type) functions we can pick the question mark we want
            //to fill in and give it a proper value. The first argument corresponds to the first
            //question mark found in our sql String
            stmt.setString(1, auth.getToken());
            stmt.setString(2, auth.getUserName());

            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            commit = false;
            throw new DataAccessException("Error encountered while inserting into the database");
        }

        return commit;
    }

    /**
     * Finds a token in the database with the given token ID
     * @param token The ID of the token to be found within the database
     * @return Returns the token with the given ID if found in the database, returns null otherwise
     * @throws DataAccessException Throws exception if there is an error interacting with the database
     */
    public AuthToken find(String token) throws DataAccessException {
        AuthToken auth = null;
        ResultSet rs = null;
        String sql = "SELECT * FROM AuthTokens WHERE Token = ?;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, token);
            rs = stmt.executeQuery();
            if (rs.next() == true) {
                auth = new AuthToken(rs.getString("Token"), rs.getString("Username"));
                return auth;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding AuthToken");
        }
        return null;
    }

    /**
     * Deletes all AuthToken data from the database
     */
    public void clear() throws DataAccessException {
        String sql = "DELETE FROM AuthTokens;";
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while interacting with the database");
        }
    }
}
