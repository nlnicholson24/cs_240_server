package request;

import model.Event;
import model.Person;
import model.User;

import java.util.Arrays;

/**
 * request body for /load API
 */
public class LoadRequest {

    /**
     * The array of users to be loaded
     */
    private User[] users;
    /**
     * The array of Persons to be loaded
     */
    private Person[] persons;
    /**
     * The array of events to be loaded
     */
    private Event[] events;

    public LoadRequest(User[] users, Person[] persons, Event[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public User[] getUsers() {
        return users;
    }

    public void setUsers(User[] users) {
        this.users = users;
    }

    public Person[] getPersons() {
        return persons;
    }

    public void setPersons(Person[] persons) {
        this.persons = persons;
    }

    public Event[] getEvents() {
        return events;
    }

    public void setEvents(Event[] events) {
        this.events = events;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LoadRequest loadRequest = (LoadRequest) o;
        return Arrays.equals(getUsers(), loadRequest.getUsers()) &&
                Arrays.equals(getPersons(), loadRequest.getPersons()) &&
                Arrays.equals(getEvents(), loadRequest.getEvents());
    }
}
