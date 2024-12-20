class Message {
    double timeIn;
    public int id;
    private static int nextId;
    public boolean isInterupted = false;

    public Message(double timeIn) {
        this.timeIn = timeIn;
        id = nextId++;
    }
}