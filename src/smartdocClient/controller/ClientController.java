package smartdocClient.controller;

import java.rmi.RemoteException;

import smartdocClient.domain.mediator.ClientModel;
import smartdocClient.domain.mediator.ClientModelManager;
public class ClientController {

	private static  ClientModel clientModel;
	
	private static ClientController instance;
	
	private ClientController(ClientModel clientModel) {
		
			this.clientModel= new ClientModelManager();
	}
	
	public static ClientController getInstance()
	{
		if(instance == null)
		{
			 instance = new ClientController(clientModel);
		}
		
		return instance;
	}
	
	public boolean verifyLogin(String login,String password) throws RemoteException
	{
		return clientModel.verifyLogin(login, password);
	}
	
}