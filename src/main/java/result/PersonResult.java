package result;
import model.Person;

import java.util.Arrays;

/**
 * The result body for the /person API
 */
public class PersonResult {

    /**
     * An array of the Person objects of all family members of the user
     */
    private Person[] data;

    public PersonResult() {}

    public PersonResult(Person[] data) {
        this.data = data;
    }

    public Person[] getData() {
        return data;
    }

    public void setData(Person[] data) {
        this.data = data;
    }
}
