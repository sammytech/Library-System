package model;

import Utilities.Utilities;
import exception.InvalidLoginException;
import exception.InvalidPrimaryKeyException;
import exception.PasswordMismatchException;
import impresario.IView;

import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

/**
 * Created by Sammytech on 3/5/17.
 */
public class WorkerHolder extends EntityBase implements IView {
    private static final String myTableName = "Worker";
    public WorkerHolder(){
        super(myTableName);
        persistentState = new Properties();
        persistentState.setProperty("FirstName", "Sammy");
        persistentState.setProperty("LastName", "Babs");
        persistentState.setProperty("Credentials", "administrator");

    }
    public WorkerHolder(Properties props) throws InvalidLoginException {
        super(myTableName);

        String idToQuery = props.getProperty("BannerId");

        String query = "SELECT * FROM " + myTableName + " WHERE (BannerId = " + idToQuery + ")";

        Vector allDataRetrieved =  getSelectQueryResult(query);

        // You must get one account at least
        if (allDataRetrieved != null)
        {
            int size = allDataRetrieved.size();

            // There should be EXACTLY one account. More than that is an error
            if (size != 1)
            {
                throw new InvalidLoginException(Utilities.getStringLang("invalid_login"));
            }
            else
            {
                // copy all the retrieved data into persistent state
                Properties retrievedCustomerData = (Properties)allDataRetrieved.elementAt(0);
                persistentState = new Properties();

                Enumeration allKeys = retrievedCustomerData.propertyNames();
                while (allKeys.hasMoreElements() == true)
                {
                    String nextKey = (String)allKeys.nextElement();
                    String nextValue = retrievedCustomerData.getProperty(nextKey);

                    if (nextValue != null)
                    {

                        persistentState.setProperty(nextKey, nextValue);

                    }
                }

            }
        }
        // If no account found for this user name, throw an exception
        else
        {
            throw new InvalidLoginException(Utilities.getStringLang("invalid_login"));
        }

        String password = props.getProperty("Password");

        String workerPassword = persistentState.getProperty("Password");
        String workerStatus = persistentState.getProperty("Status");
        if(workerStatus!=null && (workerStatus.toLowerCase().trim().equals("active") || workerStatus.toLowerCase().trim().equals("actif"))){
            if (workerPassword != null)
            {
                boolean passwordCheck = workerPassword.equals(password);
                if (passwordCheck == false)
                {
                    throw new InvalidLoginException(Utilities.getStringLang("invalid_login"));
                } else{
                    persistentState.remove("Password");
                }

            }
            else
            {
                throw new InvalidLoginException(Utilities.getStringLang("invalid_login"));
            }
        } else{
            throw new InvalidLoginException(Utilities.getStringLang("invalid_status"));
        }


    }

    @Override
    public void updateState(String key, Object value) {

    }

    @Override
    public Object getState(String key) {
        return persistentState.getProperty(key);
    }

    @Override
    public void stateChangeRequest(String key, Object value) {

    }

    @Override
    protected void initializeSchema(String tableName) {

    }


}
