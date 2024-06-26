package com.taskmanagement.task_management_app.project_manager.user;

import com.taskmanagement.task_management_app.db_connect.DbConnect;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Objects;
import java.util.ResourceBundle;

public class ViewUserController implements Initializable {
    @FXML
    private Label addres;

    @FXML
    private Label userDob;

    @FXML
    private Label dep;

    @FXML
    private Label email;

    @FXML
    private Label name;

    @FXML
    private Label phonenum;

    @FXML
    private Circle pic;

    @FXML
    private Label sex;

    @FXML
    private Label tgnum;

    @FXML
    private Label userType;

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
        selectedUser = UserController.getSelectedUser();
        loadData();
    }

    private void loadData(){
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM public.users WHERE user_id='"+selectedUser.getUser_id()+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){

                tgnum.setText(": "+resultSet.getString("user_id"));
                name.setText(": "+resultSet.getString("fname")+" "+resultSet.getString("lname"));
                email.setText(": "+resultSet.getString("email"));
                phonenum.setText(": "+resultSet.getString("phone_num"));
                userDob.setText(": "+resultSet.getString("dob"));
                sex.setText(": "+resultSet.getString("sex"));
                addres.setText(": "+resultSet.getString("address"));
                userType.setText(": "+resultSet.getString("user_roll"));

                InputStream is = resultSet.getBinaryStream("profile_pic");
                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = is.read(content)) != -1){
                    os.write(content,0,size);
                }
                os.close();
                is.close();

                pic.setFill(new ImagePattern(new Image("file:photo.jpg",0,0,true,true)));
            }

        }catch (Exception e){
            System.out.println(e);
        }

    }

    @FXML
    void backBtn(ActionEvent event) throws Exception{
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../team-members-home.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1050,600);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        stage.resizableProperty().setValue(false);
    }
}
