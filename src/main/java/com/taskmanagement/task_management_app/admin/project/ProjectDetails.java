package com.taskmanagement.task_management_app.admin.project;

public class ProjectDetails {
    private int project_id;
    private String project_name;
    private String description;
    private String start_date;
    private String end_date;
    private String progress;
    private String project_manager_id;

    public ProjectDetails(int project_id, String project_name, String description, String start_date, String end_date, String progress, String project_manager_id) {
        this.project_id = project_id;
        this.project_name = project_name;
        this.description = description;
        this.start_date = start_date;
        this.end_date = end_date;
        this.progress = progress;
        this.project_manager_id = project_manager_id;
    }

    public int getProject_id() {
        return project_id;
    }

    public void setProject_id(int project_id) {
        this.project_id = project_id;
    }

    public String getProject_name() {
        return project_name;
    }

    public void setProject_name(String project_name) {
        this.project_name = project_name;
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

    public String getProject_manager_id() {
        return project_manager_id;
    }

    public void setProject_manager_id(String project_manager_id) {
        this.project_manager_id = project_manager_id;
    }
}
