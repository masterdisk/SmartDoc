package smartdocServer.domain.mediator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;

public interface DbsPersistance {
	

	public boolean verifyLogin(String login, String password);
	public boolean createDoctor(String fname, String lname, int cpr, int phone, Date dob, String speciality) ;
}
