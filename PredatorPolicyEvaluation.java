
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

public class PredatorPolicyEvaluation implements Agent {

    private final static double chancesPositions[] = {0.2, 0.2, 0.2, 0.2, 0.2}; // up, right, down left, still
    private final static double chancePreyMoves = 0.2;
    private final static int nrMovesPrey = 4;
    private double VMatrix[][];
    private double discountFactor = 0.99;
    private double cutoffValueDiff = 0.02;

    /**
     * @param index of state in VMatrix
     * @return Position with correct x and y coordinates (fields)
     */
    Position getPosition(int nr) {

        int x = nr % Environment.WIDTH;
        int y = (int)(  (nr - x) / Environment.WIDTH);
        return new Position(x, y);
    }

    /**
     * @param Position with correct x and y coordinates (fields)
     * @return index of state in VMatrix
     */
    int getStateNr(Position state) {

        return state.getY() * Environment.WIDTH + state.getX();
    }

    // return array always length of chancesPositions.length
    // ORDER IS: // up, right, down left, still
    private double[] chancesPositionsPrey(int posPredator, int[] positionsPrey) {
        //init to default (not next to predator)
        double chanceArray[] = {chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, // up, right, down, left
            1 - (chancePreyMoves / nrMovesPrey)};// still

        // find in what direction prey will stand on  predator, if there is one
        int predatorEqualsIndex = -1;
        for (int i = 0; i < chancesPositions.length && predatorEqualsIndex == -1; i++) {
            if (positionsPrey[i] == posPredator) {
                predatorEqualsIndex = i;
            }
        }

        // prey was next to predator at that direction-index
        if (predatorEqualsIndex != -1) {
            for (int i = 0; i < chancesPositions.length - 1; i++) {
                chanceArray[i] = chancePreyMoves / (nrMovesPrey - 1);
            }

            chanceArray[predatorEqualsIndex] = 0;
        }

        return chanceArray;
    }

    // --> deze is voor zowel prey als predator goed toch? - Agnes
    private int[] newPositions(int currentPos) {
        int[] positionArray = new int[chancesPositions.length];

        //up
        positionArray[0] = currentPos - Environment.WIDTH < 0
                ? currentPos + (Environment.HEIGHT * Environment.WIDTH) - Environment.WIDTH
                : currentPos - Environment.WIDTH;
        //right
        positionArray[1] = (currentPos + 1) % Environment.WIDTH == 0
                ? (currentPos+1) - Environment.WIDTH
                : currentPos + 1;
        //left
        positionArray[2] = currentPos == 0
                ? Environment.WIDTH
                : (currentPos - 1) % Environment.WIDTH == Environment.WIDTH - 1
                ? currentPos + Environment.WIDTH - 1 : currentPos - 1;
        //down
        positionArray[3] = currentPos + Environment.WIDTH > (Environment.HEIGHT * Environment.WIDTH -1)
                ? currentPos - (Environment.HEIGHT * Environment.WIDTH) + Environment.WIDTH
                : currentPos + Environment.WIDTH;
        //still
        positionArray[4] = currentPos;

//        System.out.println("newPositions");
//        for (int i=0; i<5; i++)
//            System.out.print(+positionArray[i] + " ");
//        System.out.println();

        return positionArray;
    }

    private double getPositionValue(int posPredator, int posPrey) {

        //int[] newPreyPositions = newPositionsPrey(posPredator, posPrey);
        int[] newPreyPositions = newPositions(posPrey);
        double[] chancesPreyPositions = chancesPositionsPrey(posPredator, newPreyPositions);

        
        double totalValue = 0;
        for (int i = 0; i < chancesPreyPositions.length; i++) {

            totalValue += chancesPreyPositions[i] * // P^{a}_{s s'}
                    (Environment.reward(getPosition(newPreyPositions[i]), getPosition(posPredator))  // R^{a}_{s s'}
                    + discountFactor * VMatrix[posPredator][newPreyPositions[i]] // gamma * V_k{s')
                    );

        }
        return totalValue;

    }

    public double calculateValue(int posPredator, int posPrey) {

        double totalPossiblePositionsValue = 0;

        int[] possiblePositions = newPositions(posPredator);

        for (int i = 0; i < chancesPositions.length; i++) {

            totalPossiblePositionsValue += chancesPositions[i] * // pi(s,a)
                    						getPositionValue(possiblePositions[i], posPrey);

        }

//        System.out.println("totalPossiblePositionsValue for posPredator "+ posPredator + " (" + getPosition(posPredator).getX() + "," + getPosition(posPredator).getY() + ") : "
//                            + "and posPrey " + posPrey + " (" + getPosition(posPrey).getX() + "," + getPosition(posPrey).getY() + ") : " + totalPossiblePositionsValue);
        return totalPossiblePositionsValue;
    }

    /**
     * Start of policy evaluation
     */
    public void start() {

        double maxValueDiff;

        do {
            maxValueDiff = 0;

            // for every position in VMatrix
            for (int i = 0; i < Environment.HEIGHT *  Environment.WIDTH ; i++) {
                for (int j = 0; j < Environment.HEIGHT *  Environment.WIDTH ; j++) {

                    double oldV = VMatrix[i][j];

                    VMatrix[i][j] = calculateValue(i, j); // calculate new value for state

                    maxValueDiff = Math.max(maxValueDiff, Math.abs(VMatrix[i][j] - oldV)); // keep track of maxValueDiff in this iteration
                }
            }

           System.out.println("maxValueDiff:" + maxValueDiff);
           
        } while (maxValueDiff > cutoffValueDiff); // zolang de grootste updatewaarden groter is dan maxDiff

    }

    public PredatorPolicyEvaluation() {
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
        for (double[] row : VMatrix) {
            Arrays.fill(row, 0);
        }

        start();
        //printVMatrix();
        writeVMatrix();
    }

    // print the VMatrix values
    public void printVMatrix() {
    	
    	for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
    		System.out.format("%7d", j);
    	}
        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
        	System.out.format("%7d",i);  System.out.print("  ");
            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++)
                System.out.format("[%.3f]", VMatrix[i][j]);
            
            System.out.println();
    	
        }
        System.out.println("=====================================");

    }
    
    public void writeVMatrix()  {
    	
    	try 
    	{
	    	FileWriter fstream = new FileWriter("VMatrix.txt",false);
	    	BufferedWriter out = new BufferedWriter(fstream);
	    	
	    	out.write("       ");
	    	for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) 
	    		out.write(String.format("%7d", j));
	    	
	    	out.newLine();
	        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
	        	out.write(String.format("%7d",i));  out.write("  ");
	            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++)
	                out.write(String.format("[%.3f]", VMatrix[i][j]));
	            
	            out.newLine();
	            out.flush();
	        }
    	}
    	catch(IOException e)
    	{
    		System.out.println("Error in writeVMatrix(): " + e);
    	}
    	
    }

    @Override
    public void doMove(Position other) {
        // TODO Auto-generated method stub
    }

    @Override
    public Position getPos() {
        // TODO Auto-generated method stub
        return null;
    }
}
