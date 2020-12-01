import java.util.logging.Logger;

public class Global {
    private static final Logger logger = Logger.getLogger(Global.class.getName());

    public static void main(String[] args) {
        System.out.println("Hello World!");
        logger.info("Start");
        logger.info("Start");
        System.out.println(System.getProperty("java.util.logging.config.file"));
    }
}