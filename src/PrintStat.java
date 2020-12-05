import java.util.Vector;

public class PrintStat {

    private Model statModel;

    PrintStat(Model pModel){
        statModel = pModel;
    }

    public void printModelStat(){
        SmoApp.logger.info("---------Statistic");

    }

    public void printStepStatistic(String eventName, double curTime){
        SmoApp.logger.info("----Step Statistic");
        String stepStatText = "Current Time = "+ curTime+ " " +eventName+"\nSource\tNextTime\tStatus\n";
        Vector<TransactSource> statTransactSourcesVector = statModel.getTransactSourcesVector();
        Vector<Device> statDeviceVector = statModel.getDevicesVector();
        Buffer statBuffer = statModel.getBuffer();

        String curStatus = "waiting";

        for (int i = 0; i < statTransactSourcesVector.size(); i++){
            if(curTime == statTransactSourcesVector.elementAt(i).timeLastTrCreated){
                curStatus = "Active";
            } else {
                curStatus = "Waiting";
            }
            stepStatText = stepStatText +statTransactSourcesVector.elementAt(i).objectName + "\t"+
                    statTransactSourcesVector.elementAt(i).getNextEventTime() + "\t" + curStatus + "\n";
        }
        stepStatText = stepStatText +"\n";

        curStatus = "Free";
        for (int i =0; i< statDeviceVector.size();i++){
            if (statDeviceVector.elementAt(i).getIsInUse()){
                curStatus = "Used";
            } else {
                curStatus = "Free";
            }
            stepStatText = stepStatText + statDeviceVector.elementAt(i).objectName +"\t"+
                    statDeviceVector.elementAt(i).getNextEventTime() +"\t"+  curStatus+"\n";
        }

        stepStatText = stepStatText +"\n";
        stepStatText = stepStatText + "Position\tStatus\tTransact\tTimeIn\n";
        curStatus = "Free";
        for (int i = 0; i<statBuffer.getBufferSize(); i++){
            if (statBuffer.getTransactVector().elementAt(i) == null){
                curStatus = "Free";
                stepStatText = stepStatText + i +"\t"+ curStatus + "\n";
            } else {
                curStatus = ("Used");
                stepStatText = stepStatText + i +"\t"+ curStatus + "\t"+statBuffer.getTransactVector().elementAt(i).getObjectName() +
                        "\t"+statBuffer.getTransactVector().elementAt(i).getTimeAddToBuffer() + "\n";
            }
        }


        SmoApp.logger.info(stepStatText + "\n");
    }
}
