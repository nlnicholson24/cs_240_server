package service;

import model.Event;
import model.Person;
import result.ClearResult;
import dao.*;

import java.sql.Connection;

/**
 * service for the /clear API
 */
public class ClearService {

    public ClearService() {}

    /**
     * Deletes ALL data from the database, including user accounts, auth tokens,
     * and generated persona and event data
     * @return Returns the result body for the /clear API
     */
    public ClearResult clear() throws ServiceException {
        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            PersonDAO pdao = new PersonDAO(conn);
            UserDAO udao = new UserDAO(conn);
            AuthTokenDAO adao = new AuthTokenDAO(conn);
            EventDAO edao = new EventDAO(conn);
            pdao.clear();
            udao.clear();
            adao.clear();
            edao.clear();
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            //e.printStackTrace();
            try {
                db.closeConnection(false);
            }
            catch(DataAccessException s)
            {}
            throw new ServiceException(e.getMessage());
        }
        ClearResult clearResult = new ClearResult();
        clearResult.setMessage("Database was cleared successfully.");
        return clearResult;
    }
}
