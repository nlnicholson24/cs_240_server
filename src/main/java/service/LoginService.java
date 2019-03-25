package service;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import generator.RandomGenerator;
import model.AuthToken;
import model.User;
import request.LoginRequest;
import result.LoginResult;

import javax.xml.crypto.Data;
import java.sql.Connection;

/**
 * The service for the /user/login API
 */
public class LoginService {

    public LoginService() {}

    /**
     * Logs in the user and returns an AuthToken
     * @param request The request body for the /user/login API
     * @return Returns the result body for the /user/login API
     */
    public LoginResult login(LoginRequest request) throws ServiceException
    {
        LoginResult result = new LoginResult();

        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            UserDAO udao = new UserDAO(conn);
            User user = udao.find(request.getUserName());
            db.closeConnection(true);

            if (user == null) {
                throw new ServiceException("Username/password pair is not found in the database.");
            }
            else if (!(user.getPassword().equals(request.getPassword()))) {
                throw new ServiceException("Username/password pair is not found in the database.");
            }
            else {
                AuthToken token = RandomGenerator.randomAuth(request.getUserName());
                result = new LoginResult(token.getToken(),request.getUserName(),
                        user.getPersonID());
            }
        }
        catch(DataAccessException e)
        {
            //e.printStackTrace();
            try {
                db.closeConnection(false);
            }
            catch (DataAccessException s)
            {}
            throw new ServiceException(e.getMessage());
        }

        return result;
    }
}
