package com.taskmanagement.task_management_app.admin.assign;

public class AssignDetails {
    private String user_id;
    private String task_name;

    private String project_name;

    private int task_id;

    public AssignDetails(String user_id, String task_name, String project_name, int task_id) {
        this.user_id = user_id;
        this.task_name = task_name;
        this.project_name = project_name;
        this.task_id = task_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }
}
