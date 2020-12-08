import javax.swing.*;
import java.io.*;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Logger;

public class SmoApp {
    /*инициализация логгера*/
    public static final Logger logger = Logger.getLogger(SmoApp.class.getName());
    public static final EventFlow simpleFlow = new EventFlow();
    public static PrintStat printStat;
    public static SmoForm smoForm = new SmoForm("SMO");
    /*для продолжения пошпговой статистики*/
    public static boolean continueStepExecution = false;
    /*Пошаговое отображение, если true. Иначе моделирование без остановки*/
    public static boolean checkEndModeling = false;

    public static void main(String[] args) {
        logger.info("SMO::Start");

        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                createGUI();
            }
        });

        //smoForm = new SmoForm("SMO");

        /* количество реализаций модели */
        long maxTransactCount = 10; //TODO определить количество реализаций

        int bufferSize = 0;

        /*интенсивности источников*/
        Vector<Double> scrLambda = new Vector<Double>();
        /*интенсивности приборов*/
        Vector<Double> devicesLambda = new Vector<Double>();

        try{
            FileReader initialReader = new FileReader("init.txt");
            BufferedReader reader = new BufferedReader(initialReader);
            String elementsCountStr = reader.readLine(); // transSourses, devices, bufferSize
            String sourcesLambdasStr = reader.readLine();
            String devicesLambdasStr = reader.readLine();
            String maxTransactCountStr = reader.readLine();

            /*выделяем количество источников, приборов, размер буфера*/
            Scanner scanner = new Scanner(elementsCountStr);
            /*кол-во ячеек буфера*/
            if (scanner.hasNextInt()){
                bufferSize = scanner.nextInt();
            }

            /*выделяем интенсивности источников*/
            scanner = new Scanner(sourcesLambdasStr);
            while (scanner.hasNextDouble()){
                scrLambda.add(scanner.nextDouble());
            }
            /*выделяем интенсивности приборов*/
            scanner = new Scanner(devicesLambdasStr);
            while (scanner.hasNextDouble()){
                devicesLambda.add(scanner.nextDouble());
            }

            scanner = new Scanner(maxTransactCountStr);
            if (scanner.hasNextInt()){
                maxTransactCount = scanner.nextInt();
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        /* Цикл моделирования*/
        //for (long i = 1; i <=realizationCount; i++){
        /* инициализация модели*/
        Model smoModel = new Model(scrLambda.size(),devicesLambda.size(),bufferSize, scrLambda , devicesLambda, maxTransactCount);
        printStat = new PrintStat(smoModel);
        smoModel.runModel();
        //} // конец цикла моделирования

    }
    private static void createGUI(){
        //smoForm = new SmoForm("SMO");
//        JPanel root = smoForm.getRootPanel();
//        JFrame frame = new JFrame();
//        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        frame.setContentPane(root);
//        frame.pack();
//        frame.setLocationRelativeTo(null);
//        frame.setVisible(true);
    }

}