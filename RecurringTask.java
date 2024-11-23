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
