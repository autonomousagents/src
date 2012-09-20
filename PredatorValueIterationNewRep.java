/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.math.BigDecimal;
import java.util.Arrays;
/**
 *
 * @author 10350470
 */
public class PredatorValueIterationNewRep implements Agent{

    //variables new state space repr
    private final static int nrStates = 21;
    private StateRepresentation vStateRep;
    private double v[];
    private double vNew[];
    private static final int nrActions = 5;


    private final static double WAIT = 0.2;
    Position myPos;
    Position startPosition;
    double theta, gamma;

    public PredatorValueIterationNewRep(Position startPos){
        myPos = startPos;
        startPosition=startPos;
        setParams();
        v = new double[nrStates];
        calcV();
        // print table for prey at (5,5)
        printTable();
    }

    private void calcV() {

        double delta ;
        double[] vPerAction= new double [nrActions];
        int nrIterations = 0;
        do {
            delta = 0.0;
            vNew = new double[nrStates];
            for(int i = 0; i<nrStates;i++){
                if(i != 0) {
                    Arrays.fill(vPerAction,0);
                    vPerAction = calcVPerAction(i);
//                    vNew[i][j][k][l] = getMaximum(vPerAction);
//                    double difference = Math.abs(vNew[i][j][k][l]-v[i][j][k][l]);
                    if(difference>delta){
                        delta = difference;
                    }
                } else {
                	vNew[i] = 0.0;
                }
            }
            v = vNew;
            nrIterations++;
        } while(delta>theta);
        System.out.println("nr iterations until convergence = "+nrIterations);
    }

    private double[] calcVPerAction(int state) {
        double [] vPerAction = new double[nrActions];
        Position [] actionsPred = getPostions(state);
        Position [] actionsPrey;
        double [] pResultingStates;
        //v k+1 per action
        double vkP1;
        // for each resulting position of possible actions of the predator
        for(int i = 0; i<nrActions;i++){
            vkP1 = 0.0;
            //for each possible action of the prey determining s'
            actionsPrey = getPostions(new Position(xPrey, yPrey));
            pResultingStates = probabilities(actionsPred[i],actionsPrey);
            for(int j = 0; j<nrActions;j++){
                //V k(s')
                double vk = v[actionsPred[i].getX()][actionsPred[i].getY()][actionsPrey[j].getX()][actionsPrey[j].getY()];
                // calculate v k+1 (s) per action
                vkP1+= pResultingStates[j]*(Environment.reward(actionsPrey[j], actionsPred[i])+ gamma*vk);
            }
            vPerAction[i] = vkP1;
        }
        return vPerAction;
    }

    private double [] probabilities (Position predator, Position [] prey){
        double [] probabilities = {0.05,0.05,0.05,0.05,0.8};
        int onPredator = -1;
        //check for each action if the prey steps on the predator
        for(int i = 0;i<nrActions-1;i++){
            //if the prey steps on the predator set the probability of that state to zero
            if(prey[i].getX()==predator.getX()&&prey[i].getY()==predator.getY()){
                onPredator = i;
                probabilities[i]=0.0;
            }
        }
        //If necessary adjust the resulting probabilities
        if(onPredator != -1){
            for(int i = 0;i<nrActions-1;i++){
                if(i!=onPredator)
                    probabilities[i] = 0.2/3.0;
            }
        }
        if(predator.getX()==prey[nrActions-1].getX()&& predator.getY()==prey[nrActions-1].getY()){
            double[]pr = {0.0,0.0,0.0,0.0,1.0};
            probabilities = pr;
        }
        return probabilities;
    }

    private int[] getPostions(int state){
    	int[] stateSpacePos = StateRepresentation.linearIndexToState(state);
        Position[] pos  = new Position[nrActions];
        
        //Vertical approach
        pos[0] = new Position(p.getX(), (p.getY()+Environment.HEIGHT-1)% Environment.HEIGHT);
        //Horizontal approach
        pos[1] = new Position((p.getX() +Environment.WIDTH+1) % Environment.WIDTH, p.getY());
        //Vertical retreat
        pos[2] = new Position(p.getX(), (p.getY()+Environment.HEIGHT+1)% Environment.HEIGHT);
        //Horizontal retreat
        pos[3] = new Position((p.getX() +Environment.WIDTH-1) % Environment.WIDTH, p.getY());
        //Wait
        pos[4] = new Position(p.getX(),p.getY());
        return pos;
    }
    
    

    @Override
    public void doMove(Position other) {
        Position [] possibleMoves = getPostions(myPos);
        int maxAction = getBestAction(possibleMoves, other);
        myPos = possibleMoves[maxAction];
    }

    @Override
    public Position getPos() {
        return myPos;
    }

    public void restart(){
        myPos = startPosition;
    }

    private void fillV() {        
        Arrays.fill(v, 0.0);
    }

    public void setParams(double theta, double gamma){
            this.theta=theta;
            this.gamma = gamma;
    }

    public void setParams(){
            this.theta= 0.0;
            this.gamma = 0.8;
    }

    private double getMaximum(double [] values){
        double max = values[0];
        for(int i = 1; i<values.length;i++){
            if (values[i]>max){
                max = values[i];
            }
        }
        return max;
    }

    private int getBestAction(Position [] possibleMoves, Position prey){
        int xPrey = prey.getX(),yPrey = prey.getY();
        int maxAction = -1;
        double maxV = 0.0;
        for(int i = 0; i<nrActions;i++){
            int xPred = possibleMoves[i].getX();
            int yPred = possibleMoves[i].getY();
            if(v[xPred][yPred][xPrey][yPrey]>maxV){
                maxAction = i;
            }
        }
        return maxAction;
    }

    public double [][][][] getValues(){
            return v;
    }

    private void printTable() {
        for(int j = 0; j < Environment.WIDTH; j++){
            for(int i = 0; i < Environment.HEIGHT; i++){
                System.out.print(round(v[i][j][5][5],2)+" ");
            }
            System.out.print("\n");
        }
        System.out.print("\n");
    }

    private void printArray(double[] array, String s) {
        System.out.println(s+"\n");
        for(int i = 0; i<array.length;i++){
            System.out.print(round(array[i],2)+" ");
        }
        System.out.print("\n");
    }

  public static double round(double d, int decimalPlace){
    //from: http://www.rgagnon.com/javadetails/java-0016.html
    // see the Javadoc about why we use a String in the constructor
    // http://java.sun.com/j2se/1.5.0/docs/api/java/math/BigDecimal.html#BigDecimal(double)
    BigDecimal bd = new BigDecimal(Double.toString(d));
    bd = bd.setScale(decimalPlace,BigDecimal.ROUND_HALF_UP);
    return bd.doubleValue();
  }

}
