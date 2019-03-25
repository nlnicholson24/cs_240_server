package result;

/**
 * The result body for the /fill/[username]/[generations] API
 */
public class FillResult {

    /**
     * A string that tracks whether the generations were populated successfully
     */
    private String message;

    public FillResult() {}

    public FillResult(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void decideMessage(double pNumber, double eNumber) {
        int personNumber = (int) pNumber;
        int eventNumber = (int) eNumber;
            this.message = "Successfully added " + personNumber + " persons and "
                + eventNumber + " events to the database.";
    }
}
