public class TransactSource extends ActiveObject {

    private int sourceNum;
    private long createdCount;
    /* обработанные и отказанные заявки*/
    private long processedCount;
    private long rejectedCount;

    private double lambda;
    private Buffer buffer;

    /*для сбора статистики*/
    private double timeLastTrCreated;

    /*общее время нахождения заявок в буфере(как обраб, так и отказанных)*/
    private double totalTransactsTimeInBuffer;
    /*общее время обслуживания заявок приборами*/
    private double totalTransactsTimeInDevice;
    /*общее время жизни заявок*/
    private double totalTransactsTimeInModel;

    public TransactSource(int sNum, double pLambda, Buffer pBuffer){

        sourceNum = sNum;
        objectName = "Src " + sourceNum;
        createdCount = 0;
        rejectedCount = 0;
        processedCount = 0;
        lambda = pLambda;
        buffer = pBuffer;
        timeLastTrCreated = -1;
        totalTransactsTimeInBuffer = 0;
        totalTransactsTimeInDevice = 0;
        totalTransactsTimeInModel = 0;

        nextEventTime = SmoApp.simpleFlow.getNextSimpleFlow(lambda);
        SmoApp.logger.info(objectName+ ":: Created. nextEventTime = " + nextEventTime);
    }

    @Override
    void doAction(double pEventTime) {
        SmoApp.logger.fine("Src "+ sourceNum+":: doAction");

        /*генерация заявки*/
        Transact newTransact = new Transact(createdCount, nextEventTime, this);
        createdCount++;

        timeLastTrCreated = nextEventTime;
        //SmoApp.logger.info("nextEventTime " + nextEventTime);
        nextEventTime = nextEventTime + SmoApp.simpleFlow.getNextSimpleFlow(lambda);
        SmoApp.logger.fine(objectName+ ":: nextEventTime "+ nextEventTime);

        SmoApp.printStat.printStepStatistic(getObjectName()+" generated "+ newTransact.getObjectName(),timeLastTrCreated);

        buffer.addToBufferDispatcher(newTransact);
    }

    /*getters & setters */

    public double getTimeLastTrCreated() {
        return timeLastTrCreated;
    }

    public void setTotalTransactsTimeInBuffer(double totalTransactsTimeInBuffer) {
        this.totalTransactsTimeInBuffer = totalTransactsTimeInBuffer;
    }

    public double getTotalTransactsTimeInBuffer() {
        return totalTransactsTimeInBuffer;
    }

    public void setTotalTransactsTimeInDevice(double totalTransactsTimeInDevice) {
        this.totalTransactsTimeInDevice = totalTransactsTimeInDevice;
    }

    public double getTotalTransactsTimeInDevice() {
        return totalTransactsTimeInDevice;
    }

    public void setTotalTransactsTimeInModel(double totalTransactsTimeInModel) {
        this.totalTransactsTimeInModel = totalTransactsTimeInModel;
    }

    public double getTotalTransactsTimeInModel() {
        return totalTransactsTimeInModel;
    }

    public void setRejectedCount(long rejectedCount) {
        this.rejectedCount = rejectedCount;
    }

    public long getRejectedCount() {
        return rejectedCount;
    }

    public void setNextEventTime (double pTime){
        nextEventTime = pTime;
    }

    public int getSourceNum() {
        return sourceNum;
    }

    public long getCreatedCount() {
        return createdCount;
    }

    public void setCreatedCount(int createdCount) {
        this.createdCount = createdCount;
    }

    public long getProcessedCount() {
        return processedCount;
    }

    public void setProcessedCount(long processedCount) {
        this.processedCount = processedCount;
    }
}
