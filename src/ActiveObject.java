public abstract class ActiveObject {

    protected String objectName;
    protected double nextEventTime;

    abstract void doAction(double pEventTime);

    public String getObjectName() {
        return objectName;
    }

    public double getNextEventTime() {
        return nextEventTime;
    }
}
