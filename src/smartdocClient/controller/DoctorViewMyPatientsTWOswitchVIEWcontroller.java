package smartdocClient.controller;

import java.io.IOException;
import java.net.URL;
import java.rmi.RemoteException;
import java.time.LocalDate;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import smartdocServer.domain.model.Patient;
import smartdocServer.domain.model.PatientPrescription;

public class DoctorViewMyPatientsTWOswitchVIEWcontroller implements Initializable {

	@FXML
	private Label fNameLabel;

	@FXML
	private Label lNameLabel;

	@FXML
	private Label dobLabel;

	@FXML
	private Label emailLabel;

	@FXML
	private Label genderLabel;

	@FXML
	private Label typeLabel;

	@FXML
	private Label cprLabel;

	@FXML
	private Label phoneNumberLabel;

	private String cprFromPreviousScene;

	@FXML
	private Button signOut;
	@FXML
	private Button back;
	


	private ClientController controller;

	public DoctorViewMyPatientsTWOswitchVIEWcontroller() {

		controller = ClientController.getInstance();

		;

	}

	public void signOutButtonPressed(ActionEvent event) throws IOException {
		{
			Parent register = FXMLLoader.load(getClass().getResource("LoginPage.fxml"));
			Scene home_page_scene = new Scene(register);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(home_page_scene);
			app_stage.show();

		}
	}

	public void backButtonPressed(ActionEvent event) throws IOException {
		{
			//
			String CPR = cprFromPreviousScene;
		
		FXMLLoader loader = new FXMLLoader();
		loader.setLocation(getClass().getResource("DoctorViewMyPatientsTWO.fxml"));
		
		Parent root = loader.load();
		
		Scene home_page_scene = new Scene(root);
		
		
		DoctorViewMyPatientsTWOcontroller transfer = loader.getController();
		transfer.setcprFromPreviousScene(CPR);
		
		
		Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
		
		app_stage.setScene(home_page_scene);
		app_stage.show();

		}
	}

	public void applyButtonPressed(ActionEvent event) throws IOException {
		{
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("SUCCESS");
			alert.setContentText(
					"The operation was completed without any errors!");
			alert.showAndWait();

		}
	}

	public void setcprFromPreviousScene(String cpr) {
		System.out.println(cprFromPreviousScene + " before instantiate");

		this.cprFromPreviousScene = cpr;

		System.out.println(cprFromPreviousScene + " after instantiate");
		try {
			Patient patient = controller.getPatient(cprFromPreviousScene);
			// make and PResciption list

			fNameLabel.setText(patient.getFname());
			lNameLabel.setText(patient.getLname());
			dobLabel.setText(patient.getDob().toString());
			emailLabel.setText(patient.getEmail());
			genderLabel.setText(patient.getGender());
			typeLabel.setText(patient.getType());
			cprLabel.setText(patient.getCpr());
			phoneNumberLabel.setText(patient.getPhone() + "");
		} catch (RemoteException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@Override
	public void initialize(URL arg0, ResourceBundle arg1) {

	

	}

	
}
