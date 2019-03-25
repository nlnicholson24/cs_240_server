package handler;

import model.Person;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import request.LoginRequest;
import request.RegisterRequest;
import result.ErrorResult;
import result.LoginResult;
import result.RegisterResult;
import service.LoginService;
import service.RegisterService;
import service.ServiceException;

@RestController
public class UserHandler {

    /**
     * HAnldes the register API
     * @param request
     * @return
     */
    @PostMapping(path="user/register", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> registerUser(@RequestBody RegisterRequest request) {
        RegisterService registerService = new RegisterService();
        try {
            RegisterResult result = registerService.register(request);
            return new ResponseEntity<RegisterResult>(result, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }

    /**
     * Handles the user/login API
     * @param request
     * @return
     */
    @PostMapping(path="user/login", produces = "application/json", consumes = "application/json")
    public ResponseEntity<?> login(@RequestBody LoginRequest request)
    {
        LoginService loginService = new LoginService();
        try {
            LoginResult loginResult = loginService.login(request);
            return new ResponseEntity<LoginResult>(loginResult, HttpStatus.OK);
        }
        catch(ServiceException e)
        {
            ErrorResult result = new ErrorResult(e.getError());
            return new ResponseEntity<ErrorResult>(result,HttpStatus.OK);
        }
    }
}
