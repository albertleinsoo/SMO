import java.util.Iterator;
import java.util.Vector;

public class Buffer extends ActiveObject {

    private int bufferSize;
    //todo добавить список заявок
    private Vector<Transact> transactVector;
    private Vector<Device> deviceVectorPtr;

    public Buffer(int bSize, Vector<Device> devVec){
        bufferSize = bSize;
        objectName = "Buffer";
        deviceVectorPtr = devVec;

        SmoApp.logger.info("Buffer:: Created with "+ bufferSize + " positions");
    }



    public void addToBufferDispatcher(Transact pTransact){
        SmoApp.logger.info("Buffer:: resived "+ pTransact.getInitialSource().getSourceNum()+"."+pTransact.getTransactNum());

        /*есть ли свободный прибор*/
        Device curDev = findFreeDevice();

        if (curDev != null){
            SmoApp.logger.info("Buffer:: transact "+ pTransact.getInitialSource().getSourceNum()+"."+pTransact.getTransactNum() +
                    " sended to device "+ curDev.getObjectName());
            curDev.useDevice(pTransact);

        }
        else{
            //todo поставить в буфер. или отказ
        }

    }

    public void addTransactToDeviceDispatcher(Device deviceToAddTransact){

    }
    private Device findFreeDevice(){
        /*поиск свободного прибора*/
        int i=0;
        while(i < deviceVectorPtr.size()){
            if (!deviceVectorPtr.elementAt(i).getIsInUse()){
                return deviceVectorPtr.elementAt(i);
            }
            i++;
        }

        return null;
    }

    @Override
    void doAction(double pEventTime) {
    }
    /*getters & setters */

    public int getBufferSize() {
        return bufferSize;
    }

    public void setBufferSize(int bufferSize) {
        this.bufferSize = bufferSize;
    }
}
