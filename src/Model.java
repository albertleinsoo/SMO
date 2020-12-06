import java.util.Vector;

public class Model {

    private int sourceCount;
    private int deviceCount;
    private int bufferSize;
    private Vector<Double> sourceLambda;
    private Vector<Double> deviceLambda;
    private long maxTransactCount;
    private double lastModelEventTime;

    public Buffer buffer;

    private Vector<TransactSource> transactSourcesVector;
    private Vector<Device> devicesVector;
    private ActiveObject curActiveObject;

    public Model(int srcCnt, int devCnt, int bSize, Vector<Double> pSrcLambda, Vector<Double> pDevLambda, long pMaxTransactCount){
        SmoApp.logger.info("Model::Start");
        sourceCount = srcCnt;
        deviceCount = devCnt;
        bufferSize = bSize;
        sourceLambda  = pSrcLambda;
        deviceLambda = pDevLambda;
        maxTransactCount = pMaxTransactCount;
        lastModelEventTime = -1;

        transactSourcesVector = new Vector<TransactSource>(sourceCount);
        devicesVector = new Vector<Device>(deviceCount);
        curActiveObject = null;

        /*инициализация буфера*/
        buffer = new Buffer(bufferSize,devicesVector);
        /* инициализация источников*/
        for (int i = 0; i <sourceCount; i++){
            transactSourcesVector.add(new TransactSource(i, pSrcLambda.elementAt(i).doubleValue(), buffer));
        }

        /*инициализация приборов*/
        for (int i = 0; i < deviceCount; i++){
            devicesVector.add(new Device(i, pDevLambda.elementAt(i).doubleValue(), buffer));
        }
        /*изначально выбор свободного прибора начинается с первого. далее по кольцу*/
        devicesVector.elementAt(0).setNextToUse(true);

        SmoApp.logger.info("Model:: SrcsLambda " + pSrcLambda + " DevicesLambda " + pDevLambda);
    }

    void runModel() {

        /**/

        while (!checkStop()) {
            //transactSourcesVector.elementAt(0).doAction(0);
            curActiveObject = transactSourcesVector.elementAt(0);
            double minEventTime = transactSourcesVector.elementAt(0).getNextEventTime();
            for (int i = 1; i < transactSourcesVector.size(); i++) {
                if (transactSourcesVector.elementAt(i).getNextEventTime() < minEventTime) {
                    minEventTime = transactSourcesVector.elementAt(i).getNextEventTime();
                    curActiveObject = transactSourcesVector.elementAt(i);
                }
            }

            for (int i = 0; i < devicesVector.size(); i++) {
                if (devicesVector.elementAt(i).getIsInUse() &&
                        (devicesVector.elementAt(i).getNextEventTime() < minEventTime)) {
                    minEventTime = devicesVector.elementAt(i).getNextEventTime();
                    curActiveObject = devicesVector.elementAt(i);
                }
            }
            curActiveObject.doAction(minEventTime);
            lastModelEventTime = minEventTime;
        }

        /*завершение обслуживания заявок в буфере и приборах после окончания генерации заявок*/
        /*обнуление следующего внемени генерации заявки*/
        for (int i = 0; i < transactSourcesVector.size(); i++) {
            transactSourcesVector.elementAt(i).setNextEventTime(-1);
        }

        SmoApp.logger.fine("Model:: runModel. End of transact generation. Waiting for processing all transacts in devices.");
        SmoApp.printStat.printStepStatistic("Model:: runModel. End of transact generation.", 0);

        double minEventTime = 0;
        while (minEventTime != -1) {
            minEventTime = -1;
            for (int i = 0; i < devicesVector.size(); i++){
                if (devicesVector.elementAt(i).getIsInUse() &&
                        (devicesVector.elementAt(i).getNextEventTime() > minEventTime)){
                    minEventTime = devicesVector.elementAt(i).getNextEventTime();
                }
            }
            for (int i = 0; i < devicesVector.size(); i++) {
                if (devicesVector.elementAt(i).getIsInUse() &&
                        (devicesVector.elementAt(i).getNextEventTime() <= minEventTime)) {

                    minEventTime = devicesVector.elementAt(i).getNextEventTime();
                    curActiveObject = devicesVector.elementAt(i);
                }
            }
            /*не октивируем, если нет активного прибора*/
            if (minEventTime != -1) {
                curActiveObject.doAction(minEventTime);
                lastModelEventTime = minEventTime;
            }
        }

        SmoApp.logger.fine("Model:: runModel. Model End");
        SmoApp.printStat.printStepStatistic("Model:: runModel. Model End", lastModelEventTime);
    }
    /*проверка достижения заданного количества заявок*/
    private boolean checkStop(){
        long totalTransactCount = 1;
        for(int i = 0; i < transactSourcesVector.size(); i++){
            totalTransactCount = totalTransactCount + transactSourcesVector.elementAt(i).getProcessedCount();
        }
        if (totalTransactCount <= maxTransactCount){
            return false;
        } else {
            return true;
        }
    }


    /*getters & setters */
    public Vector<TransactSource> getTransactSourcesVector(){
        return transactSourcesVector;
    }

    public double getLastModelEventTime() {
        return lastModelEventTime;
    }

    public Vector<Device> getDevicesVector(){
        return devicesVector;
    }

    public Buffer getBuffer() {
        return buffer;
    }

    public void setSourceCount(int sourceCount) {
        this.sourceCount = sourceCount;
    }

    public int getSourceCount() {
        return sourceCount;
    }
}