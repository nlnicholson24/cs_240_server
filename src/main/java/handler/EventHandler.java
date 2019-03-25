package handler;


import model.Event;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import result.ErrorResult;
import result.EventResult;
import result.PersonResult;
import service.EventIDService;
import service.EventService;
import service.PersonService;
import service.ServiceException;

@RestController
public class EventHandler {

    /**
     * Handles the "event/{event(ID}" API
     * @param eventID
     * @param token
     * @return
     */
    @GetMapping(path="event/{eventID}", produces="application/json", consumes="application/json")
    public ResponseEntity<?> fetchEvent(@PathVariable("eventID") String eventID ,
                                         @RequestHeader("Authorization") String token)
    {
        EventIDService service = new EventIDService();
        try {
            Event result = service.eventID(token, eventID);
            return new ResponseEntity<Event>(result, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }

    /**
     * Handels the event API
     * @param token
     * @return
     */
    @GetMapping(path="event", produces="application/json", consumes="application/json")
    public ResponseEntity<?> personArray(@RequestHeader("Authorization") String token)
    {
        EventService service = new EventService();
        try {
            EventResult result = service.event(token);
            return new ResponseEntity<EventResult>(result, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }
}
