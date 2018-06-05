package smartdocClient.controller;

import java.io.IOException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.stage.Stage;

public class CreateDoctorController implements Initializable {

	@FXML
	private TextField firstName;
	@FXML
	private TextField lastName;
	@FXML
	private TextField cPR;
	@FXML
	private TextField dob;
	@FXML
	private TextField speciality;
	@FXML
	private TextField phoneNo;
	@FXML
	private TextField email;
	@FXML
	private TextField username;
	@FXML
	private PasswordField pass;

	@FXML
	private Button register;
	@FXML
	private Button clear;

	@FXML
	private Button back;

	@FXML
	private JFXComboBox<String> genderchoice;
	@FXML
	private JFXComboBox<String> doctortype;

	private ClientController clientController;

	public void backButtonPressed(ActionEvent event) throws IOException {
		{
			Parent register = FXMLLoader.load(getClass().getResource("AdministratorGUI.fxml"));
			Scene home_page_scene = new Scene(register);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(home_page_scene);
			app_stage.show();
		}

	}

	public void clearButtonPressed(ActionEvent event) throws IOException {
		{
			Parent register = FXMLLoader.load(getClass().getResource("CreateDoctor.fxml"));
			Scene home_page_scene = new Scene(register);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(home_page_scene);
			app_stage.show();
		}

	}

	public void registerButtonPressed(ActionEvent event) throws IOException {
		{
			
			try {
				String firstnameVar = firstName.getText();
				String lastnameVar = lastName.getText();
				String cprVar = cPR.getText();

				String specialityVar = speciality.getText();
				String phonenoVar = phoneNo.getText();

				String date = dob.getText();
				String[] dateVariables = new String[2];
				dateVariables = date.split("-");
				String trimYear = dateVariables[0];
				String trimMonth = dateVariables[1];
				String trimDay = dateVariables[2];

				System.out.println(trimYear + " " + trimMonth + " " + trimDay);

				LocalDate dateOfBirthVar = LocalDate.of(Integer.parseInt(trimYear), Integer.parseInt(trimMonth),
						Integer.parseInt(trimDay));

				int phoneNoVar = Integer.parseInt(phonenoVar);

				String usernameVar = username.getText();
				String passwordVar = pass.getText();
				String emailVar = email.getText();

				String gender = genderchoice.getValue().toString();

				String doctype = doctortype.getValue().toString();
				if (doctype.equals("General")) {
					doctype = "G";
				} else {
					doctype = "D";
				}

				if (gender.equals("Male")) {
					gender = "M";
				} else if (gender.equals("Female")) {
					gender = "F";
				} else {
					gender = "M";
				}

				// PASSING DOCTOR INFO TO CLIENT
				clientController = ClientController.getInstance();
				clientController.createDoctor(usernameVar, passwordVar, firstnameVar, lastnameVar, cprVar, phoneNoVar,
						emailVar, dateOfBirthVar, specialityVar, doctype, gender);

			} catch(Exception e) 
			{
				Alert alert = new Alert(AlertType.ERROR);
				alert.setTitle("Failure");
				alert.setContentText(
						"The operation failed, make sure data are valid!");
				alert.showAndWait();
				return;
			}
			
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("SUCCESS");
			alert.setContentText(
					"The operation was completed without any errors!");
			alert.showAndWait();
		}

	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		genderchoice.getItems().setAll("Male", "Female", "Other");
		doctortype.getItems().setAll("General", "Specific");
	}

}
