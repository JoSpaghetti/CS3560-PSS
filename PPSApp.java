import java.io.IOException;
import java.util.Scanner;


public class PPSApp{

    public static void main(String[] args) throws IOException {
        PSS pss = new PSS(); // Create an instance of the scheduling program
        TimeValidator timeValidator = new TimeValidator(); //Creates an instance of the date validation program
        Scanner scanner = new Scanner(System.in); // Scanner for user input
        int command; // Variable to hold user commands

        // Main loop for user interaction
        while (true) {
            System.out.print("\n[1] add\n[2] remove\n[3] edit\n[4] read\n[5] search\n[6] write to file\n[7] read from file\n[8] view schedule \n[9] exit\nEnter command from above options: " );
            command = Integer.parseInt(scanner.nextLine()); // Read user command

            switch (command) {
                case 1: //add
                    // Prompt for task details
                    boolean added = false;
                    String name;
                    String startTime;
                    double duration;
                    String id;
                    int typeNum;
                    int frequency;

                    while(!added){
                        System.out.print("\nTask Types:\n[1] recurring\n[2] transient\n[3] anti-task\n[4] back to main menu\nEnter task type: ");
                        int taskType = Integer.parseInt(scanner.nextLine());
                        // Add task based on type
                        if(taskType == 4){
                            added = true;
                        }
                        else if(taskType < 4){
                            name = pss.nameValidation("Enter task name:");

                            startTime = timeValidator.hourValidator("Enter start time (HH:mm):");

                            duration = timeValidator.durationValidator("Enter duration (HH.mm):");
                            
                        // recurring tasks
                            if (taskType == 1) {
                                System.out.println("\n[1] Class\n[2] Study\n[3] Sleep\n[4] Exercise\n[5] Work\n[6] Meal\nEnter task category: ");
                                typeNum = Integer.parseInt(scanner.nextLine()); //test
                                if(typeNum < 7 && typeNum > 0){
                                    boolean isStartBeforeEnd = false;
                                    String startDate = "";
                                    String endDate = "";

                                    System.out.println("Enter frequency: ");
                                    frequency = Integer.parseInt(scanner.nextLine());
                                    
                                    while (!isStartBeforeEnd) {
                                        startDate = timeValidator.dateValidator("Enter start date (YYYY-MM-DD):");
                                        endDate = timeValidator.dateValidator("Enter end date (YYYY-MM-DD):");
                                        

                                        int startBeforeEnd = timeValidator.dateOverlap(startDate, endDate);//validates the start and end times
                                        if (startBeforeEnd == 1) {//check to see if the end date equals the start date
                                            System.out.print("Error: End Date is before Start Date. Please try again\n");
                                        } else if (startBeforeEnd == 2) {
                                            System.out.print("Error: End Date equals Start Date. Please try again\n");
                                        } else if (startBeforeEnd == 0) {
                                            isStartBeforeEnd = true;
                                        }
                                    }
                                    id = pss.generateUniqueId(); // Generate unique ID for the task
                                    pss.addTask(new RecurringTask(id, name, startTime, pss.getType("recurring", typeNum), duration, startDate, endDate, frequency)); // Add recurring task
                                }
                                else{
                                    System.out.println("Please enter a valid task type and try again.");
                                    break;
                                }

                            } 
                            // transient tasks
                            else if (taskType == 2) {
                                System.out.println("\n[1] Visit\n[2] Shopping\n[3] Appointment\nEnter task category:");
                                typeNum = Integer.parseInt(scanner.nextLine());
                                if(typeNum < 4 && typeNum > 0){

                                    String date = timeValidator.dateValidator("Enter date (YYYY-MM-DD):");
                                    id = pss.generateUniqueId(); // Generate unique ID for the task
                                    pss.addTask(new TransientTask(id, name, startTime, pss.getType("transient", typeNum), duration, date)); // Add transient task
                                }
                                else{
                                    System.out.println("Please enter a valid task type and try again.");
                                    break;
                                }
                            } 
                            // anti-tasks
                            else if (taskType == 3) {
                                // Logic to select a recurring task to cancel
                                System.out.println("Enter the ID of the recurring task to cancel:");
                                String recurringTaskId = scanner.nextLine();
                                RecurringTask recurringTask = null;
                                for (Task task : pss.getTasks()) {
                                    if (task instanceof RecurringTask && task.getId().equals(recurringTaskId)) {
                                        recurringTask = (RecurringTask) task; // Find the recurring task
                                        break;
                                    }
                                }
                                id = pss.generateUniqueId(); // Generate unique ID for the task
                                if (recurringTask != null) {
                                    pss.addTask(new AntiTask(id, name, startTime, "Cancellation", duration, recurringTask)); // Add anti-task
                                } else {
                                    System.out.println("Recurring task not found."); // Handle case where recurring task is not found
                                }
                            }
                        
                        // breaks out of the while loop because the task has been added
                        added = true;
                        }
                        // if user enters invalid type
                        else {
                            System.out.println("Invalid task type. Please try again!"); // Handle invalid task type input
                        }
                    }
                    break;

                case 2: //remove
                    System.out.println("Enter task ID to remove:");
                    String removeId = scanner.nextLine();
                    pss.removeTask(removeId); // Remove task by ID
                    break;

                case 3: //edit
                    System.out.println("Enter task ID to edit:");
                    String editId = scanner.nextLine();
                    pss.editTask(editId); // Edit task by ID
                    break;

                case 4: //read
                    pss.readTasks(); // Read and display all tasks
                    break;

                case 5: //search
                    System.out.println("Enter task ID or task name to search:");
                    String searchId = scanner.nextLine();
                    pss.searchTask(searchId); // Search for task by ID
                    break;

                case 6: //write to File
                    System.out.print("Enter file name (no file type needed):");
                    String fileName = scanner.nextLine();
                    String writePeriod;
                    String writeStartDate;
                    boolean isWriteTrue = false;
                    while (!isWriteTrue) {
                        System.out.print("Do you want to write a day, week, or month to a file:");
                        writePeriod = scanner.nextLine().toLowerCase();
                        writeStartDate = timeValidator.dateValidator("What is the start date for your write:"); //checks if the date inputted is valid
                        switch (writePeriod) {
                            case "day" -> {
                                pss.writeToFile(fileName, writeStartDate, 1);
                                isWriteTrue = true;
                            }
                            case "week" -> {
                                pss.writeToFile(fileName, writeStartDate, 7);
                                isWriteTrue = true;
                            }
                            case "month" -> {
                                pss.writeToFile(fileName, writeStartDate, 31);
                                isWriteTrue = true;
                            }
                            default -> System.out.print("The input received is not valid. Please try again");
                        }
                    }
                    System.out.print("File Written\n");
                    break;

                case 7: // read from file
                    System.out.println("Enter file name (no file type needed):");
                    String file = scanner.nextLine();
                    pss.readFromFile(file); // Search for task by ID
                    break;

                case 8: //view schedule
                    System.out.print("-Schedule-");
                    pss.showSchedule();
                    break;

                case 9: //exit
                    System.out.println("Exiting the program."); // Exit message
                    scanner.close(); // Close the scanner
                    return; // Exit the program

                default:
                    System.out.println("Invalid command."); // Handle invalid command input
            }
        }
    }

}