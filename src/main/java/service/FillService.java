package service;

import dao.*;
import generator.EventGenerator;
import generator.PersonGenerator;
import generator.RandomGenerator;
import model.Event;
import model.Person;
import model.User;
import result.FillResult;

import java.sql.Connection;

/**
 * service for the /fill/[username]/[generations] API
 */
public class FillService {

    public FillService() {}

    /**
     * Populates the server's database with generated data for the specified user name.
     * The required "username" parameter must be a user already registered with the server. If there is
     * any data in the database already associated with the given user name, it is deleted. The
     * optional generations parameter lets the caller specify the number of generations of ancestors
     * to be generated, and must be a non-negative integer (the default is 4, which results in 31 new
     * persons each with associated events).
     * @param username The username of the user whose data will be filled
     * @param generations (Optional) The number of generations to be filled
     * @return Returns the result body for the /fill/[username]/[generations] API
     */
    public FillResult fill(String username, Integer generations) throws ServiceException
    {
        int gens;
        if (generations == null)
            gens = 4;
        else if (generations >= 0)
            gens = generations;
        else
            throw new ServiceException("Invalid number of generations.");

        User user = null;

        FillResult result = new FillResult();

        Database db = new Database();
        try {
            Connection conn = db.openConnection();
            UserDAO udao = new UserDAO(conn);
            user = udao.find(username);
            PersonDAO pdao = new PersonDAO(conn);
            EventDAO edao = new EventDAO(conn);
            pdao.deleteFromUser(username);
            edao.deleteFromUser(username);
            db.closeConnection(true);
            if (user == null)
                throw new ServiceException("User not found in database.");

            Person person = new Person(user.getPersonID(),user.getUserName(),user.getFirstName(),
                    user.getLastName(),user.getGender(),null,null,null);

            int randomBirthYear = 1950 + RandomGenerator.randomInt(70);
            EventGenerator egen = new EventGenerator();

            egen.randomEvent(person,randomBirthYear,"Birth");

            giveParents(person,0,gens,randomBirthYear);

            conn = db.openConnection();
            pdao = new PersonDAO(conn);
            pdao.insert(person);
            db.closeConnection(true);

            double personNumber = (Math.pow(2,gens+1)-1);
            double eventNumber = (3*personNumber-2);
            result.decideMessage(personNumber,eventNumber);
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

        return result;
    }

    /**
     * A recursive function to build up generations from a given person object
     * @param person
     * @param currentGen
     * @param gens
     * @param birthYear
     * @throws ServiceException
     */
    public void giveParents(Person person, int currentGen, int gens, int birthYear) throws ServiceException
    {
        if (currentGen < gens) {
            PersonGenerator pgen = new PersonGenerator(person.getDescendant());
            EventGenerator egen = new EventGenerator();

            Person mother = pgen.randomPerson("f");
            Person father = pgen.randomPerson("m");
            father.setLastName(person.getLastName());

            int randomMBirth = birthYear - 20 - RandomGenerator.randomInt(10);
            int randomFBirth = birthYear - 20 - RandomGenerator.randomInt(10);
            int randomMarriage = birthYear - RandomGenerator.randomInt(3);

            this.giveEvents(mother,randomMBirth);
            this.giveEvents(father,randomFBirth);

            egen.marry(father, mother, randomMarriage);

            this.giveParents(mother,currentGen+1,gens,randomMBirth);
            this.giveParents(father,currentGen+1,gens,randomFBirth);



            Database db = new Database();
            try {
                Connection conn = db.openConnection();
                PersonDAO pdao = new PersonDAO(conn);
                pdao.insert(mother);
                pdao.insert(father);
                db.closeConnection(true);
            }
            catch(DataAccessException e)
            {
                //e.printStackTrace();
                try {db.closeConnection(false);}
                catch(DataAccessException s)
                { }
                throw new ServiceException(e.getMessage());
            }

            person.setFather(father.getPersonID());
            person.setMother(mother.getPersonID());
        }


    }

    /**
     * Assigns semi-random birth and death events to a person
     * @param person
     * @param randomBirth
     * @throws ServiceException
     */
    public void giveEvents(Person person, int randomBirth) throws ServiceException
    {
        EventGenerator egen = new EventGenerator();
        int randomDeath = randomBirth + 55 + RandomGenerator.randomInt(45);

        egen.randomEvent(person,randomBirth,"Birth");
        egen.randomEvent(person, randomDeath, "Death");
    }
}
