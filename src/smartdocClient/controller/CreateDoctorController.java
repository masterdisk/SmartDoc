package smartdocClient.controller;

import java.io.IOException;
import java.net.URL;
import java.util.Date;
import java.util.ResourceBundle;

import com.jfoenix.controls.JFXComboBox;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.MenuButton;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class CreateDoctorController implements Initializable{

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
	private JFXComboBox genderchoice;
	@FXML
	private JFXComboBox doctortype;
	
	
	private ClientController clientController;
	
	public void backButtonPressed(ActionEvent event) throws IOException {
		{
			Parent register = FXMLLoader.load(getClass().getResource("../view/AdministratorGUI.fxml"));
			Scene home_page_scene = new Scene(register);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(home_page_scene);
			app_stage.show();
		}

	}
	
	public void clearButtonPressed(ActionEvent event) throws IOException {
		{
			Parent register = FXMLLoader.load(getClass().getResource("../view/CreateDoctor.fxml"));
			Scene home_page_scene = new Scene(register);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(home_page_scene);
			app_stage.show();
		}

	}
	

	
	public void registerButtonPressed(ActionEvent event) throws IOException {
		{
			String firstnamE=firstName.getText();
			String lastnamE=lastName.getText();
			String cpR=cPR.getText();
			
			String specialitY=speciality.getText();
			String phonenO=phoneNo.getText();
			
			int cpr = Integer.parseInt(cpR);
			int phoneNo = Integer.parseInt(phonenO);
			
			String gender= genderchoice.getValue().toString();
			String doctype= doctortype.getValue().toString();
			
			
		clientController  = ClientController.getInstance();
//		clientController.createDoctor(firstnamE, lastnamE, cpr, phoneNo, dob, specialitY);
			
			
			Parent register = FXMLLoader.load(getClass().getResource("../view/SUCCESS.fxml"));
			Scene home_page_scene = new Scene(register);
			Stage app_stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
			app_stage.setScene(home_page_scene);
			app_stage.show();
		}

	}
	
	@Override
	public void initialize(URL location, ResourceBundle resources) {
		genderchoice.getItems().setAll("Male", "Female", "Other");
		doctortype.getItems().setAll("General", "Specific");
	}

}
