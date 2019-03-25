package handler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
/**
 * The main class
 */
public class Application {

    //private HttpServer server;
    //private RegisterHandler rHandler = new RegisterHandler();
    //private LoginHandler lHandler = new LoginHandler();
    //private ClearHandler cHandler = new ClearHandler();
    //private fillHandler fHandler = new FillHandler();
    //private String address;

    public static void main(String[] args) {
        SpringApplication.run(Application.class,args);
    }

}
