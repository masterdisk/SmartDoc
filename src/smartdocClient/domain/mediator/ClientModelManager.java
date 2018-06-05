package smartdocClient.domain.mediator;

import java.io.Serializable;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Date;
import java.util.Observable;

import smartdocServer.domain.mediator.ServerModel;
import smartdocServer.domain.mediator.ServerModelManager;
import smartdocServer.domain.model.Doctor;
import smartdocServer.domain.model.DoctorList;
import smartdocServer.domain.model.Patient;
import smartdocServer.domain.model.PatientList;
import smartdocServer.domain.model.PatientPrescription;
import utility.observer.RemoteSubject;

public class ClientModelManager  implements ClientModel {

	private ServerModel server;
	private static Doctor doctor;
	private static Patient patient;

	public ClientModelManager() {

		try {
			server = (ServerModel) Naming.lookup("rmi://localhost:1099/smartdocServer");
			UnicastRemoteObject.exportObject(this, 0);
			
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NotBoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	

	public String verifyLogin(String login, String password) {
		try {
			return server.verifyLogin(login, password);
		} catch (RemoteException e) {

			e.printStackTrace();
		}
		return "0";
	}

	@Override
	public boolean createDoctor(String login, String password, String fname, String lname, String cpr, int phone,
			String email, LocalDate dob, String speciality, String type, String gender) {

		try {
			return server.createDoctor(login, password, fname, lname, cpr, phone, email, dob, speciality, type, gender);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public boolean createPatient(String login, String password, String fname, String lname, String cpr, int phone,
			String email, LocalDate dob, String gender) {

		try {
			return server.createPatient(login, password, fname, lname, cpr, phone, email, dob, gender);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

	@Override
	public String getAccountAndType(String cpr) {
		String type = "0";
		try {
			type = server.getType(cpr);
			System.out.println("TYPE:"+type);
			Object object = server.getAccount(cpr, type);
			saveObjectToModel(object, type);
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return type;
	}

	public void saveObjectToModel(Object obj, String type) {
		if (type.equals("D")) {
			doctor = (Doctor) obj;
		} else if (type.equals("P")) {
			patient = (Patient) obj;
			System.out.println(patient.toString());
		}

	}

	public Patient getPatientModel() {
		return patient;
	}
	
	public Doctor getDoctorModel() {
		return doctor;
	}

	@Override
	public Patient getPatient(String cpr) throws RemoteException {
		
		return server.getPatient(cpr);
	}

	@Override
	public Doctor getDoctor(String cpr) throws RemoteException {
		
		return server.getDoctor(cpr);
	}

	@Override
	public PatientList getPatientList() throws RemoteException {
		
		return server.getPatientList();
	}

	@Override
	public DoctorList getDoctorList() throws RemoteException {
		
		return server.getDoctorList();
	}

	@Override
	public boolean assignPatientToDoctor(String patientCpr, String doctorCpr) throws RemoteException {
			return server.assignPatientToDoctor(patientCpr,doctorCpr);

	}

	@Override
	public PatientList getAssignedPatientList(String cpr) throws RemoteException {
		
		return server.getAssignedPatientList(cpr);
	}

	@Override
	public PatientPrescription getPatientPrescription(String cpr) throws RemoteException {
		return server.getPatientPrescription(cpr);
		
	}

	@Override
	public boolean updatePrescription(String cpr, String prescription, LocalDate appointment, String problem,
			String recommendations) throws RemoteException {
		
		return server.updatePrescription(cpr, prescription, appointment, problem, recommendations);
	}

	@Override
	public boolean updatePatientHistory(String cpr, String ilnesses, String alergies, String height, String weight,
			String smoker, String vaccines, String familyIlnesses, String insurance, String pregnancy)
			throws RemoteException {
		System.out.println("ClientController");
		return server.updatePatientHistory(cpr,ilnesses,alergies,height,weight,smoker,vaccines,familyIlnesses,insurance,pregnancy);
		
	}

	
	@Override
	public void deleteDoctor(String cpr) throws RemoteException {
		server.deleteDoctor(cpr);
		
	}

	@Override
	public void deletePatient(String cpr) throws RemoteException {
		server.deletePatient(cpr);
		
	}

	@Override
	public void deletePatientByLogin(String login) throws RemoteException {
		server.deletePatientByLogin(login);
		
	}

	@Override
	public void deleteDoctorByLogin(String login) throws RemoteException {
		server.deleteDoctorByLogin(login);
		
	}




}
