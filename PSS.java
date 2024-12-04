import java.io.FileWriter;
import java.io.IOException;
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
    TimeValidator timeValidator = new TimeValidator(); //Creates an instance of the date validation program
    String[] recurringTaskTypes = {"Class", "Study", "Sleep", "Exercise", "Work", "Meal"};
    String[] transientTaskTypes = {"Visit", "Shopping", "Appointment"};
    String[] antiTaskTypes = {"Cancellation"};
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
            int typeNum;
            if (task.getId().equals(id)) {
                System.out.println("Editing task: " + task.getName());
                System.out.println("Enter new name:");
                String newName = nameValidation("Enter new task name:");

                /*
                System.out.println("Enter new start time (HH:mm):");
                String newStartTime = scanner.nextLine();

                 */

                String newStartTime = timeValidator.hourValidator("Enter new start time (HH.mm):");

                /*/
                System.out.println("Enter new duration (in minutes):");
                int newDuration = Integer.parseInt(scanner.nextLine());

                 */
                double newDuration = timeValidator.durationValidator("Enter new duration (HH:mm):");

                // Method when the task is a recurring task
                if (task instanceof RecurringTask) {
                    // Additional input
                    System.out.println("\n[1] Class\n[2] Study\n[3] Sleep\n[4] Exercise\n[5] Work\n[6] Meal\nEnter task category:");
                    typeNum = Integer.parseInt(scanner.nextLine());

                    /*
                    System.out.println("Enter new start date (YYYY-MM-DD):");
                    String newStartDate = scanner.nextLine();
                    */
                    String newStartDate = timeValidator.dateValidator("Enter new start date (YYYY-MM-DD):");
                    /*
                    System.out.println("Enter new end date (YYYY-MM-DD):");
                    String newEndDate = scanner.nextLine();

                     */
                    String newEndDate = timeValidator.dateValidator("Enter new end date (YYYY-MM-DD):");
                    tasks.remove(task); // Remove old task
                    addTask(new RecurringTask(task.getId(), newName, newStartTime, getType("recurring", typeNum), newDuration, newStartDate, newEndDate )); // Add updated recurring task
                } else {
                    tasks.remove(task); // Remove old task
                    if (task instanceof TransientTask) {
                        System.out.println("\n[1] Visit\n[2] Shopping\n[3] Appointment\nEnter task category:");
                        typeNum = Integer.parseInt(scanner.nextLine());
                        /*
                        System.out.println("Enter new date (YYYY-MM-DD):");
                        String newDate = scanner.nextLine();

                         */
                        String newDate = timeValidator.dateValidator("Enter new date (YYYY-MM-DD):");

                        addTask(new TransientTask(task.getId(), newName, newStartTime, getType("transient", typeNum), newDuration, newDate)); // Add updated transient task
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
                String taskDetails = "Task ID: " + task.getId() + ", Name: " + task.getName() + ", Type: " + task.getType() + ", Start Time: " + task.startTime + ", Duration: " + task.duration + " minutes, Type: " + task.getTaskType();
                if (task instanceof TransientTask) {
                    taskDetails += ", Date: " + ((TransientTask) task).date; // Add date for transient tasks
                }
                else if (task instanceof RecurringTask) {
                    taskDetails += ", Start Date: " + ((RecurringTask) task).startDate + ", End Date: " + ((RecurringTask) task).endDate; // Add dates for recurring tasks
                }
                System.out.println(taskDetails); // Display task details
            }
        }
    }

    // returns the task category/type when given the corresponding number
    // used for info to initalize a task
    public String getType(String taskType, int taskNum){
        if(taskType.equals("recurring")){ // recurring task types
            return recurringTaskTypes[taskNum-1];
        }
        if(taskType.equals("transient")){ // transient task types
            return transientTaskTypes[taskNum-1];
        }
        else{ // anti task types
            return antiTaskTypes[0];
        }
    }

    // creates tasks from a file
    // work in progress
    public void readFromFile(String fileName){
        // try catch to make sure the file exists
        try{
            // intialize the file
            String name = fileName + ".json";
            File inputFile = new File(name);
            Scanner reader = new Scanner(inputFile);

            // pull all the info from the JSON file
            StringBuilder jsonContent = new StringBuilder();
            while (reader.hasNextLine()) {
                jsonContent.append(reader.nextLine());
            }

            // creates the patterns to parse the key value pair correctly
            String regex = "\"(.*?)\"\\s*:\\s*(\"(.*?)\"|\\d+|true|false|null)";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(jsonContent.toString());

            //String tempid;
            String taskName;
            String curTaskType;
            String startTime;
            int duration;
            String startDate;
            String endDate;
            String frequency;

        // FIX DURATION LATER (NEEDS TO BE FLOAT POINT)
            while (matcher.find()) {
                //String taskName = matcher.group(1);
                taskName = matcher.group(2);
                matcher.find();
                curTaskType = matcher.group(2).trim();

            // check which type of task it is using the task types
                if(curTaskType.matches(".*Cancellation.*")){
                    matcher.find();
                    startDate = matcher.group(2);
                    matcher.find();
                    startTime = matcher.group(2);
                    matcher.find();
                    duration = Integer.parseInt(matcher.group(2));
            // FIX LATER // MAKE ANTITASK FIND RECURRING TASK BASED ON TIME & DATE
                    RecurringTask recurringTask = null;
                    for (Task task : tasks) {
                        if (task instanceof RecurringTask && task.getStartTime().equals(startTime)) {
                            recurringTask = (RecurringTask) task; // Find the recurring task
                            break;
                        }
                    }
                    addTask(new AntiTask(generateUniqueId(), taskName, startTime, "Cancelation", duration, recurringTask));
                } 
                else if(curTaskType.matches(".*Visit.*") || curTaskType.matches(".*Shopping.*") || curTaskType.matches(".*Appointment.*")){
                    matcher.find();
                    startDate = matcher.group(2);
                    matcher.find();
                    startTime = matcher.group(2);
                    matcher.find();
                    duration = Integer.parseInt(matcher.group(2));
                    addTask(new TransientTask(generateUniqueId(), taskName, startTime, curTaskType, duration, startDate));
                } 
                else{
                    for (String recurringTaskType : recurringTaskTypes) {
                        if (recurringTaskType.equals(curTaskType)) {
                            break;
                        }
                    }
                    matcher.find();
                    startDate = matcher.group(2);
                    matcher.find();
                    startTime = matcher.group(2);
                    matcher.find();
                    duration = Integer.parseInt(matcher.group(2));
                    matcher.find();
                    endDate = matcher.group(2);
                    matcher.find();
                    frequency = matcher.group(2);
                    addTask(new RecurringTask(generateUniqueId(), taskName, startTime, curTaskType, duration, startDate, endDate));
                }
                //readTasks();
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
                }
                else if (task instanceof RecurringTask) {
                    taskDetails += ", Start Date: " + ((RecurringTask) task).startDate + ", End Date: " + ((RecurringTask) task).endDate; // Add dates for recurring tasks
                }
                System.out.println(taskDetails); // Display found task details
                return; // Exit after finding the task
            }
        }
        System.out.println("Task not found."); // If task ID does not match
    }

    public void showSchedule(){
        String jsonFormat; //used to recreate JSON structure
        String taskType; //used to store task type
        for (Task task: tasks) {
            taskType = String.valueOf(task.getType());

            //use string format to format JSON output
            if (task instanceof RecurringTask) { //need frequency
                jsonFormat = String.format("\n \"ID\": %s, \"Name\": %s \"Task Type\": %s, \"Start Date\": %s, \"Start Time\": %s, \"Duration\": %s, \"End Date\": %s, \"Frequency\": %s",
                        task.getId(), task.getName(), taskType, ((RecurringTask) task).startDate, task.startTime, task.duration, ((RecurringTask) task).endDate, task.startTime);
            } else if (task instanceof TransientTask) {
                jsonFormat = String.format("\n \"ID\": %s, \"Name\": %s, \"Task Type\": %s, \"Date\": %s, \"Start Time\": %s, \"Duration\": %s",
                        task.getId(), task.getName(), taskType, ((TransientTask) task).date, task.startTime, task.duration);
            } else if (task instanceof AntiTask) { //need date function
                jsonFormat = String.format("\n \"ID\": %s, \"Name\": %s, \"Task Type\": %s, \"Date\": %s, \"Start Time\": %s, \"Duration\": %s",
                        task.getId(), task.getName(), taskType, task.duration, task.startTime, task.duration);
            } else {
                jsonFormat = "Error";
            }
            System.out.print(jsonFormat);
        }
    }

    public String nameValidation (String promptUser) {
        boolean nameValid = false; //used to break from while loop
        String name = "";
        while (!nameValid) {
            System.out.print(promptUser);
            name = scanner.nextLine();
            for (Task task: tasks) {
                if (task.getName().equals(name)) { //if the task name already exists, it enters the loop
                    System.out.print("Task Name Already Exists, try again!");
                    nameValid = false;
                } else {
                    nameValid=true;
                }
            }
            if (tasks.size() == 0) {
                nameValid = true;
            }
        }
        return name;
    }
    /*
    public dateOverlapValidator() {

    }
    */

    public void writeToFile (String fileSource) throws IOException {
        try {
            FileWriter writer = new FileWriter(fileSource + ".json");
            String jsonFormat; //used to recreate JSON structure
            String taskType; //used to store task type
            writer.write("[\n");
            int count = 0;
            for (Task task: tasks) {
                taskType = String.valueOf(task.getType());

                //use string format to format JSON output
                if (task instanceof RecurringTask) { //need frequency
                    jsonFormat = String.format("\t{\n \t\t\"ID\": \"%s\", \n\t\t\"Name\": \"%s\", \n\t\t\"Task Type\": \"%s\", \n\t\t\"Start Date\": \"%s\", \n\t\t\"Start Time\": \"%s\", \n\t\t\"Duration\": \"%s\", \n\t\t\"End Date\": \"%s\", \n\t\t\"Frequency\": \"%s\" \n\t}",
                            task.getId(),task.getName(),task.getType(), ((RecurringTask) task).startDate, task.startTime,task.duration, ((RecurringTask) task).endDate, task.startTime );
                }
                else if (task instanceof TransientTask) {
                    jsonFormat = String.format("\t{\n \t\t\"ID\": \"%s\", \n\t\t\"Name\": \"%s\", \n\t\t\"Task Type\": \"%s\", \n\t\t\"Date\": \"%s\", \n\t\t\"Start Time\": \"%s\", \n\t\t\"Duration\": \"%s\" \n\t}",
                            task.getId(),task.getName(),taskType,((TransientTask) task).date, task.startTime, task.duration);
                }
                else if (task instanceof AntiTask) { //need date function
                    jsonFormat = String.format("\t{\n \t\t\"ID\": \"%s\", \n\t\t\"Name\": \"%s\", \n\t\t\"Task Type\": \"%s\", \n\t\t\"Date\": \"%s\", \n\t\t\"Start Time\": \"%s\", \n\t\t\"Duration\": \"%s\" \n\t}",
                            task.getId(),task.getName(),taskType, task.duration, task.startTime,task.duration );
                }
                else {
                    jsonFormat = "Null";
                }
                //adds "," at the end of every json block
                if (++count == tasks.size() ) {
                    jsonFormat += "\n";
                } else {
                    jsonFormat += ",\n";
                }
                writer.write(jsonFormat);
            }
            writer.write("]");
            writer.close();//closes file to stop memory leak issues

        } catch (Exception ex) {//if IO exception is thrown, stack trace is posted
            ex.getStackTrace();
        }
    }
}
