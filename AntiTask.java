// Anti-Task class
class AntiTask extends Task {
    RecurringTask recurringTask; // Reference to the recurring task that this anti-task cancels

    // Constructor to initialize anti-task properties
    public AntiTask(String id, String name, String startTime, String type, int duration, RecurringTask recurringTask) {
        super(id, name, startTime, type, duration);
        this.recurringTask = recurringTask;
    }

    // Override method to return task type
    @Override
    public TaskType getTaskType() {
        return TaskType.ANTI_TASK;
    }
}