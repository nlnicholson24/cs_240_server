import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import dao.UserDAO;
import generator.RandomGenerator;
import model.AuthToken;
import model.Event;
import model.Person;
import model.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import request.LoadRequest;
import request.LoginRequest;
import request.RegisterRequest;
import result.*;
import service.*;

import java.sql.Connection;

import static junit.framework.TestCase.*;

public class ServiceTest {

    private Database db;
    private Person bestPerson;
    private Person secondPerson;
    private Event bestEvent;
    private Event secondEvent;
    private AuthToken bestToken;
    private AuthToken secondToken;
    private User bestUser;
    private RegisterRequest registerRequest;
    private LoadRequest loadRequest;
    private LoginRequest loginRequest;


    @Before
    public void setUp() throws Exception {
        db = new Database();
        db.createTables();

        bestPerson = new Person("a4z75nich","nathan123","Nathan",
                "Nicholson","m",null,null,"a23iiij");
        secondPerson = new Person("a34iiij","nathan123","Jackie",
                "Nicholson","f","moneya3a3","gem4e7z", "a4z75nich");

        bestEvent = new Event("eventID","nathan123","pID",-2.2f,
                3.3f,"cambodia","utah","birth",1922);
        secondEvent = new Event("eventID2","nathan123","pID2",-2.4f,
                3.7f,"cambodia2","utah2","birth2",1952);

        bestToken = new AuthToken("token123" ,"nathan123");
        secondToken = new AuthToken("token456","user456");

        bestUser = new User("nathan123","welcome2","themonkey@gmail.com",
                "Nathan","Nicholson","m","a437zg6");
        registerRequest = new RegisterRequest("nathan456","welcome2","thecow@gmail.com",
                "Sweeney","Todd", "m");

        Person[] persons = new Person[2];
        persons[0] = bestPerson;
        persons[1] = secondPerson;

        Event[] events = new Event[2];
        events[0] = bestEvent;
        events[1] = secondEvent;

        User[] users = new User[1];
        users[0] = bestUser;

        loadRequest = new LoadRequest(users,persons,events);
        loginRequest = new LoginRequest(registerRequest.getUsername(),registerRequest.getPassword());
    }

    @After
    public void tearDown() throws Exception {
        db.clearTables();
    }


    @Test
    public void registerPass() throws Exception {
        db.clearTables();
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(registerRequest);

        assertNotNull(result);
        assertNotNull(result.getAuthToken());
        assertNotNull(result.getPersonID());
        assertEquals(result.getUsername(),registerRequest.getUsername());
    }

    @Test
    public void registerFail() throws Exception {
        db.clearTables();
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(registerRequest);
        try {
            RegisterResult result2 = service.register(registerRequest);
            assertNotNull(null);
        }
        catch(ServiceException e)
        {
            assertNull(null);
        }
    }

    @Test
    public void ClearPass() throws Exception {
        db.clearTables();
        ClearService service = new ClearService();
        ClearResult result = service.clear();

        assertNotNull(result);
        assertNotNull(result.getMessage());
    }

    @Test
    public void clearPass2() throws Exception {
        db.clearTables();
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(registerRequest);
        ClearService service2 = new ClearService();
        ClearResult result2 = service2.clear();

        assertNotNull(result2);
        assertNotNull(result2.getMessage());
    }

    @Test
    public void loadPass() throws Exception {
        db.clearTables();
        LoadService service = new LoadService();
        LoadResult result = service.load(loadRequest);

        assertNotNull(result);
        User user = null;

        try {
            Connection conn = db.openConnection();
            UserDAO udao = new UserDAO(conn);
            user = udao.find(bestUser.getUserName());
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNotNull(user);
        assertEquals(user,bestUser);
    }

    @Test
    public void loadPass2() throws Exception {
        db.clearTables();
        RegisterService service = new RegisterService();
        RegisterResult result = service.register(registerRequest);

        LoadService service2 = new LoadService();
        LoadResult result2 = service2.load(loadRequest);

        User user = bestUser;
        try {
            Connection conn = db.openConnection();
            UserDAO udao = new UserDAO(conn);
            user = udao.find(registerRequest.getUsername());
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }

        assertNull(user);
    }

    /*@Test
    public void eventIDPass() throws Exception {
        db.clearTables();
        EventIDService service = new EventIDService();
        LoadService service2 = new LoadService();
        LoadResult result2 = service2.load(loadRequest);
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO adao = new AuthTokenDAO(conn);
            adao.insert(bestToken);
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }
        Event event = service.eventID(bestToken.getToken(),bestEvent.getEventID());
        assertNotNull(event);
        assertEquals(event,bestEvent);

    }*/

    @Test
    public void eventIDFail() throws Exception {
        db.clearTables();
        EventIDService service = new EventIDService();
        LoadService service2 = new LoadService();
        LoadResult result2 = service2.load(loadRequest);
        AuthToken token = RandomGenerator.randomAuth("notAUser");

        try {
            Event event = service.eventID(token.getToken(), bestEvent.getEventID());
            assertNotNull(null);
        }
        catch (ServiceException e)
        {
            assertNull(null);
        }
    }

    @Test
    public void personIDPass() throws Exception {
        db.clearTables();
        PersonIDService service = new PersonIDService();
        LoadService service2 = new LoadService();
        LoadResult result2 = service2.load(loadRequest);
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO adao = new AuthTokenDAO(conn);
            adao.insert(bestToken);
            db.closeConnection(true);
        }
        catch(DataAccessException e)
        {
            db.closeConnection(false);
        }
        Person person = service.personID(bestToken.getToken(),bestPerson.getPersonID());
        assertNotNull(person);
        assertEquals(person,bestPerson);

    }

    @Test
    public void personIDFail() throws Exception {
        db.clearTables();
        PersonIDService service = new PersonIDService();
        LoadService service2 = new LoadService();
        LoadResult result2 = service2.load(loadRequest);

        try {
            Person person = service.personID("notARealToken", bestPerson.getPersonID());
            assertNotNull(null);
        }
        catch (ServiceException e)
        {
            assertNull(null);
        }
    }

    @Test
    public void eventPass() throws Exception {
        db.clearTables();
        EventService service = new EventService();
        RegisterService service2 = new RegisterService();
        RegisterResult result2 = service2.register(registerRequest);
        EventResult result = service.event(result2.getAuthToken());

        assertNotNull(result);
        assertEquals(result.getData().length, 91);
    }

    @Test
    public void eventFail() throws Exception {
        db.clearTables();
        EventService service = new EventService();
        RegisterService service2 = new RegisterService();
        RegisterResult result2 = service2.register(registerRequest);
        try {
            EventResult result = service.event("notAnAuthToken");
            assertNotNull(null);
        }
        catch(ServiceException e)
        {
            assertNull(null);
        }
    }

    @Test
    public void personPass() throws Exception {
        db.clearTables();
        PersonService service = new PersonService();
        RegisterService service2 = new RegisterService();
        RegisterResult result2 = service2.register(registerRequest);
        PersonResult result = service.person(result2.getAuthToken());

        assertNotNull(result);
        assertEquals(result.getData().length, 31);
    }

    @Test
    public void personFail() throws Exception {
        db.clearTables();
        PersonService service = new PersonService();
        RegisterService service2 = new RegisterService();
        RegisterResult result2 = service2.register(registerRequest);

        try {
            PersonResult result = service.person("notAnAuthToken");
            assertNotNull(null);
        }
        catch(ServiceException e)
        {
            assertNull(null);
        }
    }

    @Test
    public void fillPass() throws Exception {
        db.clearTables();
        RegisterService rservice = new RegisterService();
        RegisterResult regResult = rservice.register(registerRequest);
        FillService service = new FillService();
        FillResult result = service.fill(registerRequest.getUsername(),1);
        PersonService service2 = new PersonService();
        PersonResult persons = service2.person(regResult.getAuthToken());

        assertNotNull(persons);
        assertEquals(persons.getData().length, 3);
    }

    @Test
    public void fillFail() throws Exception {
        db.clearTables();
        FillService service = new FillService();
        try {
            FillResult result = service.fill("noUserInputted", 3);
            assertNotNull(null);
        }
        catch(ServiceException e)
        {
            assertNull(null);
        }

    }

    @Test
    public void loginPass() throws Exception {
        db.clearTables();
        RegisterService rService = new RegisterService();
        rService.register(registerRequest);

        LoginService lService = new LoginService();
        LoginResult result = lService.login(loginRequest);

        assertNotNull(result);
        assertNotNull(result.getAuthToken());
        assertEquals(result.getUsername(),registerRequest.getUsername());
    }

    @Test
    public void loginFail() throws Exception {
        db.clearTables();
        RegisterService rService = new RegisterService();
        rService.register(registerRequest);

        LoginRequest badRequest = new LoginRequest(registerRequest.getUsername(),"wrongPassword");

        LoginService loginService = new LoginService();
        try {
            LoginResult result = loginService.login(badRequest);
            assertNotNull(null);
        }
        catch (ServiceException e)
        {
            assertNull(null);
        }
    }
}
