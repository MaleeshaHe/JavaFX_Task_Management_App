package com.taskmanagement.task_management_app.project_manager.task;

import com.jfoenix.controls.JFXButton;
import com.taskmanagement.task_management_app.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

public class TaskControlloer implements Initializable  {
    @FXML
    private JFXButton addNew;

    @FXML
    private JFXButton delete;

    @FXML
    private TableColumn<TaskDetails, String> edatecol;

    @FXML
    private JFXButton edit;

    @FXML
    private TableColumn<TaskDetails, String> idcol;

    @FXML
    private TableColumn<TaskDetails, String> namecol;

    @FXML
    private TableColumn<TaskDetails, String> progresscol;

    @FXML
    private TableView<TaskDetails> taskTable;

    @FXML
    private TableColumn<TaskDetails, String> promanagercol;

    @FXML
    private JFXButton refreshBtn;

    @FXML
    private TableColumn<TaskDetails, String> sdatecol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    TaskDetails taskDetails = null ;

    ObservableList<TaskDetails> taskList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadData();
    }

    @FXML
    private void addNew(){
        try {
            taskDetails = taskTable.getSelectionModel().getSelectedItem();
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("add-task.fxml"));
            Parent root = (Parent) fxmlLoader.load();
            Stage stage = new Stage();
            stage.setTitle("Create a new Task");
            Image image = new Image("images/appIcon.png");
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
        if(taskTable.getSelectionModel().getSelectedItem() != null){
//            try {
//                taskDetails = taskTable.getSelectionModel().getSelectedItem();
//                FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("edit-task.fxml"));
//                Parent root = (Parent) fxmlLoader.load();
//
//                EditProjectControlloer senddata = fxmlLoader.getController();
//                senddata.showInformation(projectDetails.getProject_id(),projectDetails.getProject_name(),projectDetails.getDescription(),projectDetails.getStart_date(),projectDetails.getEnd_date(),projectDetails.getProject_manager_id(),projectDetails.getProgress());
//
//                Stage stage = new Stage();
//                stage.setTitle("Edit Notice");
//                javafx.scene.image.Image image = new Image("images/appIcon.png");
//                stage.getIcons().add(image);
//                stage.resizableProperty().setValue(false);
//                stage.setScene(new Scene(root));
//                stage.show();
//
//            } catch (IOException ex) {
//                //              Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
//            }
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

        if(taskTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete Task");
            alert.setContentText("Are you sure delete this Task?");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    taskDetails = taskTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM public.tasks WHERE task_id='"+taskDetails.getTask_id()+"'";
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
            alert.setContentText("If you want to delete any task, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }

    @FXML
    private void refreshTable() {
        try {
            taskList.clear();

            query = "SELECT * FROM public.tasks, public.projects WHERE public.tasks.project_id = public.projects.project_id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                taskList.add(new TaskDetails(
                        resultSet.getInt("task_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("description"),
                        resultSet.getString("start_date"),
                        resultSet.getString("end_date"),
                        resultSet.getString("progress"),
                        resultSet.getInt("project_id"),
                        resultSet.getString("project_name")
                        ));
                taskTable.setItems(taskList);
            }

        } catch (SQLException ex) {
//            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        idcol.setCellValueFactory(new PropertyValueFactory<>("task_id"));
        namecol.setCellValueFactory(new PropertyValueFactory<>("task_name"));
        promanagercol.setCellValueFactory(new PropertyValueFactory<>("project_name"));
        sdatecol.setCellValueFactory(new PropertyValueFactory<>("start_date"));
        edatecol.setCellValueFactory(new PropertyValueFactory<>("end_date"));
        progresscol.setCellValueFactory(new PropertyValueFactory<>("progress"));
        taskTable.setItems(taskList);
    }
}
