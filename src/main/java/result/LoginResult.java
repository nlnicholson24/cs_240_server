package result;

import java.util.Objects;

/**
 * The result body for the /user/login API
 */
public class LoginResult {

    /**
     * The AuthToken generated for the user
     */
    private String authToken;
    /**
     * The username of the user logging in
     */
    private String username;
    /**
     * The ID of the person associated with the user
     */
    private String personID;

    public LoginResult() {}

    public LoginResult(String authToken, String username, String personID) {
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
