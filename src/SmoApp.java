import java.util.logging.Logger;

public class SmoApp {
    /*инициализация логгера*/
    public static final Logger logger = Logger.getLogger(SmoApp.class.getName());
    public static final EventFlow simpleFlow = new EventFlow();

    public static void main(String[] args) {

        logger.info("SMO::Start");
        //System.out.println(System.getProperty("java.util.logging.config.file"));
        /* количество реализаций модели */
        long maxTransactCount = 10; //TODO определить количество реализаций
        /*интенсивность источников*/
        double lambda = 0.1;

        /* Цикл моделирования*/
        //for (long i = 1; i <=realizationCount; i++){
            /* инициализация модели*/
            Model smoModel = new Model(1,1,1, lambda, maxTransactCount);
            smoModel.runModel();


        //} // конец цикла моделирования


    }

}