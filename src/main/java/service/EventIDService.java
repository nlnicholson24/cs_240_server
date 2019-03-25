package service;

import dao.DataAccessException;
import dao.Database;
import dao.EventDAO;
import dao.PersonDAO;
import model.Event;
import model.AuthToken;
import model.Person;
import verify.AuthTokenVerify;

import java.sql.Connection;

/**
 * service for the /event/[eventID] API
 */
public class EventIDService {

    public EventIDService() {}

    /**
     * Returns the single EventResult object with the specified ID
     * @return Returns the result body for the /event/[eventID] API
     * @param token An AuthToken is required for this API
     * @param eventID The ID of the event to be returned
     */
    public Event eventID(String token, String eventID) throws ServiceException {

        AuthToken authToken = AuthTokenVerify.verify(token);

        if(authToken != null)
        {
            Database db = new Database();
            try{
                Connection conn = db.openConnection();
                EventDAO edao = new EventDAO(conn);
                Event event = edao.find(eventID);

                db.closeConnection(true);
                if (event !=null)
                {
                    if(authToken.getUserName().equals(event.getDescendant()))
                        return event;
                    else
                        throw new ServiceException("Event does not belong to this user");
                }
                else
                {
                    throw new ServiceException("EventID not valid.");
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
            throw new ServiceException("Authorization token is invalid.");
        }
    }
}
