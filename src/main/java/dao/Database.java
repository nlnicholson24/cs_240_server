package dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class Database {
    private Connection conn;

    static {
        try {
            //This is how we set up the driver for our database
            final String driver = "org.sqlite.JDBC";
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    //Whenever we want to make a change to our database we will have to open a connection and use
    //Statements created by that connection to initiate transactions
    public Connection openConnection() throws DataAccessException {
        try {
            //The Structure for this Connection is driver:language:path
            //The pathing assumes you start in the root of your project unless given a non-relative path
            final String CONNECTION_URL = "jdbc:sqlite:familymap.sqlite";

            // Open a database connection to the file given in the path
            conn = DriverManager.getConnection(CONNECTION_URL);

            // Start a transaction
            conn.setAutoCommit(false);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to open connection to database");
        }

        return conn;
    }

    //When we are done manipulating the database it is important to close the connection. This will
    //End the transaction and allow us to either commit our changes to the database or rollback any
    //changes that were made before we encountered a potential error.

    //IMPORTANT: IF YOU FAIL TO CLOSE A CONNECTION AND TRY TO REOPEN THE DATABASE THIS WILL CAUSE THE
    //DATABASE TO LOCK. YOUR CODE MUST ALWAYS INCLUDE A CLOSURE OF THE DATABASE NO MATTER WHAT ERRORS
    //OR PROBLEMS YOU ENCOUNTER
    public void closeConnection(boolean commit) throws DataAccessException {
        try {
            if (commit) {
                //This will commit the changes to the database
                conn.commit();
            } else {
                //If we find out something went wrong, pass a false into closeConnection and this
                //will rollback any changes we made during this connection
                conn.rollback();
            }

            conn.close();
            conn = null;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Unable to close database connection");
        }
    }

    public void createTables() throws DataAccessException {

        openConnection();

        try (Statement stmt = conn.createStatement()){
            //First lets open our connection to our database.

            //We pull out a statement from the connection we just established
            //Statements are the basis for our transactions in SQL
            //Format this string to be exaclty like a sql create table command
            String sql = "CREATE TABLE IF NOT EXISTS Persons ("
                    +"PersonID varchar(255) not null unique, "
                    +"Descendant varchar(255) not null, "
                    +"FirstName varchar(255) not null, "
                    +"LastName varchar(255) not null, "
                    +"Gender varchar(1) not null, "
                    +"Father varchar(255), "
                    +"Mother varchar(255), "
                    +"Spouse varchar(255), "
                    +"PRIMARY KEY (PersonID)"
                    +");\n\n"
                    +"CREATE TABLE IF NOT EXISTS Users ("
                    +"Username varchar(255) not null unique, "
                    +"Password varchar(255) not null, "
                    +"Email varchar(255) not null, "
                    +"FirstName varchar(255) not null, "
                    +"LastName varchar(255) not null, "
                    +"Gender varchar(1) not null, "
                    +"PersonID varchar(255) not null unique, "
                    +"PRIMARY KEY (Username)"
                    +");\n\n"
                    +"CREATE TABLE IF NOT EXISTS Events("
                    +"EventID varchar(255) not null unique, "
                    +"Descendant varchar(255) not null, "
                    +"PersonID varchar(255) not null, "
                    +"Latitude double not null, "
                    +"Longitude double not null, "
                    +"Country varchar(255) not null, "
                    +"City varchar(255) not null, "
                    +"EventType varchar(255) not null,"
                    +"Year int not null, "
                    +"PRIMARY KEY (EventID) );\n\n"
                    +"CREATE TABLE IF NOT EXISTS AuthTokens ("
                    +"Token varchar(255) not null unique, "
                    +"Username varchar(255) not null, "
                    +"PRIMARY KEY (Token)"
                    +");";

            stmt.executeUpdate(sql);
            //if we got here without any problems we successfully created the table and can commit

        } /*catch (DataAccessException e) {
            //if our table creation caused an error, we can just not commit the changes that did happen
            closeConnection(false);
            throw e;
        }*/ catch (SQLException e) {
            //if our table creation caused an error, we can just not commit the changes that did happen
            e.printStackTrace();
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while creating tables");
        }

        closeConnection(true);
    }

    public void clearTables() throws DataAccessException
    {
        openConnection();

        try (Statement stmt = conn.createStatement()){
            String sql = "DELETE FROM Users; \n DELETE FROM Persons;"
            +"\n DELETE FROM AuthTokens; \n DELETE FROM Events;";
            stmt.executeUpdate(sql);
        } /*catch (DataAccessException e) {
            closeConnection(false);
            throw e;
        }*/ catch (SQLException e) {
            e.printStackTrace();
            closeConnection(false);
            throw new DataAccessException("SQL Error encountered while clearing tables");
        }


        closeConnection(true);
    }
}
