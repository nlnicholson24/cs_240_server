package service;

import generator.RandomGenerator;
import request.RegisterRequest;
import result.RegisterResult;
import dao.*;
import java.sql.*;
import model.*;

/**
 * The service for the /user/register API
 */
public class RegisterService {

    public RegisterService() {}

    /**
     * Creates a new user account, generates 4 generations of ancestor data for the new
     * user, logs the user in, and returns an auth token
     * @param request The request body for the /user/register API
     * @return Returns the result body for the /user/register API
     */
    public RegisterResult register(RegisterRequest request) throws ServiceException
    {
        String username = request.getUsername();
        String personID = "p" + RandomGenerator.randomString(10);
        boolean success = true;

        if(!request.getGender().equals("m") && !request.getGender().equals("f")) {
            throw new ServiceException("Gender must be \'m\' or \'f\'");
        }

        User user = new User(username,request.getPassword(),request.getEmail(),
                request.getFirstName(),request.getLastName(),request.getGender(),personID);
        AuthToken token = RandomGenerator.randomAuth(username);

        Database db = new Database();
        try
        {
            Connection conn = db.openConnection();
            UserDAO ud = new UserDAO(conn);
            if (ud.find(username) != null) {
                db.closeConnection(false);
                throw new ServiceException("This username is already in use.");
            }
            ud.insert(user);
            db.closeConnection(true);

            FillService fillService = new FillService();
            fillService.fill(username,4);
        }
        catch (DataAccessException e)
        {

            try {
                db.closeConnection(false);
            }
            catch(DataAccessException s)
            { }
            throw new ServiceException(e.getMessage());
        }

        RegisterResult result = new RegisterResult(token.getToken(),username,personID);
        return result;

    }
}
