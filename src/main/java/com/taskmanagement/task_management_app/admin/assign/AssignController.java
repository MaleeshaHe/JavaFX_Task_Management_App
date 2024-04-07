package com.taskmanagement.task_management_app.admin.assign;

import animatefx.animation.Shake;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;


import com.taskmanagement.task_management_app.admin.task.TaskDetails;
import com.taskmanagement.task_management_app.db_connect.DbConnect;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Label;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Callback;


import java.awt.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.sql.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
public class AssignController implements Initializable {

    @FXML
    private TableView<AssignDetails> assignTable;

    @FXML
    private JFXComboBox<String> comboMember;

    @FXML
    private JFXComboBox<String> comboTask;

    @FXML
    private Label error;

    @FXML
    private TableColumn<AssignDetails, String> memberCol;

    @FXML
    private TableColumn<AssignDetails, String> projectCol;

    @FXML
    private TableColumn<AssignDetails, String> taskCol;

    String query = null;
    Connection connection = null ;
    PreparedStatement preparedStatement = null ;
    ResultSet resultSet = null ;
    AssignDetails assignDetails = null ;

    ObservableList<AssignDetails> assignList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        comboboxDataLoad();
        comboboxDataLoad1();
        loadData();
    }

    private void comboboxDataLoad(){
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM public.users WHERE user_roll='Team Members'";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            ObservableList data = FXCollections.observableArrayList();

            while (resultSet.next()){
                data.add(new String(resultSet.getString("user_id")));
            }
            comboMember.setItems(data);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    private void comboboxDataLoad1(){
        try {
            connection = DbConnect.getConnect();
            query = "SELECT * FROM public.tasks";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            ObservableList data = FXCollections.observableArrayList();

            while (resultSet.next()){
                data.add(new String(resultSet.getString("task_id")));
            }
            comboTask.setItems(data);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    @FXML
    private void refreshTable() {
        try {
            assignList.clear();

            query = "SELECT * FROM public.user_tasks, public.tasks,  public.projects WHERE user_tasks.task_id = tasks.task_id AND projects.project_id = tasks.project_id";
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();


            while (resultSet.next()){
                assignList.add(new AssignDetails(
                        resultSet.getString("user_id"),
                        resultSet.getString("task_name"),
                        resultSet.getString("project_name"),
                        resultSet.getInt("task_id")
                ));
                assignTable.setItems(assignList);
            }

        } catch (SQLException ex) {
//            Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

    @FXML
    private void delete(){

        if(assignTable.getSelectionModel().getSelectedItem() != null){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Delete This");
            alert.setContentText("Are you sure delete this");
            Optional<ButtonType> result = alert.showAndWait();

            if(result.get() == ButtonType.OK){
                try {
                    assignDetails = assignTable.getSelectionModel().getSelectedItem();
                    query = "DELETE FROM public.user_tasks WHERE task_id='"+assignDetails.getTask_id()+"'";
                    connection = DbConnect.getConnect();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    refreshTable();

                } catch (SQLException ex) {
    //                Logger.getLogger(NoticeController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }
        else{
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning");
            alert.setContentText("If you want to delete any Timetable, First you select the row that you want to delete");
            alert.showAndWait();
        }
    }

    public void loadData() {
        connection = DbConnect.getConnect();
        refreshTable();

        projectCol.setCellValueFactory(new PropertyValueFactory<>("project_name"));
        taskCol.setCellValueFactory(new PropertyValueFactory<>("task_name"));
        memberCol.setCellValueFactory(new PropertyValueFactory<>("user_id"));

        assignTable.setItems(assignList);
    }

    public void clear(){
        comboMember.setValue(null);
        comboTask.setValue(null);
    }


    public void assignTask(ActionEvent event) {
        if(comboMember.getValue().length() == 0){
            new Shake(error).play();
            error.setText("Please fill the all Fields");
        }
        else {
            try {
                String task_id = comboTask.getValue();
                String user_id = comboMember.getValue();

                connection = DbConnect.getConnect();
                query = "INSERT INTO public.user_tasks (user_id, task_id) VALUES (?, ?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1,user_id);
                preparedStatement.setInt(2, Integer.parseInt(task_id));
                preparedStatement.executeUpdate();

                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("successfully Assigned");
                alert.setContentText("successfully created new task");
                Optional<ButtonType> result = alert.showAndWait();

                if(result.get() == ButtonType.OK){
                    clear();
                    loadData();
                }

            }catch (Exception e){
                System.out.println(e);
            }
        }

    }
}
