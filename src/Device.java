public class Device extends ActiveObject{

    private int deviceNum;
    /*статус занятости прибора*/
    private boolean isInUse;
    /*указание для начала поиска следующего свободного прибора*/
    private boolean isNextToUse;
    /*интенсивность работы прибора*/
    private double lambdaDevice;
    /*буфер*/
    private Buffer bufferRef;
    /*обработанные заявки*/
    private long processedTransactCount;
    /*общее время занятости прибора*/
    private double totalUsedTime;

    private Transact transactInDevice;

    public Device(int dNum, double pLambda, Buffer pBufferRef){
        deviceNum = dNum;
        objectName = "Dev " + deviceNum;
        isInUse = false;
        isNextToUse = false;
        transactInDevice = null;
        lambdaDevice = pLambda;
        processedTransactCount = 0;
        totalUsedTime = 0;

        bufferRef = pBufferRef;

        SmoApp.logger.info(objectName + ":: Created");
    }

    @Override
    void doAction(double pEventTime) {
        freeDevice();
        bufferRef.getFromBufferDispatcher();
    }

    public void useDevice(Transact pTransact){
        isInUse = true;
        transactInDevice = pTransact;
        transactInDevice.setTimeAddToDevice(pTransact.getLastEventTime());
        transactInDevice.setTimeOutOfBuffer(pTransact.getLastEventTime());

        /*запись в источник времени нахождения выбранной на обслуживание заявки в буфере*/
        double tmpTimeInBuffer = transactInDevice.getTimeOutOfBuffer() - transactInDevice.getTimeAddToBuffer();
        transactInDevice.getInitialSource().setTotalTransactsTimeInBuffer(
                transactInDevice.getInitialSource().getTotalTransactsTimeInBuffer() + tmpTimeInBuffer);

        double tmpDeltaTime = SmoApp.simpleFlow.getNextExpFlow(lambdaDevice);
        totalUsedTime = totalUsedTime + tmpDeltaTime;
        nextEventTime = transactInDevice.getLastEventTime() + tmpDeltaTime;

        SmoApp.logger.fine(objectName + ":: in use. Transact: "+ transactInDevice.getObjectName() + " nextEventTime "+ nextEventTime);

        SmoApp.printStat.printStepStatistic(getObjectName()+" received "+ pTransact.getObjectName(),pTransact.getTimeAddToDevice());
    }

    public void freeDevice(){
        SmoApp.logger.fine(objectName + ":: is free. EventTime " + nextEventTime);

        isInUse = false;

        transactInDevice.setLastEventTime(nextEventTime);
        transactInDevice.setTimeProcessed(nextEventTime);
        transactInDevice.getInitialSource().setProcessedCount(transactInDevice.getInitialSource().getProcessedCount() + 1);

        /*запись в источник времени нахождения выбранной на обслуживание заявки в приборе*/
        double tmpTimeInDevice = transactInDevice.getTimeProcessed() - transactInDevice.getTimeAddToDevice();
        transactInDevice.getInitialSource().setTotalTransactsTimeInDevice(
                transactInDevice.getInitialSource().getTotalTransactsTimeInDevice() + tmpTimeInDevice);
        /*запись в источник времени нахождения обработанной заявки в системе*/
        double tmpTimeInModel = transactInDevice.getTimeProcessed() - transactInDevice.getTimeCreated();
        transactInDevice.getInitialSource().setTotalTransactsTimeInModel(
                transactInDevice.getInitialSource().getTotalTransactsTimeInModel() + tmpTimeInModel);

        /*прибор свободен*/
        nextEventTime = -1;
        processedTransactCount++;

        SmoApp.printStat.printStepStatistic(getObjectName()+" released "+ transactInDevice.getObjectName(),transactInDevice.getTimeProcessed());

        transactInDevice = null;
    }

    /*getters & setters */

    public String getTransactInDevName(){
        if (transactInDevice != null ){
            return transactInDevice.getObjectName();
        } else
        {
            return "-";
        }

    }

    public double getTotalUsedTime() {
        return totalUsedTime;
    }

    public int getDeviceNum() {
        return deviceNum;
    }

    public void setDeviceNum(int deviceNum) {
        this.deviceNum = deviceNum;
    }

    public void setIsInUse(boolean inUse) {
        isInUse = inUse;
    }

    public boolean getIsInUse(){
        return isInUse;
    }

    public void setNextToUse(boolean nextToUse) {
        isNextToUse = nextToUse;
    }

    public boolean getIsNextToUse(){
        return isNextToUse;
    }

}
