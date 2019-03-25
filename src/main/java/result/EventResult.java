package result;
import model.Event;

/**
 * The result body for the /event API
 */
public class EventResult {

    /**
     * An array of the all events that happened to all family members of the user
     */
    private Event[] data;

    public EventResult() {}

    public EventResult(Event[] data) {
        this.data = data;
    }

    public Event[] getData() {
        return data;
    }

    public void setData(Event[] data) {
        this.data = data;
    }
}
