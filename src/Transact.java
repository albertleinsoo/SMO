public class Transact {
    private String objectName;
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
    /*время конца обработки*/
    private double timeProcessed;
    /*время отказа*/
    private double timeReject;

    /*время последнего события*/
    private double lastEventTime;



    public Transact(long transNum, double createTime,TransactSource pInitialSource){
        transactNum = transNum;
        initialSource = pInitialSource;
        timeCreated = createTime;
        lastEventTime = createTime;
        objectName = "Tr"+initialSource.getSourceNum()+"."+transactNum;

        timeAddToBuffer = -1;
        timeAddToDevice = -1;
        timeProcessed = -1;
        timeReject = -1;

        SmoApp.logger.fine(objectName+":: Created at "+ timeCreated);
    }
    /*getters & setters*/

    public String getObjectName() {
        return objectName;
    }

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

    public void setTimeProcessed(double timeProcessed) {
        this.timeProcessed = timeProcessed;
    }

    public double getTimeProcessed() {
        return timeProcessed;
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
