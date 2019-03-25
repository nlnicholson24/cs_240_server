package model;

import java.util.Objects;

/**
 * An authorization token, to be created when a user logs in
 */
public class AuthToken {

    /**
     * The unique string used to identify the token
     */
    private String token;
    /**
     * The userName of the user whom this token authorizes
     */
    private String userName;


    public AuthToken(String token, String userName) {
        this.token = token;
        this.userName = userName;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AuthToken authToken = (AuthToken) o;
        return Objects.equals(getToken(), authToken.getToken()) &&
                Objects.equals(getUserName(), authToken.getUserName());
    }
}
