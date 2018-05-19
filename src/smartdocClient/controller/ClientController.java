package smartdocClient.controller;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.Date;

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
	
	public String verifyLogin(String login,String password) throws RemoteException
	{
		return clientModel.verifyLogin(login, password);
	}
	
	public boolean createDoctor(String login, String password, String fname, String lname, String cpr, int phone, String email, LocalDate dob, String speciality, String type, String gender) throws RemoteException
	{
		return clientModel.createDoctor(login, password, fname,lname,cpr,phone, email, dob,speciality, type, gender);
		 
	}
	
	public boolean createPatient(String login, String password, String fname, String lname, String cpr, int phone, String email, LocalDate dob, String gender) throws RemoteException {
		return clientModel.createPatient(login, password, fname, lname, cpr, phone, email, dob, gender);
	}
}
