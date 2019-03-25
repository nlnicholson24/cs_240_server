package request;

import java.util.Objects;

/**
 * request body for the /user/register API
 */
public class RegisterRequest {

    /**
     * The username of the user being created
     */
    private String userName;
    /**
     * The desired password of the user being created
     */
    private String password;
    /**
     * The desired user's email address
     */
    private String email;
    /**
     * The desired user's first name
     */
    private String firstName;
    /**
     * The desired user's last name
     */
    private String lastName;
    /**
     * The desired user's gender (m or f)
     */
    private String gender;

    public RegisterRequest(String userName, String password, String email, String firstName, String lastName, String gender) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() {
        return userName;
    }

    public void setUsername(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        RegisterRequest registerRequest = (RegisterRequest) o;
        return Objects.equals(getUsername(), registerRequest.getUsername()) &&
                Objects.equals(getPassword(), registerRequest.getPassword()) &&
                Objects.equals(getEmail(), registerRequest.getEmail()) &&
                Objects.equals(getFirstName(), registerRequest.getFirstName()) &&
                Objects.equals(getLastName(), registerRequest.getLastName()) &&
                Objects.equals(getGender(), registerRequest.getGender());
    }
}
