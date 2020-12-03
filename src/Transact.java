public class Transact {

    /*номер заявки*/
    private long transactNum;
    /* номер источника, сгенерировавшего заявку*/
    private TransactSource initialSource;
    /* время генерации*/
    private double timeCreated;
    /*время добавления в буфер*/
    private double timeAddToBuffer;
    /*время добавления в прибор*/
    private  double timeAddToDevice;
    /*время отказа*/
    private double timeReject;

    /*время последнего события*/
    private double lastEventTime;


    public Transact(long transNum, double createTime,TransactSource pInitialSource){
        transactNum = transNum;
        initialSource = pInitialSource;
        timeCreated = createTime;
        lastEventTime = createTime;

        timeAddToBuffer = -1;
        timeAddToDevice = -1;
        timeReject = -1;

        SmoApp.logger.info("Transact "+ initialSource.getSourceNum() +"."+transactNum +":: Created at "+ timeCreated);
    }

    /*getters & setters*/

    public TransactSource getInitialSource() {
        return initialSource;
    }

    public long getTransactNum() {
        return transactNum;
    }

    public void setTransactNum(int transactNum) {
        this.transactNum = transactNum;
    }

    public void setTimeCreated(double timeCreated) {
        this.timeCreated = timeCreated;
    }

    public double getTimeCreated() {
        return timeCreated;
    }

    public double getTimeAddToDevice() {
        return timeAddToDevice;
    }

    public void setTimeAddToDevice(double timeAddToDevice) {
        this.timeAddToDevice = timeAddToDevice;
    }

    public double getTimeAddToBuffer() {
        return timeAddToBuffer;
    }

    public void setTimeAddToBuffer(double timeAddToBuffer) {
        this.timeAddToBuffer = timeAddToBuffer;
    }

    public double getTimeReject() {
        return timeReject;
    }

    public void setTimeReject(double timeReject) {
        this.timeReject = timeReject;
    }

    public void setLastEventTime(double lastEventTime) {
        this.lastEventTime = lastEventTime;
    }

    public double getLastEventTime() {
        return lastEventTime;
    }
}
