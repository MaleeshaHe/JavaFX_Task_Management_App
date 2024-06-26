package com.taskmanagement.task_management_app.project_manager.user;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXPasswordField;
import com.jfoenix.controls.JFXTextField;
import com.taskmanagement.task_management_app.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class EditUserControlloer implements Initializable {
    @FXML
    private JFXComboBox<String> comboGender;

    @FXML
    private JFXComboBox<String> comboUserType;
    @FXML
    private Circle imageView;
    @FXML
    private JFXTextField txtAddress;

    @FXML
    private DatePicker txtDoB;

    @FXML
    private JFXTextField txtEmail;

    @FXML
    private JFXTextField txtFname;

    @FXML
    private JFXTextField txtLname;

    @FXML
    private JFXPasswordField txtPassword;

    @FXML
    private JFXPasswordField txtPasswordC;

    @FXML
    private JFXTextField txtPhoneNumber;

    @FXML
    private JFXTextField txtRnumber;
    @FXML
    private Label error;
    @FXML
    private BorderPane borderpane;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;
    private UserDetails selectedUser;
    private FileInputStream fis;
    private File img;
    String query = null;
    Connection connection = null ;
    ResultSet resultSet = null ;
    PreparedStatement preparedStatement = null ;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboInitialize();
        selectedUser = UserController.getSelectedUser();
        loadData();
    }

    private void comboInitialize(){
        imageView.setFill(new ImagePattern(new Image(getClass().getResourceAsStream("..\\..\\..\\..\\..\\images\\profilePic.jpg"))));
        comboGender.setItems(FXCollections.observableArrayList("Male","Female","Other"));
        comboUserType.setItems(FXCollections.observableArrayList("Admin","Project Managers","Team Members"));
    }

    private void loadData(){
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM public.users WHERE user_id='"+selectedUser.getUser_id()+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                txtRnumber.setText(resultSet.getString("user_id"));
                txtFname.setText(resultSet.getString("fname"));
                txtLname.setText(resultSet.getString("lname"));
                txtEmail.setText(resultSet.getString("email"));
                txtPhoneNumber.setText(resultSet.getString("phone_num"));
                txtAddress.setText(resultSet.getString("address"));
                txtDoB.setValue(LocalDate.parse(resultSet.getString("dob")));
                txtPassword.setText(resultSet.getString("password"));
                txtPasswordC.setText(resultSet.getString("password"));
                comboUserType.getSelectionModel().select(resultSet.getString("user_roll"));
                comboGender.getSelectionModel().select(resultSet.getString("sex"));

                InputStream is = resultSet.getBinaryStream("profile_pic");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1){
                    os.write(content,0,size);
                }
                os.close();
                is.close();

                imageView.setFill(new ImagePattern(new Image("file:photo.jpg",0,0,true,true)));
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }
    @FXML
    void backBtn(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../team-members-home.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080,610);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        stage.resizableProperty().setValue(false);
    }


    @FXML
    void clearBtn(ActionEvent event) {
        txtRnumber.setText("");
        txtFname.setText("");
        txtLname.setText("");
        txtEmail.setText("");
        txtPhoneNumber.setText("");
        txtAddress.setText("");
        txtPassword.setText("");
        txtPasswordC.setText("");
        comboInitialize();
    }

    @FXML
    void choosePhoto(ActionEvent event) {
        try {
            FileChooser fileopen = new FileChooser();
            img = fileopen.showOpenDialog(stage);
            imageView.setFill(new ImagePattern(new Image(img.toURI().toString(),0,0,true,true)));
        }catch (Exception e){
        }

    }

    @FXML
    void updateUser(ActionEvent event) {

        if(txtDoB.getValue() == null  || comboGender.getValue() == null || comboUserType.getValue() == null || txtPassword.getText().length() == 0 || txtPasswordC.getText().length() == 0 || txtAddress.getText().length() == 0 || txtPhoneNumber.getText().length() == 0 || txtRnumber.getText().length() == 0 || txtFname.getText().length() == 0 || txtLname.getText().length() == 0 || txtEmail.getText().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else if (!(txtPasswordC.getText().equals(txtPassword.getText()))) {
            new Shake(error).play();
            error.setText("Password are not matching");

        }else if (img == null) {
            new Shake(error).play();
            new Shake(imageView).play();
            imageView.setStroke(Color.RED);
            error.setText("Please add a photo");
        }
        else {
            LocalDate birtDate = txtDoB.getValue();

            String tgnum = txtRnumber.getText();
            String fname = txtFname.getText();
            String lname = txtLname.getText();
            String email = txtEmail.getText();
            String phoneNum = txtPhoneNumber.getText();
            String address = txtAddress.getText();
            String dob = String.valueOf(birtDate);
            String userRole = comboUserType.getValue();
            String gender = comboGender.getValue();
            String password = txtPasswordC.getText();
            String stgnum = selectedUser.getUser_id();



            try {
                connection = DbConnect.getConnect();
                query = "UPDATE public.users SET user_id = ?, fname= ?, lname = ?, phone_num = ?, email = ?, password = ?, dob = ?, sex = ?, address = ?, user_roll = ?, profile_pic = ? WHERE user_id = ?";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,tgnum);
                preparedStatement.setString(2,fname);
                preparedStatement.setString(3,lname);
                preparedStatement.setString(4,phoneNum);
                preparedStatement.setString(5,email);
                preparedStatement.setString(6,password);
                preparedStatement.setString(7,dob);
                preparedStatement.setString(8,gender);
                preparedStatement.setString(9,address);
                preparedStatement.setString(10,userRole);
                fis = new FileInputStream(img);
                preparedStatement.setBinaryStream(11, (InputStream)fis, (int)img.length());
                preparedStatement.setString(12,stgnum);
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfull");
                alert.setContentText("successfully update User");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../team-members-home.fxml")));
                    stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                    scene = new Scene(root, 1080,610);
                    stage.setScene(scene);
                    stage.centerOnScreen();
                    stage.show();
                    stage.resizableProperty().setValue(false);
                }

            }catch (Exception e){
                System.out.println(e);
            }

        }
    }
}
