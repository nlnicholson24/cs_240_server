package service;

import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import dao.PersonDAO;
import model.AuthToken;
import model.Event;
import model.Person;
import result.EventResult;
import result.PersonResult;
import verify.AuthTokenVerify;

import java.sql.Connection;

/**
 * service for the /event API
 */
public class EventService {

    public EventService() {}

    /**
     * Returns ALL events for ALL family members of the current user. The current
     * user is determined from the provided auth token
     * @return Returns the result body for the /event API
     * @param token An AuthToken is required for this API, and the user is determined from this token
     */
    public EventResult event(String token) throws ServiceException {
        AuthToken auth = AuthTokenVerify.verify(token);
        EventResult result = new EventResult();
        if(auth != null)
        {
            String descendant = auth.getUserName();
            Database db = new Database();
            try{
                Connection conn = db.openConnection();
                EventDAO edao = new EventDAO(conn);
                Event[] events = edao.familyEvents(descendant);
                result.setData(events);
                db.closeConnection(true);
            }
            catch(DataAccessException e)
            {
                //e.printStackTrace();
                try {
                    db.closeConnection(false);
                }
                catch (DataAccessException s) {}
                throw new ServiceException(e.getMessage());
            }
        }
        else
            throw new ServiceException("Authorization token is not valid.");

        return result;
    }
}
