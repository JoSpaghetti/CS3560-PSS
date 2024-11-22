import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Enum for task types
enum TaskType {
    RECURRING, TRANSIENT, ANTI_TASK
}

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

// Recurring Task class
class RecurringTask extends Task {
    String startDate; // Start date of the recurring task in YYYY-MM-DD format
    String endDate; // End date of the recurring task in YYYY-MM-DD format

    // Constructor to initialize recurring task properties
    public RecurringTask(String id, String name, String startTime, int duration, String startDate, String endDate) {
        super(id, name, startTime, duration);
        this.startDate = startDate;
        this.endDate = endDate;
    }

    // Override method to return task type
    @Override
    public TaskType getTaskType() {
        return TaskType.RECURRING;
    }
}

// Transient Task class
class TransientTask extends Task {
    String date; // Date of the transient task in YYYY-MM-DD format

    // Constructor to initialize transient task properties
    public TransientTask(String id, String name, String startTime, int duration, String date) {
        super(id, name, startTime, duration);
        this.date = date;
    }

    // Override method to return task type
    @Override
    public TaskType getTaskType() {
        return TaskType.TRANSIENT;
    }
}

// Anti-Task class
class AntiTask extends Task {
    RecurringTask recurringTask; // Reference to the recurring task that this anti-task cancels

    // Constructor to initialize anti-task properties
    public AntiTask(String id, String name, String startTime, int duration, RecurringTask recurringTask) {
        super(id, name, startTime, duration);
        this.recurringTask = recurringTask;
    }

    // Override method to return task type
    @Override
    public TaskType getTaskType() {
        return TaskType.ANTI_TASK;
    }
}

// Main scheduling tool
public class PSS {
    private List<Task> tasks = new ArrayList<>(); // List to hold all tasks
    private Scanner scanner = new Scanner(System.in); // Scanner for user input
    private int taskCounter = 1; // Counter for generating unique IDs

    // Method to generate a unique ID for tasks
    private String generateUniqueId() {
        return String.format("%03d", taskCounter++);
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

    // Main method to run the scheduling program
    public static void main(String[] args) {
        PSS pss = new PSS(); // Create an instance of the scheduling program
        Scanner scanner = new Scanner(System.in); // Scanner for user input
        String command; // Variable to hold user commands

        // Main loop for user interaction
        while (true) {
            System.out.println("Enter command (add, remove, edit, read, search, exit):");
            command = scanner.nextLine(); // Read user command

            switch (command.toLowerCase()) {
                case "add":
                    // Prompt for task details
                    System.out.println("Enter task type (recurring, transient, anti-task):");
                    String type = scanner.nextLine();
                    System.out.println("Enter task name:");
                    String name = scanner.nextLine();
                    System.out.println("Enter start time (HH:mm):");
                    String startTime = scanner.nextLine();
                    System.out.println("Enter duration (in minutes):");
                    int duration = Integer.parseInt(scanner.nextLine());
                    String id = pss.generateUniqueId(); // Generate unique ID for the task

                    // Add task based on type
                    if (type.equalsIgnoreCase("recurring")) {
                        System.out.println("Enter start date (YYYY-MM-DD):");
                        String startDate = scanner.nextLine();
                        System.out.println("Enter end date (YYYY-MM-DD):");
                        String endDate = scanner.nextLine();
                        pss.addTask(new RecurringTask(id, name, startTime, duration, startDate, endDate)); // Add recurring task
                    } else if (type.equalsIgnoreCase("transient")) {
                        System.out.println("Enter date (YYYY-MM-DD):");
                        String date = scanner.nextLine();
                        pss.addTask(new TransientTask(id, name, startTime, duration, date)); // Add transient task
                    } else if (type.equalsIgnoreCase("anti-task")) {
                        // Logic to select a recurring task to cancel
                        System.out.println("Enter the ID of the recurring task to cancel:");
                        String recurringTaskId = scanner.nextLine();
                        RecurringTask recurringTask = null;
                        for (Task task : pss.tasks) {
                            if (task instanceof RecurringTask && task.getId().equals(recurringTaskId)) {
                                recurringTask = (RecurringTask) task; // Find the recurring task
                                break;
                            }
                        }
                        if (recurringTask != null) {
                            pss.addTask(new AntiTask(id, name, startTime, duration, recurringTask)); // Add anti-task
                        } else {
                            System.out.println("Recurring task not found."); // Handle case where recurring task is not found
                        }
                    } else {
                        System.out.println("Invalid task type."); // Handle invalid task type input
                    }
                    break;

                case "remove":
                    System.out.println("Enter task ID to remove:");
                    String removeId = scanner.nextLine();
                    pss.removeTask(removeId); // Remove task by ID
                    break;

                case "edit":
                    System.out.println("Enter task ID to edit:");
                    String editId = scanner.nextLine();
                    pss.editTask(editId); // Edit task by ID
                    break;

                case "read":
                    pss.readTasks(); // Read and display all tasks
                    break;

                case "search":
                    System.out.println("Enter task ID to search:");
                    String searchId = scanner.nextLine();
                    pss.searchTask(searchId); // Search for task by ID
                    break;

                case "exit":
                    System.out.println("Exiting the program."); // Exit message
                    scanner.close(); // Close the scanner
                    return; // Exit the program

                default:
                    System.out.println("Invalid command."); // Handle invalid command input
            }
        }
    }
}
