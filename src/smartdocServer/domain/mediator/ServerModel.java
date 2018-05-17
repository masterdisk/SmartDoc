package smartdocServer.domain.mediator;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.Date;

import utility.observer.RemoteSubject;

public interface ServerModel extends Remote, RemoteSubject<String> {
	

	public boolean verifyLogin(String login, String password) throws RemoteException;
	public boolean createDoctor(String login, String password, String fname, String lname, int cpr, int phone, String email, Date dob, String speciality, String type, String gender) throws RemoteException;

}
