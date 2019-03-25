package service;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.PersonDAO;
import model.Person;
import model.AuthToken;
import verify.AuthTokenVerify;

import java.sql.Connection;

/**
 * The service for the /person/[personID] API
 */
public class PersonIDService {

    public PersonIDService() {}

    /**
     * Returns the single PersonResult object with the specified ID
     * @return Returns the result body for the /person/[personID] API
     * @param token An AuthToken is required for this API
     * @param personID The ID of the person to return
     */
    public Person personID(String token, String personID) throws ServiceException {

        AuthToken authToken = AuthTokenVerify.verify(token);

        if(authToken != null)
        {
            Database db = new Database();
            try{
                Connection conn = db.openConnection();
                PersonDAO pdao = new PersonDAO(conn);
                Person person = pdao.find(personID);
                db.closeConnection(true);
                if (person !=null)
                {
                    if(authToken.getUserName().equals(person.getDescendant()))
                        return person;
                    else
                        throw new ServiceException("Person does not belong to this user");
                }
                else
                {
                    throw new ServiceException("Invalid personID");
                }
            }
            catch(DataAccessException e)
            {
                //e.printStackTrace();
                try {
                    db.closeConnection(false);
                }
                catch(DataAccessException s) {}
                throw new ServiceException(e.getMessage());
            }
        }
        else
        {
            throw new ServiceException("Authorization token is not valid.");
        }
    }
}
