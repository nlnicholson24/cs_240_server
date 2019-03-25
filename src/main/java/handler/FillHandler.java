package handler;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import request.LoadRequest;
import request.LoginRequest;
import result.*;
import service.*;

@RestController
public class FillHandler {

    /**
     * Handles the fill API
     * @param username
     * @return
     */
    @PostMapping(path="fill/{username}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> fill(@PathVariable("username") String username)
    {
        FillService fillService = new FillService();
        try {
            FillResult fillResult = fillService.fill(username, null);
            return new ResponseEntity<FillResult>(fillResult, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }

    /**
     * Handles the fill API with a specified generation
     * @param username
     * @param generations
     * @return
     */
    @PostMapping(path="fill/{username}/{generations}", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> fillGenerations(@PathVariable("username") String username,
                                             @PathVariable("generations") int generations)
    {
        FillService fillService = new FillService();
        try {
            FillResult fillResult = fillService.fill(username, generations);
            return new ResponseEntity<FillResult>(fillResult, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }

    /**
     * Handles the clear API
     * @return
     */
    @PostMapping (path="clear", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> clear()
    {
        ClearService clearService = new ClearService();
        try {
            ClearResult clearResult = clearService.clear();
            return new ResponseEntity<ClearResult>(clearResult, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }

    /**
     * HAndles the load API
     * @param loadRequest
     * @return
     */
    @PostMapping (path="load", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> load(@RequestBody LoadRequest loadRequest)
    {
        LoadService loadService = new LoadService();
        try {
            LoadResult loadResult = loadService.load(loadRequest);
            return new ResponseEntity<LoadResult>(loadResult, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }
}
