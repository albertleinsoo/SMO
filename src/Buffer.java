import java.util.Iterator;
import java.util.Vector;

public class Buffer extends ActiveObject {

    private int bufferSize;
    /*позиция с которой начинается поиск занятой ячейки*/
    private int nextPosToGet;
    private Vector<Transact> bufferTransactVector;
    private Vector<Device> deviceVectorPtr;

    public Buffer(int bSize, Vector<Device> devVec){
        bufferSize = bSize;
        nextPosToGet = 0;
        objectName = "Buffer";
        deviceVectorPtr = devVec;
        bufferTransactVector = new Vector<Transact>(bSize);
        for (int i = 0; i < bSize; i++){
            bufferTransactVector.add(i,null);
        }

        SmoApp.logger.fine("Buffer:: Created with "+ bufferSize + " positions");
    }



    public void addToBufferDispatcher(Transact pTransact){
        SmoApp.logger.fine("Buffer:: resived "+ pTransact.getObjectName());

        /*есть ли свободный прибор*/
        if (findFreeDevice()){
            addTransactToDeviceDispatcher(pTransact);
        }
        else{
            int freePos = findFreePosition();
            SmoApp.logger.fine("Buffer:: freePosition= "+ freePos);
            if (freePos >= 0){
                bufferTransactVector.setElementAt(pTransact, freePos);
                pTransact.setTimeAddToBuffer(pTransact.getLastEventTime());
                SmoApp.logger.fine("Buffer:: Position= "+freePos+" used by "+pTransact.getObjectName());
            } else{ /*уничтожение */
                int minPos = findMinTimePosition();
                bufferTransactVector.elementAt(minPos).setTimeReject(pTransact.getLastEventTime());
                bufferTransactVector.elementAt(minPos).setLastEventTime(pTransact.getLastEventTime());

                SmoApp.logger.fine("Buffer:: Position= "+minPos+" Reject transact "
                        + bufferTransactVector.elementAt(minPos).getObjectName() + " at time = "+ pTransact.getLastEventTime());

                pTransact.setTimeAddToBuffer(pTransact.getLastEventTime());
                bufferTransactVector.setElementAt(pTransact,minPos);

                SmoApp.logger.fine("Buffer:: Position= "+minPos+" used by "+pTransact.getObjectName()+" at "+ pTransact.getLastEventTime());
            }
        }
    }

    public void getFromBufferDispatcher(){

        Transact curTrans = null;
        boolean posFound = false;
        /*поиск 'по кольцу' от текущей позиции указателя до конца буфера*/
        for(int i = nextPosToGet; i < bufferSize;i++){
            if(bufferTransactVector.elementAt(i) != null){
                curTrans = bufferTransactVector.elementAt(i);
                bufferTransactVector.setElementAt(null,i);

                /*постановка указателя для выбора следующей ячейки*/
                if (i >= bufferSize-1){
                    nextPosToGet = 0;
                } else {
                    nextPosToGet = i + 1;
                }
                SmoApp.logger.fine("Buffer:: Position= "+ i + " released "+curTrans.getObjectName() +
                        " . Next position to search= "+ nextPosToGet);
                posFound = true;
            }
        }

        /*поиск 'по кольцу' от начала буфера до позиции указателя*/
        if (!posFound){
            for (int i = 0; i <= nextPosToGet; i++){
                if(bufferTransactVector.elementAt(i) != null){
                    curTrans = bufferTransactVector.elementAt(i);
                    bufferTransactVector.setElementAt(null,i);

                    /*постановка указателя для выбора следующей ячейки*/
                    if (i >= bufferSize-1){
                        nextPosToGet = 0;
                    } else {
                        nextPosToGet = i + 1;
                    }
                    SmoApp.logger.fine("Buffer:: Position= "+ i + " released "+curTrans.getObjectName() +
                            " . Next position to search= "+ nextPosToGet);
                    posFound = true;
                }
            }
        }

        /*поиск следующего прибора 'по кольцу' */
        if (curTrans != null) {
           addTransactToDeviceDispatcher(curTrans);
        } else {
            SmoApp.logger.fine("Buffer:: no transacts in buffer" + " . Next position to search= "+ nextPosToGet);
        }

    }

    /*выбор следующего прибора для обслуживания
    * pTransact != null*/

    private void addTransactToDeviceDispatcher(Transact pTransact){
        /*поиск метки прибора*/
        int curDevToUseNum = 0;
        for (int i = 0; i < deviceVectorPtr.size(); i++){
            if (deviceVectorPtr.elementAt(i).getIsNextToUse()){
                curDevToUseNum = i;
                break;
            }
        }

        /*поиск 'по кольцу' от текущей позиции указателя до последнего прибора*/
        int newCurDevToUseNum = -1;
        for (int i = curDevToUseNum; i<deviceVectorPtr.size(); i++){
            if (!deviceVectorPtr.elementAt(i).getIsInUse()){
                newCurDevToUseNum = i;
            }
        }

        if (newCurDevToUseNum == -1){
            for (int i = 0; i<curDevToUseNum; i++){
                if (!deviceVectorPtr.elementAt(i).getIsInUse()){
                    newCurDevToUseNum = i;
                }
            }
        }

        deviceVectorPtr.elementAt(newCurDevToUseNum).useDevice(pTransact);
        deviceVectorPtr.elementAt(newCurDevToUseNum).setNextToUse(false);
        SmoApp.logger.fine("Buffer:: "+ pTransact.getObjectName() + " sent to device "+ deviceVectorPtr.elementAt(newCurDevToUseNum).getObjectName());

        SmoApp.logger.fine("Buffer:: newCurDevToUseNum= " + newCurDevToUseNum);
        if (newCurDevToUseNum + 1 >= deviceVectorPtr.size()){
            deviceVectorPtr.elementAt(0).setNextToUse(true);
        } else {
            deviceVectorPtr.elementAt(newCurDevToUseNum + 1).setNextToUse(true);
        }
    }

    /*найти первую свободную ячейку в буфере*/
    private int findFreePosition(){
        for(int i = 0; i < bufferTransactVector.size(); i++){
            if (bufferTransactVector.elementAt(i) == null){
                return i;
            }
        }
        /*свободная ячейка не найдена*/
        return -1;
    }
    private int findMinTimePosition(){
        double minTime = bufferTransactVector.elementAt(0).getLastEventTime();
        int minPos = 0;
        for(int i = 0; i < bufferTransactVector.size(); i++){
            if(minTime < bufferTransactVector.elementAt(i).getLastEventTime()){
                minTime = bufferTransactVector.elementAt(i).getLastEventTime();
                minPos = i;
            }

        }
        return minPos;
    }

    private boolean findFreeDevice(){
        /*поиск свободного прибора*/
        int i=0;
        while(i < deviceVectorPtr.size()){
            if (!deviceVectorPtr.elementAt(i).getIsInUse()){
                return true;
            }
            i++;
        }

        return false;
    }

    @Override
    void doAction(double pEventTime) {
    }
    /*getters & setters */

    public Vector<Transact> getTransactVector(){
        return bufferTransactVector;
    }

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
