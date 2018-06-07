package smartdocServer.domain.mediator;

import java.rmi.RemoteException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

import org.postgresql.util.PSQLException;

import com.mysql.jdbc.PreparedStatement;

import java.sql.Date;

import utility.persistence.MyDatabase;

/**
 * The class is the so called "adapter" for the database. Its' methods main
 * purpose is to retrieve or send data to database, and pass the data further to
 * the system.
 * 
 * @author Michal Ciebien
 *
 */
public class DBS implements DbsPersistance {

	private MyDatabase myDatabase;

	/**
	 * The DBS constructor initializes the connection with the database.
	 */
	public DBS(String username, String password, String port, String ip, String databaseName) {
		try {
			myDatabase = new MyDatabase("org.postgresql.Driver",
					"jdbc:postgresql://" + ip + ":" + port + "/" + databaseName + "", "" + username, "" + password);
		} catch (ClassNotFoundException e) {

			e.printStackTrace();
		}
	}

	/**
	 * Changes the parameter password into a new hexadecimal String, which is then
	 * ready to be passed to database
	 * 
	 * @param password
	 * @return password as hexadecimal string
	 */
	public String passwordToHex(String password) {
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		byte[] buffer = password.getBytes();
		md.update(buffer);
		byte[] digest = md.digest();

		StringBuffer sb = new StringBuffer();
		for (byte b : digest) {
			sb.append(Integer.toHexString((int) (b & 0xff)));
		}
		String passwordHex = sb.toString();
		return passwordHex.toUpperCase();
	}

	@Override
	public String verifyLogin(String login, String password) {
		String passwordHex = "";

		passwordHex = passwordToHex(password);

		String sql = "SELECT cpr from account where (login=? and password=?)";
		try {
			System.out.println(passwordHex);
			ArrayList<Object[]> array = myDatabase.query(sql, login, passwordHex);

			if (array.isEmpty()) {
				return "0";
			} else {
				return (String) array.get(0)[0];
			}
		} catch (SQLException e) {
			e.printStackTrace();
			return "0";

		}

	}

	/**
	 * Creates a doctor in the database, by inserting data to relations
	 * 
	 * @param login
	 * @param password
	 * @param fname
	 * @param lname
	 * @param cpr
	 * @param phone
	 * @param email
	 * @param dob
	 * @param speciality
	 * @param type
	 * @param gender
	 * @return
	 */
	@Override
	public boolean createDoctor(String login, String password, String fname, String lname, String cpr, int phone,
			String email, LocalDate dob, String speciality, String type, String gender) {

		String passwordHex = passwordToHex(password);

		String sql = "insert into account values (?,?,?)";
		System.out.println("Adding a new doctor with these data: " + login + " " + password + " " + cpr + " " + type);
		String sql1 = "insert into account_data values (?,?,?,?,?,?,?,?)";
		String sql2 = "insert into doctor_speciality values (?,?)";

		Date dateSQL = parseDateToDbs(dob);

		System.out.println(dob.toString());

		try {
			myDatabase.update(sql, cpr, login, passwordHex);
			myDatabase.update(sql1, cpr, fname, lname, phone, dateSQL, email, type, gender);
			myDatabase.update(sql2, cpr, speciality);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 * Creates a patient in the database, by inserting data to relations
	 * 
	 * @param login
	 * @param password
	 * @param fname
	 * @param lname
	 * @param cpr
	 * @param phone
	 * @param email
	 * @param dob
	 * @param gender
	 * @return
	 */
	@Override
	public boolean createPatient(String login, String password, String fname, String lname, String cpr, int phone,
			String email, LocalDate dob, String gender) {

		String passwordHex = passwordToHex(password);
		String sql = "insert into account values (?,?,?)";
		String sql1 = "insert into account_data values(?,?,?,?,?,?,?,?)";
		String sql2 = "insert into patient_data values(?,?,?,?,?,?,?,?,?,?)";
		String sql3 = "insert into patient_prescription values(?,?,?,?,?)";

		Date dateSQL = parseDateToDbs(dob);
		String type = "P";

		try {
			myDatabase.update(sql, cpr, login, passwordHex);
			myDatabase.update(sql1, cpr, fname, lname, phone, dateSQL, email, type, gender);
			myDatabase.update(sql2, cpr, '0', '0', 0, 0, false, '0', '0', false, false);
			myDatabase.update(sql3, cpr, '0', parseDateToDbs(LocalDate.now()), '0', '0');
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * The method parses the LocalDate parameter into Date object that is compatible
	 * with postgreSQL database.
	 * 
	 * @param date
	 * @return date object compatible with database
	 */
	public Date parseDateToDbs(LocalDate date) {
		String[] dateVariables = date.toString().split("-");
		String trimYear = dateVariables[0];
		String trimMonth = dateVariables[1];
		String trimDay = dateVariables[2];

		System.out.println(trimYear + " " + trimMonth + " " + trimDay);

		int year = Integer.parseInt(trimYear) - 1900;
		int month = Integer.parseInt(trimMonth);

		month--;

		int day = Integer.parseInt(trimDay);

		Date dateSQL = new Date(year, month, day);
		return dateSQL;
	}

	/**
	 * Look for a matching cpr result in the database and returns that row as an
	 * array of objects in an ArrayList which contains one result
	 * 
	 * @param cpr
	 * @return
	 */
	@Override
	public ArrayList<Object[]> getDoctor(String cpr) {

		String sql = "SELECT * from account_data where (cpr=?)";

		ArrayList<Object[]> array = null;

		try {
			array = myDatabase.query(sql, cpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Looks for a matching cpr result in the database and returns that row as an
	 * array of objects in an ArrayList which contains one result
	 * 
	 * @param cpr
	 * @return
	 */
	@Override
	public ArrayList<Object[]> getSpeciality(String cpr) {
		String sql = "SELECT speciality from doctor_speciality where (cpr=?)";
		ArrayList<Object[]> array = null;

		try {
			array = myDatabase.query(sql, cpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * Look for a matching cpr result in the database and returns that row as an
	 * array of objects in an ArrayList which contains one result
	 * 
	 * @param cpr
	 * @return
	 */
	@Override
	public ArrayList<Object[]> getAccountData(String cpr) {
	
		String sql = "SELECT * from account_data where (cpr=?)";
		ArrayList<Object[]> array = null;
		// array needs to be initialized before try / catch
	
		try {
			array = myDatabase.query(sql, cpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * 
	 * 
	 * @param
	 * @return It returns an ArrayList full of Arrays that contains Patient model
	 */
	@Override
	public ArrayList<Object[]> getPatientList() {
		String sql = "SELECT * from account_data where (type=?)";
		ArrayList<Object[]> array = null;
		String type = "P";
		try {
			array = myDatabase.query(sql, type);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * 
	 * 
	 * @param
	 * @return It returns an ArrayList full of Arrays that contains Doctor model
	 */
	@Override
	public ArrayList<Object[]> getDoctorList() {
		String sql = "SELECT * from account_data where (type=?)";
		ArrayList<Object[]> array = null;
		String type = "D";
		try {
			array = myDatabase.query(sql, type);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * This method is conencting the cpr of the doctor and the cpr of the patient in
	 * the database
	 * 
	 * @param patientCpr
	 * @param doctorCpr
	 * @return
	 */
	@Override
	public boolean assignPatientToDoctor(String patientCpr, String doctorCpr) {
		String sql = "INSERT INTO patientDoctor values(?,?)";

		try {
			myDatabase.update(sql, patientCpr, doctorCpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");

			e.printStackTrace();
			return false;
		}

		return true;
	}

	/**
	 *
	 * 
	 * @param cpr
	 * @return The method returns searches for the Doctor's CPR in the database and it
	 * return an ArrayList full of Arrays that are containing the patients that are
	 * assigned to this doctor
	 */
	@Override
	public ArrayList<Object[]> getAssignedPatientList(String cpr) {

		String sql = "select pd.* from (select patientCpr as p from patientDoctor where"
				+ " (doctorCpr=?)) pcpr, account_data pd where pcpr.p =" + "pd.cpr;";

		ArrayList<Object[]> array = null;

		try {
			array = myDatabase.query(sql, cpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * It returns an ArrayList full of Arrays that are representing the prescription
	 * fields (recommendation,prescription,appointment,problem)
	 * 
	 * @param cpr
	 * @return
	 */
	public ArrayList<Object[]> getPatientPrescription(String cpr) {
		String sql = "select * from patient_prescription where cpr =? ";
		ArrayList<Object[]> array = null;

		try {
			array = myDatabase.query(sql, cpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
		return array;
	}

	/**
	 * It deletes Patient from all the databases that was present in by their CPR.
	 * 
	 * @param cpr
	 */
	public void deletePatient(String cpr) {
		String sql = "delete from account_data where (cpr=?)";
		String sql1 = "delete from patient_prescription where (cpr=?)";
		String sql2 = "delete from patient_data where (cpr=?)";
		String sql3 = "delete from patientDoctor where (patientCpr=?)";
		String sql4 = "delete from account where (cpr=?)";

		try {
			myDatabase.update(sql, cpr);
			myDatabase.update(sql1, cpr);
			myDatabase.update(sql2, cpr);
			myDatabase.update(sql3, cpr);
			myDatabase.update(sql4, cpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}

	}

	/**
	 * It updates the Prescription based on the CPR entered,modifying the
	 * prescription fields (recommendation,prescription,appointment,problem)
	 * 
	 * @param cpr
	 */
	public void deleteDoctor(String cpr) {
		String sql = "delete from account_data where (cpr=?)";
		String sql1 = "delete from doctor_speciality where (cpr=?)";
		String sql2 = "delete from patientDoctor where (doctorCpr=?)";
		String sql3 = "delete from account where (cpr=?)";

		try {
			myDatabase.update(sql, cpr);
			myDatabase.update(sql1, cpr);
			myDatabase.update(sql2, cpr);
			myDatabase.update(sql3, cpr);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
	}

	/**
	 * It deletes the Doctor from all the databases that was present in by their
	 * CPR.
	 * 
	 * @param login
	 */
	public void deleteDoctorByLogin(String login) {
		String sql = "SELECT cpr from account where (login=?)";

		ArrayList<Object[]> array = null;

		try {
			array = myDatabase.query(sql, login);
			deleteDoctor((String) array.get(0)[0]);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
	}

	/**
	 * It deletes Patient from all the databases that was present in by their
	 * username.
	 * 
	 * @param login
	 */
	public void deletePatientByLogin(String login) {
		String sql = "SELECT cpr from account where (login=?)";

		ArrayList<Object[]> array = null;

		try {
			array = myDatabase.query(sql, login);
			deletePatient((String) array.get(0)[0]);
		} catch (SQLException e) {
			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
		}
	}

	/**
	 * It updates the Prescription based on the CPR entered,modifying the
	 * prescription fields (recommendation,prescription,appointment,problem)
	 * 
	 * @param cpr
	 */
	@Override
	public boolean updatePrescription(String cpr, String prescription, LocalDate appointment, String problem,
			String recommendations) {

		String sql = "update patient_prescription set cpr=?, prescription=?, appointments=?, problem=?,recommendations=?"
				+ "where cpr=?";

		try {
			myDatabase.update(sql, cpr, prescription, parseDateToDbs(appointment), problem, recommendations, cpr);
		} catch (SQLException e) {

			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * It updates Patient History based on the CPR entered,modifying the
	 * PatientHistory fields.
	 * 
	 * @param cpr
	 */
	@Override
	public boolean updatePatientHistory(String cpr, String ilnesses, String alergies, String height, String weight,
			String smoker, String vaccines, String familyIlnesses, String insurance, String pregnancy) {

		String sql = "update patient_history set cpr=?, ilnesses=?, alergies=?, height=?,weight=?,smoker=?,vaccines=?,familyIlnesses=?,insurance=?,pregnancy=? where cpr=?";

		System.out.println("WORKS");
		try {
			myDatabase.update(sql, cpr, ilnesses, alergies, height.toString(), weight.toString(), smoker, vaccines,
					familyIlnesses, insurance, pregnancy, cpr);
		} catch (SQLException e) {

			System.out.println("Failed to retrieve data from database");
			e.printStackTrace();
			return false;
		}
		return true;
	}

	/**
	 * This method initialize the system with the ADMIN user that has
	 * pre-established login and pass
	 */
	@Override
	public void initializeAdmin() {
		System.out.println("Initializing admin with login: admin, password: 321");
		String sql = "insert into account values ('111111-2222','admin','8D23CF6C86E834A7AA6EDED54C26CE2BB2E7493538C61BDD5D2197997AB2F72')";
		try {
			myDatabase.update(sql);
		} catch (SQLException e) {
			System.out.println("Admin created/already exists");
		}
	}

}
