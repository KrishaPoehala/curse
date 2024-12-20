class Dispose extends Element {
    public Dispose(String name) {

        super(name);
        tNext = Double.MAX_VALUE;
    }

    @Override
    public void inAct(Message m){
        super.outAct();
    }
}