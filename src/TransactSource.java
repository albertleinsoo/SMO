public class TransactSource extends ActiveObject {

    private int sourceNum;
    private long createdCount;
    /* обработанные и отказанные заявки*/
    private long processedCount;
    private double lambda;
    private Buffer buffer;

    public TransactSource(int sNum, double pLambda, Buffer pBuffer){

        sourceNum = sNum;
        objectName = "Src " + sourceNum;
        createdCount = 0;
        processedCount = 0;
        lambda = pLambda;
        buffer = pBuffer;

        nextEventTime = SmoApp.simpleFlow.getNextSimpleFlow(lambda);
        SmoApp.logger.info(objectName+ ":: Created. nextEventTime = " + nextEventTime);
    }

    @Override
    void doAction(double pEventTime) {
        SmoApp.logger.info("Src "+ sourceNum+":: doAction");

        /*генерация заявки*/
        Transact newTransact = new Transact(createdCount, nextEventTime, this);
        createdCount++;

        //SmoApp.logger.info("nextEventTime " + nextEventTime);
        nextEventTime = nextEventTime + SmoApp.simpleFlow.getNextSimpleFlow(lambda);
        SmoApp.logger.info(objectName+ ":: nextEventTime "+ nextEventTime);

        buffer.addToBufferDispatcher(newTransact);
    }

    /*getters & setters */

    public int getSourceNum() {
        return sourceNum;
    }

    public void setSourceNum(int sourceNum) {
        this.sourceNum = sourceNum;
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
