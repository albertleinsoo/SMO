import javax.swing.table.DefaultTableModel;
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
        String stepStatText = "Current Time = "+ curTime+ " " +eventName+"\nSource\tNextTime\tStatus\tCreated\tProcessed\tRejected\n";
        Vector<TransactSource> statTransactSourcesVector = statModel.getTransactSourcesVector();
        Vector<Device> statDeviceVector = statModel.getDevicesVector();
        Buffer statBuffer = statModel.getBuffer();

        String curStatus = "Waiting";

        for (int i = 0; i < statTransactSourcesVector.size(); i++){
            if(curTime == statTransactSourcesVector.elementAt(i).getTimeLastTrCreated()){
                curStatus = "Active";
            } else {
                curStatus = "Waiting";
            }
            stepStatText = stepStatText +statTransactSourcesVector.elementAt(i).objectName + "\t"+
                    statTransactSourcesVector.elementAt(i).getNextEventTime() + "\t" + curStatus+ "\t" +
                    statTransactSourcesVector.elementAt(i).getCreatedCount()+ "\t" +
                    statTransactSourcesVector.elementAt(i).getProcessedCount()+ "\t" +
                    statTransactSourcesVector.elementAt(i).getRejectedCount()+ "\t"
            //todo убрать
                    + statTransactSourcesVector.elementAt(i).getTotalTransactsTimeInBuffer()+"\t"+
                    + statTransactSourcesVector.elementAt(i).getTotalTransactsTimeInDevice()+"\t"
                    + statTransactSourcesVector.elementAt(i).getTotalTransactsTimeInModel()+"\n";
        }

        stepStatText = stepStatText +"\nDevice\tNextTime\tStatus\tTransact\tTotalUsedTime\n";


        for (int i =0; i< statDeviceVector.size();i++){
            if (statDeviceVector.elementAt(i).getIsInUse()){
                curStatus = "Used";
            } else {
                curStatus = "Free";
            }
            stepStatText = stepStatText + statDeviceVector.elementAt(i).objectName +"\t"+
                    statDeviceVector.elementAt(i).getNextEventTime() +"\t"+  curStatus+"\t" + statDeviceVector.elementAt(i).getTransactInDevName()+
                    "\t"+statDeviceVector.elementAt(i).getTotalUsedTime()+"\n";
        }

        stepStatText = stepStatText +"\n";
        stepStatText = stepStatText + "Position\tStatus\tTransact\tTimeIn\n";

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

        /*todo тестирование отображения*/
        /*данные для таблиц*/
        Object srcData[][] = new Object[statModel.getSourceCount()][9];
        Object devData[][] = new Object[statModel.getDeviceCount()][6];
        Object bufData[][] = new Object[statModel.getBufferSize()][5];

        /*запись данных для таблицы источников*/
        for ( int i=0; i< statModel.getSourceCount(); i++){
            srcData[i][0] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getObjectName()));
            /*todo нормальное объявление столбцов*/
            /*j = 9, так как у нас 9 столбцов*/
            for (int j = 1; j<9;j++){
                switch (j){

                    case (1):/*NextTime*/
                        srcData[i][j] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getNextEventTime()));
                        break;
                    case (2):/*Status*/
                        if(curTime == statTransactSourcesVector.elementAt(i).getTimeLastTrCreated()){
                            curStatus = "Active";
                        } else {
                            curStatus = "Waiting";
                        }
                        srcData[i][j] = new String(String.valueOf(curStatus));
                        break;
                    case (3):/*Created*/
                        srcData[i][j] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getCreatedCount()));
                        break;
                    case (4): /*Processed*/
                        srcData[i][j] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getProcessedCount()));
                        break;
                    case (5):/*Rejected*/
                        srcData[i][j] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getRejectedCount()));
                        break;
                    case (6):/*total time in buffer*/
                        srcData[i][j] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getTotalTransactsTimeInBuffer()));
                        break;
                    case (7):/*total time in device*/
                        srcData[i][j] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getTotalTransactsTimeInDevice()));
                        break;
                    case (8):/*total model time*/
                        srcData[i][j] = new String(String.valueOf(statTransactSourcesVector.elementAt(i).getTotalTransactsTimeInModel()));
                        break;
                }
            }
        } /*\\ запись данных для таблицы источников*/

        /*запись данных для таблицы приборов*/
        for (int i = 0; i< statModel.getDeviceCount(); i++){
            devData[i][0] = new String(String.valueOf(statDeviceVector.elementAt(i).getObjectName()));
            /*j=5, так как 5 столюцов*/
            for (int j = 1; j<6;j++){
                switch (j){
                    case (1):/*Next Time*/
                        devData[i][j] = new String(String.valueOf(statDeviceVector.elementAt(i).getNextEventTime()));
                        break;
                    case (2):/*Status*/
                        if (statDeviceVector.elementAt(i).getIsInUse()){
                            curStatus = "Used";
                        } else {
                            curStatus = "Free";
                        }
                        devData[i][j] = new String(String.valueOf(curStatus));
                        break;
                    case (3):/*Transact*/
                        devData[i][j] = new String(String.valueOf(statDeviceVector.elementAt(i).getTransactInDevName()));
                        break;
                    case (4):/*Time in use*/
                        devData[i][j] = new String(String.valueOf(statDeviceVector.elementAt(i).getTotalUsedTime()));
                        break;
                    case (5):/*Next to use*/
                        if (statDeviceVector.elementAt(i).getIsNextToUse()){
                            devData[i][j] = new String(String.valueOf("|||||||||"));
                        }
                        break;
                }
            }
        } /*\\запись данных для таблицы приборов*/

        /*запись данных для таблицы буфера*/
        for ( int i=0; i< statModel.getBufferSize(); i++){
            bufData[i][0] = new String(String.valueOf("Buf "+i)); /*position*/
            /*j=4, так как 5 столбца*/
            for (int j = 0; j < 5;j++){
                switch (j){
                    case(1):/*Status*/
                        if (statBuffer.getTransactVector().elementAt(i) == null){
                            bufData[i][j] = new String(String.valueOf("Free"));
                        } else {
                            bufData[i][j] = new String(String.valueOf("Used"));
                        }
                        break;
                    case (2):/*Transact in buffer position*/
                        if (statBuffer.getTransactVector().elementAt(i) != null){
                            bufData[i][j] = new String(String.valueOf(statBuffer.getTransactVector().elementAt(i).getObjectName()));
                        }
                        break;
                    case (3): /*TimeIn*/
                        if (statBuffer.getTransactVector().elementAt(i) != null){
                            bufData[i][j] = new String(String.valueOf(statBuffer.getTransactVector().elementAt(i).getTimeAddToBuffer()));
                        }
                        break;
                    case (4):
                        if (statModel.getBuffer().getNextPosToGet() == i){
                            bufData[i][j] = new String(String.valueOf("|||||||||"));
                        }
                }
            }
        }

        /*Обновление таблиц*/
        /*при изменении столбцов не забыть изменить инициализацию данных*/
        /*Модель для таблици источников*/
        DefaultTableModel testSrcModel = new DefaultTableModel(
                srcData,
                new String[]{"Source", "NextTime", "Status", "Created", "Processed", "Rejected"}
                );
        /*Модель для таблици приборов*/
        DefaultTableModel testDevModel = new DefaultTableModel(
                devData,
                new String[]{"Device","NextTime","Status","Transact","TotalUsedTime", "Next to use"}
        );
        /*Модель для таблици буфера*/
        DefaultTableModel testBufModel = new DefaultTableModel(
                bufData,
                new String[]{"Position","Status","Transact","TimeIn", "Next to get from"}
        );
        /*\обновление тавлиц*/

        //todo Перенести сюда заполнение таблиц. сделать ожидание нажатия.
        if (!SmoApp.checkEndModeling) {
            SmoApp.smoForm.updateTables("Current Time = "+ curTime+ " " +eventName,
                    testSrcModel,testDevModel,testBufModel
            );

            /*todo задержка нужна только для ожидания нажатия*/
            while (!SmoApp.continueStepExecution && !SmoApp.checkEndModeling){
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //SmoApp.smoForm.setVisible(true);
            }

            SmoApp.continueStepExecution = false;
//            try {
//
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
        }

        SmoApp.logger.info(stepStatText + "\n");
    }
}
