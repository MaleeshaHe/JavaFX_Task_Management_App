package com.taskmanagement.task_management_app.admin;

import animatefx.animation.*;
import com.jfoenix.controls.JFXButton;
import com.taskmanagement.task_management_app.UserSession;
import com.taskmanagement.task_management_app.db_connect.DbConnect;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class AdminHomeController implements Initializable {

    @FXML
    private Label time;
    @FXML
    private Label date;
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
    private Label tgnum;

    @FXML
    private Label welcomeName;


    @FXML
    private ImageView Exit;

    @FXML
    private Label Menu;

    @FXML
    private Label MenuClose;

    @FXML
    private AnchorPane slider;

    @FXML
    private BorderPane mainPane;

    @FXML
    private BorderPane borderpane;

    @FXML
    private JFXButton about;

    @FXML
    private JFXButton user;

    @FXML
    private Stage stage;
    private Scene scene;
    private Parent root;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dateShow();
        timeNow();
        showData();
    }
    private void showData(){
        String usertg = UserSession.getUserTgNum();
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM public.users WHERE user_id='"+usertg+"'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()){
                tgnum.setText(": "+resultSet.getString("user_id"));
                name.setText(": "+resultSet.getString("fname")+" "+resultSet.getString("lname"));
                email.setText(": "+resultSet.getString("email"));
                phonenum.setText(": "+resultSet.getString("phone_num"));
                dep.setText(": "+resultSet.getString("user_roll"));
                welcomeName.setText("Welcome "+resultSet.getString("fname"));


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

    private void dateShow(){
        SimpleDateFormat sdf = new SimpleDateFormat("'Today is 'MMMM dd, yyyy");
        String datenow = sdf.format(new Date());
        date.setText(datenow);
    }
    private void timeNow(){
        Thread thread = new Thread(() -> {
            SimpleDateFormat sdf = new SimpleDateFormat("hh : mm : ss a");
            while(true){
                try{
                    Thread.sleep(1000);
                }catch(Exception e){
                    System.out.println(e);
                }
                final String timenow = sdf.format(new Date());
                Platform.runLater(() -> {
                    time.setText(timenow); // This is the label
                });
            }
        });
        thread.start();
    }

    public void btnEditUser(ActionEvent actionEvent) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("admin-update.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Update Admin Details");
            javafx.scene.image.Image image = new Image("images/appIcon.png");
            stage.getIcons().add(image);
            stage.resizableProperty().setValue(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
            //Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void btnHome(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("admin-home.fxml"));
        root = loader.load();
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 1080,610);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.show();
        stage.resizableProperty().setValue(false);
        new FadeIn(root).play();
    }

    public void btnUser(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("user/user.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }

    public void btnProject(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("project/project.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }

    public void btnTask(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("task/task.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }

    public void btnAssign(ActionEvent actionEvent) throws IOException {
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("assign/assign.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInDown(view).play();
    }

    public void btnLogOut(ActionEvent event) throws IOException {

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("../login-page.fxml")));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root, 600,400);
        stage.setScene(scene);
        stage.centerOnScreen();
        stage.setTitle("TECMIS");
        stage.show();
        stage.resizableProperty().setValue(false);
        new FadeIn(root).play();
        UserSession.cleanUserSession();
    }
}

