package com.taskmanagement.task_management_app.admin.project;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextArea;
import com.jfoenix.controls.JFXTextField;

import com.taskmanagement.task_management_app.db_connect.DbConnect;
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
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AddProjectControlloer implements Initializable {

    @FXML
    private JFXButton addBtn;

    @FXML
    private JFXButton clearBtn;

    @FXML
    private JFXComboBox<String> comboUserType;

    @FXML
    private Label error;

    @FXML
    private JFXTextArea txtDescription;

    @FXML
    private JFXTextField txtEdate;

    @FXML
    private JFXTextField txtName;

    @FXML
    private JFXTextField txtSdate;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboboxDataLoad();
    }

    private void comboboxDataLoad(){
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM public.users WHERE user_roll='Project Managers'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            ObservableList data = FXCollections.observableArrayList();

            while (resultSet.next()){
                data.add(new String(resultSet.getString("user_id")));
            }
            comboUserType.setItems(data);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    void clearNotice(ActionEvent event) {
        reset();
    }

    @FXML
    void addNotice(ActionEvent event) {

        if(txtName.getText().length() == 0 || txtDescription.getText().length() == 0 || txtSdate.getText().length() == 0 || txtEdate.getText().length() == 0 || comboUserType.getValue().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else {

            try {

                String proName = txtName.getText();
                String proDescription = txtDescription.getText();
                String proSdate = txtSdate.getText();
                String proEdate = txtEdate.getText();
                String proUserType = comboUserType.getValue();

                connection = DbConnect.getConnect();
                query = "INSERT INTO public.projects (project_name, description, start_date, end_date, progress, project_manager_id) VALUES (?, ?, ?, ?, ?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,proName);
                preparedStatement.setString(2,proDescription);
                preparedStatement.setString(3,proSdate);
                preparedStatement.setString(4,proEdate);
                preparedStatement.setString(5,"0");
                preparedStatement.setString(6,"TM/001");
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully created");
                alert.setContentText("successfully created new Notice");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    reset();
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-project.fxml")));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.close();
                }

            }catch (Exception e){
                System.out.println(e);
            }
        }
    }

    private void reset(){
        txtName.setText("");
        txtDescription.setText("");
        txtSdate.setText("");
        txtEdate.setText("");
        comboUserType.setValue("");
    }
}
