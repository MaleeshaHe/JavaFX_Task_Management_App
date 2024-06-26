package com.taskmanagement.task_management_app.project_manager.user;

import animatefx.animation.FadeIn;
import animatefx.animation.FadeInRight;
import com.jfoenix.controls.JFXTextField;
import com.taskmanagement.task_management_app.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.util.Callback;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import java.util.Optional;
import java.util.ResourceBundle;

public class UserController implements Initializable {

    @FXML
    private BorderPane borderpane;
    @FXML
    private TableColumn<UserDetails, String> departmentcol;
    @FXML
    private TableColumn<UserDetails, String> photocol;
    @FXML
    private TableColumn<UserDetails, String> emailcol;
    @FXML
    private TableColumn<UserDetails, String> fnamecol;
    @FXML
    private TableColumn<UserDetails, String> idcol;
    @FXML
    private TableColumn<UserDetails, String> lnamecol;
    @FXML
    private TableColumn<UserDetails, String> phonecol;
    @FXML
    private TableView<UserDetails> userTable;
    @FXML
    private JFXTextField txtKeyword;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    UserDetails userDetails = null ;
    ObservableList<UserDetails> userList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    private static UserDetails selectedUser;
    public static UserDetails getSelectedUser() {
        return selectedUser;
    }
    public static void setSelectedUser(UserDetails user) {
        selectedUser = user;
    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        idcol.setCellValueFactory(new PropertyValueFactory<>("user_id"));
        fnamecol.setCellValueFactory(new PropertyValueFactory<>("fname"));
        lnamecol.setCellValueFactory(new PropertyValueFactory<>("lname"));
        phonecol.setCellValueFactory(new PropertyValueFactory<>("phone_num"));
        emailcol.setCellValueFactory(new PropertyValueFactory<>("email"));
        departmentcol.setCellValueFactory(new PropertyValueFactory<>("password"));

        Callback<TableColumn<UserDetails, String>, TableCell<UserDetails, String>> cellFoctory = (TableColumn<UserDetails, String> param) -> {
            final TableCell<UserDetails, String> cell = new TableCell<UserDetails, String>() {
                @Override
                public void updateItem(String item, boolean empty) {
                    super.updateItem(item, empty);

                    if (empty) {
                        setGraphic(null);
                        setText(null);

                    } else {

                        UserDetails userDetails = getTableView().getItems().get(getIndex());
                        String tg = userDetails.getUser_id(); // get tgnum value from UserDetails object

                        Circle propic = new Circle(25);
                        try {
                            connection = DbConnect.getConnect();
                            query = "SELECT profile_pic FROM public.users WHERE user_id='"+tg+"'";
                            preparedStatement = connection.prepareStatement(query);
                            resultSet = preparedStatement.executeQuery();

                            while (resultSet.next()){

                                InputStream is = resultSet.getBinaryStream("profile_pic");
                                OutputStream os = new FileOutputStream(new File("photo.jpg"));
                                byte[] content = new byte[1024];
                                int size = 0;
                                while ((size = is.read(content)) != -1){
                                    os.write(content,0,size);
                                }
                                os.close();
                                is.close();

                                propic.setFill(new ImagePattern(new Image("file:photo.jpg",0,0,true,true)));
                            }

                        }catch (Exception e){
                            System.out.println(e);
                        }

                        HBox managebtn = new HBox(propic);
                        managebtn.setStyle("-fx-alignment:center");
                        HBox.setMargin(propic, new Insets(5, 2, 5, 2));
                        setGraphic(managebtn);
                        setText(null);
                    }
                }
            };

            return cell;
        };
        photocol.setCellFactory(cellFoctory);
        userTable.setItems(userList);
        new FadeIn(userTable).play();

        FilteredList<UserDetails> filteredData = new FilteredList<>(userList, b -> true);
        txtKeyword.textProperty().addListener((observable, oldValue, newValue) -> {
            filteredData.setPredicate(UserDetails -> {

                if(newValue.isEmpty() || newValue.isBlank() || newValue == null){
                    return true;
                }

                String searchKeyword = newValue.toLowerCase();

                if (UserDetails.getFname().toLowerCase().indexOf(searchKeyword) > -1){
                    return true;
                } else if (UserDetails.getLname().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else if (UserDetails.getUser_id().toLowerCase().indexOf(searchKeyword) > -1 ) {
                    return true;
                } else {
                    return false;
                }

            });
        });

        SortedList<UserDetails> sortedData = new SortedList<>(filteredData);
        sortedData.comparatorProperty().bind(userTable.comparatorProperty());
        userTable.setItems(sortedData);
        new FadeIn(userTable).play();

    }

    @FXML
    private void refreshTable() {
        try {
            userList.clear();

            query = "SELECT * FROM public.users";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                userList.add(new UserDetails(
                        resultSet.getString("user_id"),
                        resultSet.getString("fname"),
                        resultSet.getString("lname"),
                        resultSet.getString("phone_num"),
                        resultSet.getString("email"),
                        resultSet.getString("dob"),
                        resultSet.getString("sex"),
                        resultSet.getString("address"),
                        resultSet.getString("password"),
                        resultSet.getString("user_roll")));
                userTable.setItems(userList);
            }

        } catch (SQLException ex) {
//            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    void addUser(ActionEvent event) throws Exception{
        AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("add-user.fxml")));
        borderpane.getChildren().removeAll();
        borderpane.setCenter(view);
        new FadeInRight(view).play();
    }


    @FXML
    void deleteUser(ActionEvent event) {
        if(userTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete User");
            alert.setContentText("Are you sure delete this User");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    userDetails = userTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM public.users WHERE user_id='"+userDetails.getUser_id()+"'";
                    connection = DbConnect.getConnect();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    refreshTable();

                } catch (SQLException ex) {
//                    Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to delete any user, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }


    @FXML
    void editUser(ActionEvent event) throws Exception {

        if(userTable.getSelectionModel().getSelectedItem() != null){
            try {

                userDetails = userTable.getSelectionModel().getSelectedItem();
                UserController.setSelectedUser(userDetails);
                AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("edit-user.fxml")));
                borderpane.getChildren().removeAll();
                borderpane.setCenter(view);
                new FadeInRight(view).play();

            } catch (IOException ex) {
//                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to update any user, First you select the row that you want to update");
            alert.showAndWait();
        }
    }

    @FXML
    void showUserDetails(MouseEvent event) {
        if (event.isPrimaryButtonDown() && event.getClickCount() == 2) {
            if(userTable.getSelectionModel().getSelectedItem() != null){
                try {

                    userDetails = userTable.getSelectionModel().getSelectedItem();
                    UserController.setSelectedUser(userDetails);
                    AnchorPane view = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("view-user.fxml")));
                    borderpane.getChildren().removeAll();
                    borderpane.setCenter(view);
                    new FadeInRight(view).play();

                } catch (IOException ex) {
//                    Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Warning");
                alert.setContentText("If you want to view any user details, First you select the row that you want to view");
                alert.showAndWait();
            }

        }
    }
}
