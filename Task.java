// Base class for Task
abstract class Task {
    String id; // Unique ID for each task
    String name; // Name of the task
    String startTime; // Start time of the task in HH:mm format
    int duration; // Duration of the task in minutes

    // Constructor to initialize task properties
    public Task(String id, String name, String startTime, int duration) {
        this.id = id;
        this.name = name;
        this.startTime = startTime;
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

    // Abstract method to get the task type
    public abstract TaskType getTaskType();
}