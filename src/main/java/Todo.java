import java.util.ArrayList;

public class Todo {

    private String todoItem;
    private static ArrayList<Todo> instances = new ArrayList<>(); // I’m new. When do I get created?


    public Todo(String todoItem) {
        this.todoItem = todoItem;
        instances.add(this);
    }

    public String getTodoItem() {
        return todoItem;
    }

    //return all heroes
    public static ArrayList<Todo> getAllTodo(){
        return instances;
    }

}
