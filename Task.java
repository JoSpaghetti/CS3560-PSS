// Base class for Task
abstract class Task {
    String id; // Unique ID for each task
    String name; // Name of the task
    String startTime; // Start time of the task in HH:mm format
    String type; // the type/category of the task
    float duration; // Duration of the task in minutes


    // Constructor to initialize task properties
    public Task(String id, String name, String startTime, String type, float duration) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
        this.type = type;
        this.duration = duration;
    }

    // Getter for ID
    public String getId() {
        return id;
    }

    // Getter for name
    public String getName() {
        return name;
    }

    public String getStartTime() {
        return startTime;
    }

    // returns the task category
    public String getType() {
        return type;
    }

    // Abstract method to get the task type
    public abstract TaskType getTaskType();
}