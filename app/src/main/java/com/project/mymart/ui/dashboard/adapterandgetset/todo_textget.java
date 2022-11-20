package com.project.mymart.ui.dashboard.adapterandgetset;

public class todo_textget {

    String todo_id, todo_name;

    public todo_textget(String todo_id, String todo_name) {
        this.todo_id = todo_id;
        this.todo_name = todo_name;
    }

    public String getTodo_id() {
        return todo_id;
    }

    public void setTodo_id(String todo_id) {
        this.todo_id = todo_id;
    }

    public String getTodo_name() {
        return todo_name;
    }

    public void setTodo_name(String todo_name) {
        this.todo_name = todo_name;
    }
}
