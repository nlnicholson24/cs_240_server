package verify;

import dao.AuthTokenDAO;
import dao.DataAccessException;
import dao.Database;
import model.AuthToken;
import service.ServiceException;

import javax.sql.rowset.serial.SerialException;
import java.sql.Connection;
import java.util.ConcurrentModificationException;

public class AuthTokenVerify {

    public AuthTokenVerify() {}

    /**
     * Verifies that a string is a valid authoken string, and returns
     * the authoken object
     * @param token
     * @return
     * @throws ServiceException
     */
    public static AuthToken verify(String token) throws ServiceException
    {
        Database db = new Database();
        AuthToken auth = null;
        try {
            Connection conn = db.openConnection();
            AuthTokenDAO adao = new AuthTokenDAO(conn);
            auth = adao.find(token);
            db.closeConnection(true);
        }
        catch (DataAccessException e)
        {
            //e.printStackTrace();
            try {
                db.closeConnection(false);
            }
            catch(DataAccessException s) {}
            throw new ServiceException(e.getMessage());
        }

        return auth;
    }
}
