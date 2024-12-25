class Message {
    double timeIn;
    double timeOut;
    public int id;
    private static int nextId;
    public boolean isInterupted = false;
    public String channel;

    public Message(double timeIn) {
        this.timeIn = timeIn;
        this.timeOut = timeIn;
        id = nextId++;
    }
}