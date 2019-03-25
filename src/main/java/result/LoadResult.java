package result;

/**
 * The result body for the /load API
 */
public class LoadResult {

    /**
     * A boolean that tracks whether the information was successfully loaded to the database
     */
    private String message;

    public LoadResult() {}

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void decideMessage(Integer uNum, Integer eNum, Integer pNum)
    {
        this.message = "Successfully added " + uNum + " users, "
                   + pNum + " person, and " + eNum + " events to the database.";
    }
}
