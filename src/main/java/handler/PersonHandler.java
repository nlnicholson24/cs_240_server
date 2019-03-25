package handler;


import model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import result.ErrorResult;
import result.PersonResult;
import service.PersonIDService;
import service.PersonService;
import service.ServiceException;

@RestController
public class PersonHandler {

    /**
     * Handles the person/personID aPI
     * @param personID
     * @param token
     * @return
     */
    @GetMapping(path="person/{personID}", produces="application/json", consumes="application/json")
    public ResponseEntity<?> fetchPerson(@PathVariable("personID") String personID ,
                                         @RequestHeader("Authorization") String token)
    {
        PersonIDService service = new PersonIDService();
        try {
            Person result = service.personID(token, personID);
            return new ResponseEntity<Person>(result, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }

    /**
     * Handles the person API
     * @param token
     * @return
     */
    @GetMapping(path="person", produces="application/json", consumes="application/json")
    public ResponseEntity<?> personArray(@RequestHeader("Authorization") String token)
    {
        PersonService service = new PersonService();
        try {
            PersonResult result = service.person(token);
            return new ResponseEntity<PersonResult>(result, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }
}
