package service;

import dao.*;
import model.Event;
import model.Person;
import model.User;
import request.LoadRequest;
import result.LoadResult;

import java.sql.Connection;

/**
 * The service for the /load API
 */
public class LoadService {

    public LoadService() {

    }

    /**
     * Clears all data from the database, and then loads the posted user, person,
     * and event data into the database
     * @param request The request body for the /load API
     * @return The result body for the /load API
     */
    public LoadResult load(LoadRequest request) throws ServiceException {
        LoadResult result = new LoadResult();

        ClearService clearService = new ClearService();
        clearService.clear();

        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            UserDAO udao = new UserDAO(conn);
            PersonDAO pdao = new PersonDAO(conn);
            EventDAO edao = new EventDAO(conn);

            User[] users = request.getUsers();
            Person[] persons = request.getPersons();
            Event[] events = request.getEvents();

            for(int i = 0; i < users.length; i++) {
                udao.insert(users[i]);
            }
            for (int i = 0; i < persons.length; i++) {
                pdao.insert(persons[i]);
            }
            for (int i = 0; i < events.length; i++) {
                edao.insert(events[i]);
            }

            db.closeConnection(true);
            result.decideMessage(users.length, events.length,persons.length);
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

        return result;
    }
}
