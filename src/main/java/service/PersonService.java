package service;

import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import result.PersonResult;
import model.AuthToken;
import verify.AuthTokenVerify;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

/**
 * The service for the /person API
 */
public class PersonService {

    public PersonService() {}

    /**
     * Returns ALL family members of the current user. The current user is
     * determined from the provided auth token
     * @return Returns the result body for the /person API
     * @param token An AuthToken is required for this API. The AuthToken determines the user whose family will be returned
     */
    public PersonResult person(String token) throws ServiceException {
        AuthToken auth = AuthTokenVerify.verify(token);
        PersonResult result = new PersonResult();
        if(auth != null)
        {
            String descendant = auth.getUserName();
            Database db = new Database();
            try{
                Connection conn = db.openConnection();
                PersonDAO pdao = new PersonDAO(conn);
                Person[] persons = pdao.family(descendant);
                result.setData(persons);
                db.closeConnection(true);
            }
            catch(DataAccessException e)
            {
                //e.printStackTrace();
                try {
                    db.closeConnection(false);
                }
                catch(DataAccessException s)
                { }
                throw new ServiceException(e.getMessage());
            }
        }
        else
            throw new ServiceException("Authorization token is not valid.");
        return result;
    }
}
