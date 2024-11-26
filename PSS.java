import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.io.File; 
import java.io.FileNotFoundException;  


// Enum for task types
enum TaskType {
    RECURRING, TRANSIENT, ANTI_TASK
}

// Main scheduling tool
public class PSS {
    private List<Task> tasks = new ArrayList<>(); // List to hold all tasks
    private Scanner scanner = new Scanner(System.in); // Scanner for user input
    private int taskCounter = 1; // Counter for generating unique IDs

    // Method to generate a unique ID for tasks
    public String generateUniqueId() {
        return String.format("%03d", taskCounter++);
    }

    public List<Task> getTasks(){
        return tasks;
    }

    // Method to add a task to the list
    public void addTask(Task task) {
        tasks.add(task);
        System.out.println("Task added: " + task.getName() + " with ID: " + task.getId());
    }

    // Method to remove a task by ID
    public void removeTask(String id) {
        tasks.removeIf(task -> task.getId().equals(id));
        System.out.println("Task removed with ID: " + id);
    }

    // Method to edit an existing task
    public void editTask(String id) {
        for (Task task : tasks) {
            // Check for the ID of each task
            if (task.getId().equals(id)) {
                System.out.println("Editing task: " + task.getName());
                System.out.println("Enter new name:");
                String newName = scanner.nextLine();
                System.out.println("Enter new start time (HH:mm):");
                String newStartTime = scanner.nextLine();
                System.out.println("Enter new duration (in minutes):");
                int newDuration = Integer.parseInt(scanner.nextLine());

                // Method when the task is a recurring task
                if (task instanceof RecurringTask) {
                    // Additional input
                    System.out.println("Enter new start date (YYYY-MM-DD):");
                    String newStartDate = scanner.nextLine();
                    System.out.println("Enter new end date (YYYY-MM-DD):");
                    String newEndDate = scanner.nextLine();
                    tasks.remove(task); // Remove old task
                    addTask(new RecurringTask(task.getId(), newName, newStartTime, newDuration, newStartDate, newEndDate )); // Add updated recurring task
                } else {
                    tasks.remove(task); // Remove old task
                    if (task instanceof TransientTask) {
                        System.out.println("Enter new date (YYYY-MM-DD):");
                        String newDate = scanner.nextLine();
                        addTask(new TransientTask(task.getId(), newName, newStartTime, newDuration, newDate)); // Add updated transient task
                    } else if (task instanceof AntiTask) {
                        // Handling anti-task editing would require additional logic
                        System.out.println("Anti-tasks cannot be edited directly.");
                    }
                }
                return; // Exit after editing
            }
        }
        System.out.println("Task not found."); // If task ID does not match
    }

    // Method to read and display all tasks
    public void readTasks() {
        // No tasks to display
        if (tasks.isEmpty()) {
            System.out.println("No tasks available.");
        } else {
            for (Task task : tasks) {
                // Constructing task details for display
                String taskDetails = "Task ID: " + task.getId() + ", Name: " + task.getName() + ", Start Time: " + task.startTime + ", Duration: " + task.duration + " minutes, Type: " + task.getTaskType();
                if (task instanceof TransientTask) {
                    taskDetails += ", Date: " + ((TransientTask) task).date; // Add date for transient tasks
                } else if (task instanceof RecurringTask) {
                    taskDetails += ", Start Date: " + ((RecurringTask) task).startDate + ", End Date: " + ((RecurringTask) task).endDate; // Add dates for recurring tasks
                }
                System.out.println(taskDetails); // Display task details
            }
        }
    }

    // creates tasks from a file
    // work in progress
    public void readFromFile(String fileName){
        try{
            String name = fileName + ".json";
            File inputFile = new File(name);
            Scanner reader = new Scanner(inputFile);

            StringBuilder jsonContent = new StringBuilder();
            while (reader.hasNextLine()) {
                jsonContent.append(reader.nextLine());
            }
            String regex = "\"(.*?)\"\\s*:\\s*(\"(.*?)\"|\\d+|true|false|null)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(jsonContent.toString());

            while (matcher.find()) {
                String key = matcher.group(1);
                String value = matcher.group(2);
                System.out.println("Key: " + key + ", Value: " + value);
            }

            reader.close(); 

        } catch (FileNotFoundException e) {
            System.out.println("Couldn't find a file of that name, please try again.");
        }

    }

    // Method to search for a task by ID
    public void searchTask(String id) {
        for (Task task : tasks) {
            if (task.getId().equals(id)) {
                // Constructing task details for display
                String taskDetails = "Found Task: ID: " + task.getId() + ", Name: " + task.getName() + ", Start Time: " + task.startTime + ", Duration: " + task.duration + " minutes, Type: " + task.getTaskType();
                if (task instanceof TransientTask) {
                    taskDetails += ", Date: " + ((TransientTask) task).date; // Add date for transient tasks
                } else if (task instanceof RecurringTask) {
                    taskDetails += ", Start Date: " + ((RecurringTask) task).startDate + ", End Date: " + ((RecurringTask) task).endDate; // Add dates for recurring tasks
                }
                System.out.println(taskDetails); // Display found task details
                return; // Exit after finding the task
            }
        }
        System.out.println("Task not found."); // If task ID does not match
    }

}
