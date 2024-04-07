package com.taskmanagement.task_management_app.admin.project;

import com.jfoenix.controls.JFXButton;

import com.taskmanagement.task_management_app.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
public class ProjectControlloer implements Initializable {
    @FXML
    private JFXButton addNew;

    @FXML
    private JFXButton delete;

    @FXML
    private TableColumn<ProjectDetails, String> edatecol;

    @FXML
    private JFXButton edit;

    @FXML
    private TableColumn<ProjectDetails, String> idcol;

    @FXML
    private TableColumn<ProjectDetails, String> namecol;

    @FXML
    private TableColumn<ProjectDetails, String> progresscol;

    @FXML
    private TableView<ProjectDetails> projectTable;

    @FXML
    private TableColumn<ProjectDetails, String> promanagercol;

    @FXML
    private JFXButton refreshBtn;

    @FXML
    private TableColumn<ProjectDetails, String> sdatecol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    ProjectDetails projectDetails = null ;

    ObservableList<ProjectDetails> projectList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    @FXML
    private void addNew(){
        try {
            projectDetails = projectTable.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-project.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create a new Project");
            javafx.scene.image.Image image = new Image("images/appIcon.png");
            stage.getIcons().add(image);
            stage.resizableProperty().setValue(false);
            stage.setScene(new Scene(root));
            stage.show();

        } catch (IOException ex) {
//            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    private void editProject(){
        if(projectTable.getSelectionModel().getSelectedItem() != null){
            try {
                projectDetails = projectTable.getSelectionModel().getSelectedItem();
                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-project.fxml"));
                Parent root = (Parent) fxmlLoader.load();

                EditProjectControlloer senddata = fxmlLoader.getController();
                senddata.showInformation(projectDetails.getProject_id(),projectDetails.getProject_name(),projectDetails.getDescription(),projectDetails.getStart_date(),projectDetails.getEnd_date(),projectDetails.getProject_manager_id(),projectDetails.getProgress());

                Stage stage = new Stage();
                stage.setTitle("Edit Notice");
                javafx.scene.image.Image image = new Image("images/appIcon.png");
                stage.getIcons().add(image);
                stage.resizableProperty().setValue(false);
                stage.setScene(new Scene(root));
                stage.show();

            } catch (IOException ex) {
  //              Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to update any notice, First you select the row that you want to update");
            alert.showAndWait();
        }

    }

    @FXML
    private void deleteProject(){

        if(projectTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Project");
            alert.setContentText("Are you sure delete this Project?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    projectDetails = projectTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM public.projects WHERE project_id='"+projectDetails.getProject_id()+"'";
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
            alert.setContentText("If you want to delete any project, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }

    @FXML
    private void refreshTable() {
        try {
            projectList.clear();

            query = "SELECT * FROM public.projects, public.users WHERE public.projects.project_manager_id = public.users.user_id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                projectList.add(new ProjectDetails(
                        resultSet.getInt("project_id"),
                        resultSet.getString("project_name"),
                        resultSet.getString("description"),
                        resultSet.getString( "project_manager_id")+ " " + resultSet.getString("fname"),
                        resultSet.getString("start_date"),
                        resultSet.getString("end_date"),
                        resultSet.getString("progress")));
                projectTable.setItems(projectList);
            }

        } catch (SQLException ex) {
//            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        idcol.setCellValueFactory(new PropertyValueFactory<>("project_id"));
        namecol.setCellValueFactory(new PropertyValueFactory<>("project_name"));
        promanagercol.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        sdatecol.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        edatecol.setCellValueFactory(new PropertyValueFactory<>("progress"));
        progresscol.setCellValueFactory(new PropertyValueFactory<>("project_manager_id"));
        projectTable.setItems(projectList);
    }
}
