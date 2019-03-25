package generator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import model.Event;
import model.Person;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Random;

/**
 * The class to randomly generate person objects
 */
public class PersonGenerator {

    /**
     * The user for which this personGenerator generates person objects
     */
    private String descendant;

    public PersonGenerator(String descendant) {
        this.descendant = descendant;
    }

    /**
     * Randomly generates a first name, and last name, and and Id for the person object
     * @param gender The gender
     * @return The person object
     */
    public Person randomPerson(String gender) {

        String firstName;
        if(gender.equals("m"))
            firstName = RandomGenerator.randomName("mnames.json");
        else
            firstName = RandomGenerator.randomName("fnames.json");
        String lastName = RandomGenerator.randomName("snames.json");
        String personID = "p"+RandomGenerator.randomString(10);

        Person person = new Person(personID, descendant, firstName, lastName, gender,
                null, null, null);

        return person;
    }
}
