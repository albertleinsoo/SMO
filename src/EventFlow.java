import java.util.Random;
import java.util.Date;

//todo закон для device

public class EventFlow {
    /**/
    private Date rndTime;
    private Random random;

    void EventFlow(){
        rndTime = new Date();
        SmoApp.logger.info("EventFlow::Start");
    }

    double getNextSimpleFlow(double pLambda){
       // SmoApp.logger.info("EventFlow::getNextSimpleFlow");
        rndTime = new Date();
        random = new Random(rndTime.getTime());

        double rndVal = random.nextDouble();

        double result = (-1/pLambda * Math.log(rndVal));
        SmoApp.logger.finest("EventFlow::getNextSimpleFlow result = "+ result);
        return result;
    }

    double getNextExpFlow(double pLambda){
        rndTime = new Date();
        random = new Random(rndTime.getTime());

        double rndVal = random.nextDouble();

        double result = (pLambda * Math.exp(-pLambda * rndVal));
        SmoApp.logger.finest("EventFlow::getNextExpFlow result = "+ result);
        return result;
    }
}
