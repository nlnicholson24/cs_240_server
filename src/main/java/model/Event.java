package model;

/**
 * An event that happened in the life of the person
 */
public class Event {
    /**
     * The unique string used to identify this event
     */
    private String eventID;
    /**
     * The username of the user to whom this event belongs
     */
    private String descendant;
    /**
     * The ID of the Person associated with this event
     */
    private String personID;
    /**
     * The latitude at which this event occurred
     */
    private float latitude;
    /**
     * The longitutde at which this event occurred
     */
    private float longitude;
    /**
     * The country in which this event occurred
     */
    private String country;
    /**
     * The city in which this event occurred
     */
    private String city;
    /**
     * The type of event taking place (i.e. marriage, death, birth, etc.)
     */
    private String eventType;
    /**
     * The year in which the event took place
     */
    private int year;

    public Event(String eventID, String descendant, String personID, float latitude, float longitude,
                 String country, String city, String eventType, int year) {
        this.eventID = eventID;
        this.descendant = descendant;
        this.personID = personID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
    }

    public Event() {}

    
    /**
     * Get the EventResult ID
     */
    public String getEventID() {
        return eventID;
    }


    /**
     * Set the EventResult ID
     */
    public void setEventID(String eventID) {
        this.eventID = eventID;
    }


    /**
     * Get the user's name
     */
    public String getDescendant() {
        return descendant;
    }


    /**
     * Set the user's name
     */
    public void setDescendant(String username) {
        this.descendant = username;
    }


    /**
     * Get the PersonResult's ID
     */
    public String getPersonID() {
        return personID;
    }


    /**
     * Set the PersonResult's ID
     */
    public void setPersonID(String personID) {
        this.personID = personID;
    }


    /**
     * Get the Latitude
     */
    public double getLatitude() {
        return latitude;
    }


    /**
     * Set the Latitude
     */
    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }


    /**
     * Get the Longitude
     */
    public double getLongitude() {
        return longitude;
    }


    /**
     * Set the Longitude
     */
    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }


    /**
     * Get the Country
     */
    public String getCountry() {
        return country;
    }


    /**
     * Set the Country
     */
    public void setCountry(String country) {
        this.country = country;
    }


    /**
     * Get the City
     */
    public String getCity() {
        return city;
    }


    /**
     * Set the City
     */
    public void setCity(String city) {
        this.city = city;
    }


    /**
     * Get EventResult Type
     */
    public String getEventType() {
        return eventType;
    }


    /**
     * Set the EventResult Type
     */
    public void setEventType(String eventType) {
        this.eventType = eventType;
    }


    /**
     * Get the Year
     */
    public int getYear() {
        return year;
    }


    /**
     * Set the Year
     */
    public void setYear(int year) {
        this.year = year;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof Event) {
            Event oEvent = (Event) o;
            if (oEvent.getEventID().equals(getEventID()) &&
                    oEvent.getDescendant().equals(getDescendant()) &&
                    oEvent.getPersonID().equals(getPersonID()) &&
                    oEvent.getLatitude() == (getLatitude()) &&
                    oEvent.getLongitude() == (getLongitude()) &&
                    oEvent.getCountry().equals(getCountry()) &&
                    oEvent.getCity().equals(getCity()) &&
                    oEvent.getEventType().equals(getEventType()) &&
                    oEvent.getYear() == (getYear())) {
                return true;
            }
        }
        return false;
    }
}
