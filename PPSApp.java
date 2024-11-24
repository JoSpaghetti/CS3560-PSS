import java.util.Scanner;


public class PPSApp{
    public static void main(String[] args) {
        PSS pss = new PSS(); // Create an instance of the scheduling program
        Scanner scanner = new Scanner(System.in); // Scanner for user input
        int command; // Variable to hold user commands

        // Main loop for user interaction
        while (true) {
            System.out.print("[1] add\n[2] remove\n[3] edit\n[4] read\n[5] search\n[6] exit\nEnter command from above options: " );
            command = Integer.parseInt(scanner.nextLine()); // Read user command

            switch (command) {
                case 1: //add
                    // Prompt for task details
                    boolean added = false;
                    while(!added){
                        System.out.print("\nTask Types:\n[1] recurring\n[2] transient\n[3] anti-task\n[4] back to main menu\nEnter task type: ");
                        int type = Integer.parseInt(scanner.nextLine());
                        String name;
                        String startTime;
                        int duration;
                        String id;

                        // Add task based on type
                        if(type == 4){
                            added = true;
                        }
                        else if(type < 4){
                            System.out.println("Enter task name:");
                            name = scanner.nextLine();
                            System.out.println("Enter start time (HH:mm):");
                            startTime = scanner.nextLine();
                            System.out.println("Enter duration (in minutes):");
                            duration = Integer.parseInt(scanner.nextLine());
                        // recurring tasks
                            if (type == 1) {
                                System.out.println("Enter start date (YYYY-MM-DD):");
                                String startDate = scanner.nextLine();
                                System.out.println("Enter end date (YYYY-MM-DD):");
                                String endDate = scanner.nextLine();
                                id = pss.generateUniqueId(); // Generate unique ID for the task
                                pss.addTask(new RecurringTask(id, name, startTime, duration, startDate, endDate)); // Add recurring task
                            } 
                            // transient tasks
                            else if (type == 2) {
                                System.out.println("Enter date (YYYY-MM-DD):");
                                String date = scanner.nextLine();
                                id = pss.generateUniqueId(); // Generate unique ID for the task
                                pss.addTask(new TransientTask(id, name, startTime, duration, date)); // Add transient task
                            } 
                            // anti-tasks
                            else if (type == 3) {
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
                                    pss.addTask(new AntiTask(id, name, startTime, duration, recurringTask)); // Add anti-task
                                } else {
                                    System.out.println("Recurring task not found."); // Handle case where recurring task is not found
                                }
                            }
                            // reselect
                            else if (type == 4){
                                added = true;
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
                    System.out.println("Enter task ID to search:");
                    String searchId = scanner.nextLine();
                    pss.searchTask(searchId); // Search for task by ID
                    break;

                case 6: //exit
                    System.out.println("Exiting the program."); // Exit message
                    scanner.close(); // Close the scanner
                    return; // Exit the program

                default:
                    System.out.println("Invalid command."); // Handle invalid command input
            }
        }
    }
}