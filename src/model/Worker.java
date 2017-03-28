package model;

import exception.InvalidPrimaryKeyException;
import impresario.IView;

import java.sql.SQLException;
import java.util.Enumeration;
import java.util.Properties;
import java.util.Vector;

public class Worker extends EntityBase implements IView {

	private static final String myTableName = "Worker";

	protected Properties dependencies;

	// GUI Components

	private String updateStatusMessage = "";
	private boolean successFlag = true;

	public Worker(String bannerId) throws InvalidPrimaryKeyException {
		super(myTableName);

		setDependencies();
		String query = "SELECT * FROM " + myTableName + " WHERE (bannerId = " + bannerId + ")";

		Vector<Properties> allDataRetrieved = getSelectQueryResult(query);

		// You must get one worker at least
		if (allDataRetrieved != null)
		{
			int size = allDataRetrieved.size();

			// There should be EXACTLY one worker. More than that is an error
			if (size != 1)
			{
				throw new InvalidPrimaryKeyException("Multiple workers matching id : "
					+ bannerId + " found.");
			}
			else
			{
				// copy all the retrieved data into persistent state
				Properties retrievedWorkerData = allDataRetrieved.elementAt(0);
				processNewWorkerHelper(retrievedWorkerData);


			}
		}
		// If no worker found for this user name, throw an exception
		else
		{
//		    workerErrorMessage="No worker matching id : " + workerId + " found.";
			throw new InvalidPrimaryKeyException("No worker matching id : "
				+ bannerId + " found.");
		}
	}


	// Can also be used to create a NEW Worker (if the system it is part of
	// allows for a new worker to be set up)
	//----------------------------------------------------------
	public Worker(Properties props)
	{
		super(myTableName);

		setDependencies();
		processNewWorkerHelper(props);
	}
	
	
	//----------------------------------------------------------
	public Object getState(String key)
	{
		if (key.equals("UpdateStatusMessage"))
			return updateStatusMessage;
		else if(key.equals("SuccessFlag")){
		    return successFlag;
        }
		return persistentState.getProperty(key);
	}

	//----------------------------------------------------------------
	public void stateChangeRequest(String key, Object value)
	{
		if(key.equals("ProcessNewWorker")){
		    processNewWorker((Properties) value);
        }
	    myRegistry.updateSubscribers(key, this);
	}
	
	private void setDependencies()
	{
		dependencies = new Properties();
		dependencies.setProperty("AddWorkerCancelled","ViewCancelled");
		dependencies.setProperty("ModifyWorkerCancelled","ViewCancelled");
        dependencies.setProperty("ProcessNewWorker","UpdateStatusMessage");
		myRegistry.setDependencies(dependencies);
	}

	
	public void update()
	{
		updateStateInDatabase();
	}
	
	//-----------------------------------------------------------------------------------
	private void updateStateInDatabase() 
	{
		try
		{
            successFlag = true;
			Properties whereClause = new Properties();
			whereClause.setProperty("workerId",
					persistentState.getProperty("workerId"));
			updatePersistentState(mySchema, persistentState, whereClause);
			updateStatusMessage = "Worker data for worker id : " + persistentState.getProperty("workerId") + " updated successfully in database!";
		}
		catch (SQLException ex)
		{
		    successFlag = false;
			updateStatusMessage = "Error in installing worker data in database!";
		}
		//DEBUG System.out.println("updateStateInDatabase " + updateStatusMessage);
	}

	/**
	 * This method is needed solely to enable the Worker information to be displayable in a table
	 *
	 */
	//--------------------------------------------------------------------------
	public Vector<String> getEntryListView()
	{
		Vector<String> v = new Vector<String>();
		v.addElement(persistentState.getProperty("BannerId"));
		v.addElement(persistentState.getProperty("FirstName"));
		v.addElement(persistentState.getProperty("LastName"));
		v.addElement(persistentState.getProperty("Phone"));
		v.addElement(persistentState.getProperty("E-mail"));
		v.addElement(persistentState.getProperty("Credentials"));
		v.addElement(persistentState.getProperty("DateOfLatestCredentialStatus"));
		v.addElement(persistentState.getProperty("DateOfHire"));
		v.addElement(persistentState.getProperty("Status"));

		return v;
	}

	public void processNewWorker(Properties props){
        processNewWorkerHelper(props);
        updateStateInDatabase();
    }

    private void processNewWorkerHelper(Properties props){
        persistentState = new Properties();
        Enumeration allKeys = props.propertyNames();
        while (allKeys.hasMoreElements())
        {
            String nextKey = (String)allKeys.nextElement();
            String nextValue = props.getProperty(nextKey);

            if (nextValue != null)
            {
            	System.out.println(nextKey + "  " + nextValue);
                persistentState.setProperty(nextKey, nextValue);
            }
        }
    }
	//-----------------------------------------------------------------------------------
	protected void initializeSchema(String tableName)
	{
		if (mySchema == null)
		{
			mySchema = getSchemaInfo(tableName);
		}
	}

	protected void createAndShowView()
	{
//		Scene currentScene = myViews.get("NewWorkerView");
//
//		if (currentScene == null)
//		{
//			// create our initial view
//			View newView = ViewFactory.createView("NewWorkerView", this);
//			currentScene = new Scene(newView);
//			myViews.put("NewWorkerView", currentScene);
//		}
//		swapToView(currentScene);
	}

	@Override
	public void updateState(String key, Object value) {
		stateChangeRequest(key, value);
	}
}