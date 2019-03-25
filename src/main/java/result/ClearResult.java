package result;

/**
 * result body for the /clear API
 */
public class ClearResult {

    /**
     * A bool that tracks whether the database was cleared successfully
     */
    private String message;

    public ClearResult() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
