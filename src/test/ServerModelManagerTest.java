package test;

import static org.junit.jupiter.api.Assertions.*;

import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.postgresql.util.PSQLException;

import smartdocServer.domain.model.DoctorList;
import smartdocServer.domain.mediator.DBS;
import smartdocServer.domain.mediator.DbsPersistance;
import smartdocServer.domain.mediator.ServerModel;
import smartdocServer.domain.mediator.ServerModelManager;
import smartdocServer.domain.model.Doctor;
import smartdocServer.domain.model.Patient;
import smartdocServer.domain.model.PatientList;


class ServerModelManagerTest {
	
	static ServerModelManager server;
	
	@BeforeAll
	public static void initialize(){
		server = ServerModelManager.getInstance("postgres","123","5432","localhost","smartdocdatabase");
	}

	@Test
	void testSingleton() {
		String server1=ServerModelManager.getInstance("postgres","123","5432","localhost","smartdocdatabase").toString();
		String server2=ServerModelManager.getInstance("postgres","123","5432","localhost","smartdocdatabase").toString();
		assertEquals(server1,server2);
	}
	
	@Test
	void testVerifyLoginFail() {
		assertEquals(server.verifyLogin("blah", "123"),"0");
	}
	
	@Test
	void testCreatingPatient() 
	{
		LocalDate date = LocalDate.now();
		try {
			server.createPatient("da", "123", "DAR", "DAN", "120699-4008", 4324234, "sadad", date, "M");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		try {
			assertEquals(server.getPatient("120699-4008").getCpr(),"120699-4008");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}


	// @Test
	//void testAddingAndGettingDoctor() 
	// {
	///	LocalDate date = LocalDate.now();
	//	server.createDoctor("re", "123", "mar", "dar", "120695-3333", 231123, "dasda", date, "Gynecologist", "D", "F");
	//	assertEquals(server.getDoctor("120695-3333").getCpr(),"120695-3333");
	//}

	@Test
	void testDeletingPatient()
	{
		PatientList patientList = new PatientList();
		DbsPersistance dbsPersistance = new DBS("postgres","123","5432","localhost","smartdocdatabase");
		ArrayList<Object[]> array = dbsPersistance.getPatientList();
		
		for(Object[] object: array) 
		{
			String cpr = (String) object[0];
			String fname = (String) object[1];
			String lname = (String) object[2];
			int phone = (int) object[3];

			LocalDate dob = server.parseDateFromDbs((Date) object[4]);
			String email = (String) object[5];
			String type = (String) object[6];
			String gender = (String) object[7];


			Patient patient = new Patient(cpr, fname, lname, dob, phone, email, type, gender);
			patientList.addPatient(patient);;
		}
		
		System.out.println(patientList.getNumberOfPatients());
		assertEquals(patientList.getNumberOfPatients(),4);
		
		try {
			server.deletePatient("120699-4008");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Object[]> array1 = dbsPersistance.getPatientList();
		PatientList patientList1 = new PatientList();
		
		for(Object[] object: array1) 
		{
			String cpr = (String) object[0];
			String fname = (String) object[1];
			String lname = (String) object[2];
			int phone = (int) object[3];

			LocalDate dob = server.parseDateFromDbs((Date) object[4]);
			String email = (String) object[5];
			String type = (String) object[6];
			String gender = (String) object[7];


			Patient patient = new Patient(cpr, fname, lname, dob, phone, email, type, gender);
			patientList1.addPatient(patient);
		}
		
		System.out.println(patientList1.getNumberOfPatients());
		assertEquals(patientList1.getNumberOfPatients(),3);
		
		
	}

	@Test
	void testDeletingDoctor()
	{
		DoctorList doctorList = new DoctorList();
		DbsPersistance dbsPersistance = new DBS("postgres","123","5432","localhost","smartdocdatabase");
		ArrayList<Object[]> array = dbsPersistance.getDoctorList();
		
		for(Object[] object: array) 
		{
			String cpr = (String) object[0];
			String fname = (String) object[1];
			String lname = (String) object[2];
			int phone = (int) object[3];

			LocalDate dob = server.parseDateFromDbs((Date) object[4]);
			String email = (String) object[5];
			String type = (String) object[6];
			String gender = (String) object[7];
				
			ArrayList<Object[]> arraySpeciality = dbsPersistance.getSpeciality(cpr);
			System.out.println(cpr);
			String speciality = (String) arraySpeciality.get(0)[0];

			Doctor doctor = new Doctor(cpr, fname, lname, dob, phone, email, type, gender, speciality);
			doctorList.addDoctor(doctor);
		
	}
		System.out.println(doctorList.getNumberOfDoctors());
		assertEquals(doctorList.getNumberOfDoctors(),1);
		
		try {
			server.deleteDoctor("120945-4444");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		ArrayList<Object[]> array1 = dbsPersistance.getDoctorList();
		DoctorList doctorList1 = new DoctorList();
		
		for(Object[] object: array1) 
		{
			String cpr = (String) object[0];
			String fname = (String) object[1];
			String lname = (String) object[2];
			int phone = (int) object[3];

			
			LocalDate dob = server.parseDateFromDbs((Date) object[4]);
			String email = (String) object[5];
			String type = (String) object[6];
			String gender = (String) object[7];
				
			ArrayList<Object[]> arraySpeciality = dbsPersistance.getSpeciality(cpr);
			System.out.println(cpr);
			String speciality = (String) arraySpeciality.get(0)[0];

			Doctor doctor = new Doctor(cpr, fname, lname, dob, phone, email, type, gender, speciality);
			doctorList1.addDoctor(doctor);
		
	}
		System.out.println(doctorList.getNumberOfDoctors());
		assertEquals(doctorList.getNumberOfDoctors(),0);
  }
}
