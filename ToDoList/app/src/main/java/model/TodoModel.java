package model;

import java.io.Serializable;
import java.util.HashMap;


public class TodoModel implements Serializable {


    private  String id;
    private String name;
    private String message;
    private String date;
    private String status;
    private String priority;

    public TodoModel() {

    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }

    public String getStatus()
    {
        return status;
    }
    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    public void setStatus(String status)
    {
        this.status=status;
    }


    public HashMap<String,String> toFirebaseObject() {
        HashMap<String,String> todo =  new HashMap<String,String>();
        todo.put("name", name);
        todo.put("message", message);
        todo.put("date", date);
        todo.put("status", status);
        todo.put("priority",priority);
        todo.put("id",id);

        return todo;
    }

}