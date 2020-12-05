import java.util.Vector;

public class Model {

    private int sourceCount;
    private int deviceCount;
    private int bufferSize;
    private double sourceLambda;
    private double deviceLambda;
    private long maxTransactCount;

    public Buffer buffer;

    private Vector<TransactSource> transactSourcesVector;
    private Vector<Device> devicesVector;
    private ActiveObject curActiveObject;

    //todo сделать разные интенсивности приборов и источников
    public Model(int srcCnt, int devCnt, int bSize, double pSrcLambda, double pDevLambda, long pMaxTransactCount){
        SmoApp.logger.info("Model::Start");
        sourceCount = srcCnt;
        deviceCount = devCnt;
        bufferSize = bSize;
        sourceLambda  = pSrcLambda;
        deviceLambda = pDevLambda;
        maxTransactCount = pMaxTransactCount;

        transactSourcesVector = new Vector<TransactSource>(sourceCount);
        devicesVector = new Vector<Device>(deviceCount);
        curActiveObject = null;

        /*инициализация буфера*/
        buffer = new Buffer(bufferSize,devicesVector);
        /* инициализация источников*/
        for (int i = 0; i <sourceCount; i++){
            transactSourcesVector.add(new TransactSource(i, pSrcLambda, buffer));
        }

        /*инициализация приборов*/
        for (int i = 0; i < deviceCount; i++){
            devicesVector.add(new Device(i, pDevLambda, buffer));
        }
        /*изначально выбор свободного прибора начинается с первого. далее по кольцу*/
        devicesVector.elementAt(0).setNextToUse(true);

    }

    void runModel() {
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

        }
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