package com.taskmanagement.task_management_app.admin.task;

public class TaskDetails {
    private int task_id;
    private String task_name;
    private String description;
    private String start_date;
    private String end_date;
    private String progress;
    private int project_id;

    public TaskDetails(int task_id, String task_name, String description, String start_date, String end_date, String progress, int project_id) {
        this.task_id = task_id;
        this.task_name = task_name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.progress = progress;
        this.project_id = project_id;
    }

    public int getTask_id() {
        return task_id;
    }

    public void setTask_id(int task_id) {
        this.task_id = task_id;
    }

    public String getTask_name() {
        return task_name;
    }

    public void setTask_name(String task_name) {
        this.task_name = task_name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_date() {
        return start_date;
    }

    public void setStart_date(String start_date) {
        this.start_date = start_date;
    }

    public String getEnd_date() {
        return end_date;
    }

    public void setEnd_date(String end_date) {
        this.end_date = end_date;
    }

    public String getProgress() {
        return progress;
    }

    public void setProgress(String progress) {
        this.progress = progress;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }
}
