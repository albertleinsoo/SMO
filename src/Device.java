import javax.management.MBeanException;

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

    private Transact transactInDevice;

    public Device(int dNum, double pLambda, Buffer pBufferRef){
        deviceNum = dNum;
        objectName = "Dev " + deviceNum;
        isInUse = false;
        isNextToUse = false;
        transactInDevice = null;
        lambdaDevice = pLambda;

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

        nextEventTime = transactInDevice.getLastEventTime() + SmoApp.simpleFlow.getNextExpFlow(lambdaDevice);
        SmoApp.logger.info(objectName + ":: in use. Transact: "+ transactInDevice.getObjectName() + " nextEventTime "+ nextEventTime);
    }

    public void freeDevice(){
        SmoApp.logger.info(objectName + ":: is free. EventTime " + nextEventTime);

        isInUse = false;

        transactInDevice.setLastEventTime(nextEventTime);
        transactInDevice.setTimeProcessed(nextEventTime);
        transactInDevice.getInitialSource().setProcessedCount(transactInDevice.getInitialSource().getProcessedCount() + 1);
        /*прибор свободен*/
        nextEventTime = -1;

        //todo убить транзакцию. записать статистику
    }

    /*getters & setters */

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
