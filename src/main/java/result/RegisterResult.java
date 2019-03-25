package result;

import java.util.Objects;

/**
 * The result body for the /user/register API
 */
public class RegisterResult {

    /**
     * The AuthToken given to the user upon registering
     */
    private String authToken;
    /**
     * The username of the registered user
     */
    private String username;
    /**
     * The ID of the person associated with the registered user
     */
    private String personID;

    public RegisterResult() {}

    public RegisterResult(String authToken, String username, String personID) {
        this.authToken = authToken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthToken() {
        return authToken;
    }

    public void setAuthToken(String authToken) {
        this.authToken = authToken;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
