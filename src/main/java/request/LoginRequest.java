package request;

import java.util.Objects;

/**
 * request body for the /user/login API
 */
public class LoginRequest {

    /**
     * The username of the user to be logged in
     */
    private String userName;
    /**
     * The user's attempted password
     */
    private String password;

    public LoginRequest(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoginRequest loginRequest = (LoginRequest) o;
        return Objects.equals(getUserName(), loginRequest.getUserName()) &&
                Objects.equals(getPassword(), loginRequest.getPassword());
    }
}
