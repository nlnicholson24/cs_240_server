package model;

import java.util.Objects;

/**
 * Data on a person, including parents, spouse and descendant
 */
public class Person {

    public Person() {}

    /**
     * The unique string used to identify this Person
     */
    private String personID;
    /**
     * The username of the user associated with this person
     */
    private String descendant;
    /**
     * The first name of the person
     */
    private String firstName;
    /**
     * The last name of the person
     */
    private String lastName;
    /**
     * The gender (m or f) of the person
     */
    private String gender;
    /**
     * The ID of the person's father (possibly null)
     */
    private String father;
    /**
     * The ID of the person's mother (possibly null)
     */
    private String mother;
    /**
     * The ID of ther person's spouse (possibly null)
     */
    private String spouse;

    public Person(String personID, String descendant, String firstName,
                  String lastName, String gender, String father, String mother, String spouse) {
        this.personID = personID;
        this.descendant = descendant;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.father = father;
        this.mother = mother;
        this.spouse = spouse;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }

    public String getDescendant() {
        return descendant;
    }

    public void setDescendant(String descendant) {
        this.descendant = descendant;
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

    public String getFather() {
        return father;
    }

    public void setFather(String father) {
        this.father = father;
    }

    public String getMother() {
        return mother;
    }

    public void setMother(String mother) {
        this.mother = mother;
    }

    public String getSpouse() {
        return spouse;
    }

    public void setSpouse(String spouse) {
        this.spouse = spouse;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return Objects.equals(getPersonID(), person.getPersonID()) &&
                Objects.equals(getDescendant(), person.getDescendant()) &&
                Objects.equals(getFirstName(), person.getFirstName()) &&
                Objects.equals(getLastName(), person.getLastName()) &&
                Objects.equals(getGender(), person.getGender()) &&
                Objects.equals(getFather(), person.getFather()) &&
                Objects.equals(getMother(), person.getMother()) &&
                Objects.equals(getSpouse(), person.getSpouse());
    }
}

