import java.util.Random;
import java.util.Date;

public class EventFlow {
    /**/
    private Date rndTime;
    private Random random;

    void EventFlow(){
        rndTime = new Date();
        SmoApp.logger.fine("EventFlow::Start");
    }

    double getNextSimpleFlow(double pLambda){
       // SmoApp.logger.info("EventFlow::getNextSimpleFlow");
        //rndTime = new Date();
        //random = new Random(rndTime.getTime());
        random = new Random();

        double rndVal = random.nextDouble();

        double result = (-1/pLambda * Math.log(rndVal));
        SmoApp.logger.finest("EventFlow::getNextSimpleFlow result = "+ result);
        return result;
    }

    double getNextExpFlow(double pLambda){
        //rndTime = new Date();
        //random = new Random(rndTime.getTime());
        random = new Random();
        double rndVal = random.nextDouble();

        //double result = (pLambda * Math.exp(-pLambda * rndVal));
        double result = (Math.log(rndVal) / (-1*pLambda));
        SmoApp.logger.finest("EventFlow::getNextExpFlow result = "+ result);
        return result;
    }
}
