package generator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import model.Event;
import model.Person;
import service.ServiceException;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Connection;

/**
 * The class to generate random events
 */
public class EventGenerator {



    public EventGenerator() {

    }

    /**
     * Randomly generates an eventID, and randomly selects location data from
     * the given json example files. Inserts the event into the database.
     * @param person The person to whom the event applies
     * @param year The year the event takes place
     * @param type The type of event occuring
     * @throws ServiceException Is thrown if there is an error putting the event
     *                          into the database
     */
    public void randomEvent(Person person, int year, String type) throws ServiceException {
        String randomEventID = "e" + RandomGenerator.randomString(12);
        String descendant = person.getDescendant();
        String personID = person.getPersonID();

        JsonObject json = this.randomJson();
        float latitude = json.get("latitude").getAsFloat();
        float longitude = json.get("longitude").getAsFloat();
        String country = json.get("country").getAsString();
        String city = json.get("city").getAsString();

        Event event = new Event(randomEventID,descendant,personID,latitude,
                                longitude,country,city ,type,year);

        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            EventDAO edao = new EventDAO(conn);
            edao.insert(event);
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            //e.printStackTrace();
            try {db.closeConnection(false);}
            catch(DataAccessException s)
            {}

            throw new ServiceException(e.getMessage());
        }
    }

    /**
     * Randomly selects a json object from the list
     * @return
     */
    public JsonObject randomJson() {
        Gson gson = new Gson();
        try {
            String locations = new String(Files.readAllBytes(Paths.get("locations.json")));
            JsonObject jsonObject = gson.fromJson(locations,JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            int randomInteger = RandomGenerator.randomInt(jsonArray.size());
            JsonObject json = jsonArray.get(randomInteger).getAsJsonObject();
            return json;
        }
        catch(IOException e)
        {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Randomly creates a marriage event for the specified people inserted
     * @param husband The husband to be married
     * @param wife The wife to be married
     * @param year The year the marriage will take place
     * @throws ServiceException Throws the exception if the event is not entered
     *                          into the database
     */
    public void marry(Person husband, Person wife, int year) throws ServiceException
    {

        String descendant = husband.getDescendant();
        String personID1 = husband.getPersonID();
        String personID2 = wife.getPersonID();
        String randomEventID1 = "e"+RandomGenerator.randomString(12);
        String randomEventID2 = "e"+RandomGenerator.randomString(12);

        JsonObject json = this.randomJson();
        float latitude = json.get("latitude").getAsFloat();
        float longitude = json.get("longitude").getAsFloat();
        String country = json.get("country").getAsString();
        String city = json.get("city").getAsString();

        Event event1 = new Event(randomEventID1,descendant,personID1,latitude,longitude,
                country,city ,"Marriage",year);
        Event event2 = new Event(randomEventID2, descendant, personID2, latitude, longitude,
                country, city, "Marriage", year);

        husband.setSpouse(wife.getPersonID());
        wife.setSpouse(husband.getPersonID());

        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            EventDAO edao = new EventDAO(conn);
            edao.insert(event1);
            edao.insert(event2);
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            //e.printStackTrace();
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException s)
            { }

            throw new ServiceException(e.getMessage());
        }
    }
}
