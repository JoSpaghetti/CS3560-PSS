// Recurring Task class
class RecurringTask extends Task {
    String startDate; // Start date of the recurring task in YYYY-MM-DD format
    String endDate; // End date of the recurring task in YYYY-MM-DD format
    int frequency; // Frequency of the task 1 - daily 7 - weekly
    // String[] recurringTaskTypes = {"Class", "Study", "Sleep", "Exercise", "Work", "Meal"};

    // Constructor to initialize recurring task properties
    public RecurringTask(String id, String name, String startTime, String type, double duration, String startDate, String endDate, int frequency) {
        super(id, name, startTime, type, duration);
        this.startDate = startDate;
        this.endDate = endDate;
        this.frequency = frequency;
    }

    public String getStartDate(){
        return startDate;
    } 

    public String getEndDate(){
        return endDate;
    } 

    public int getFrequency(){
        return frequency;
    } 

    // Override method to return task type
    @Override
    public TaskType getTaskType() {
        return TaskType.RECURRING;
    }
}
