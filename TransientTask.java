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