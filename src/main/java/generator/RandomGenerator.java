package generator;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import service.ServiceException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.Provider;
import java.sql.Connection;
import java.util.Random;

/**
 * A helper class to generate certain random things
 */
public class RandomGenerator {

    public RandomGenerator() {}

    /**
     * Generates random strings
     * @param length The length of the random string
     * @return The string which was generated
     */
    public static String randomString(int length) {
        String chars = "abcdefghijklmnpqrstuvwxyz1234567890";
        StringBuilder rString = new StringBuilder();
        Random rnd = new Random();
        for (int i = 0; i < length; i++)
        {
            int index = (int)(rnd.nextFloat()*chars.length());
            rString.append(chars.charAt(index));
        }

        return rString.toString();
    }

    /**
     * Generates a nonnegative random integer
     * @param max The maximum possible integer to be generated
     * @return The integer to be returned
     */
    public static int randomInt(int max) {
        Random rnd = new Random();
        int result = (int) (rnd.nextFloat()*max);
        return result;
    }

    /**
     * Randomly generates and authtoken and inserts into the database
     * @param username The user to which the token belongs
     * @return Returns an authtoken object
     * @throws ServiceException
     */
    public static AuthToken randomAuth(String username) throws ServiceException {
        String token = "at" + randomString(8);
        AuthToken authToken = new AuthToken(token,username);
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO adao = new AuthTokenDAO(conn);
            adao.insert(authToken);
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
        return authToken;
    }

    /**
     * A function to randomly select a name from a specified json list
     * @param fileName The file from which the names will be selected
     * @return A string which is the name
     */
    public static String randomName(String fileName) {
        Gson gson = new Gson();
        try {
            String names = new String(Files.readAllBytes(Paths.get(fileName)));
            JsonObject jsonObject = gson.fromJson(names, JsonObject.class);
            JsonArray jsonArray = jsonObject.getAsJsonArray("data");
            int randomInteger = RandomGenerator.randomInt(jsonArray.size());
            String name = jsonArray.get(randomInteger).getAsString();
            return name;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
