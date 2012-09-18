
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Locale;

public class PredatorPolicyEvaluation implements Agent {

    private final static double chancesPositions[] = {0.2, 0.2, 0.2, 0.2, 0.2}; // up, right, down left, still
    private final static double chancePreyMoves = 0.2;
    private final static double nrMovesPrey = 4;
    private double VMatrix[][];
    private double discountFactor = 0.90;
    private double cutoffValueDiff = 0.1;

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
    private double[] chancesPositionsPrey(int posPredator, int posPrey, int[] positionsPrey) {
    
    	 
    	if(posPrey == posPredator)
    		return new double[]{0,0,0,0,1};
    	else {
	    	//init to default (not next to predator)
	        double[] chanceArray = {chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, // up, right, down, left
	            1 - chancePreyMoves};// still
	
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
    }

    private int[] newPositions(int currentPos) {
    	
    	Position worldPos = getPosition(currentPos);
    	int[] positionArray = new int[chancesPositions.length];
    	Position[] worldPositionArray = new Position[chancesPositions.length];    	
    	for (int i=0; i < worldPositionArray.length; i++)
    		worldPositionArray[i]= new Position(worldPos);
    	
    	// up
    	worldPositionArray[0].setY(worldPos.getY() == 0 ? Environment.HEIGHT-1 : worldPos.getY()-1);
    	//right
    	worldPositionArray[1].setX(worldPos.getX() == Environment.WIDTH-1 ? 0 : worldPos.getX()+1);
    	// down
    	worldPositionArray[2].setY(worldPos.getY() == Environment.HEIGHT-1 ? 0 : worldPos.getY()+1);
    	//left
    	worldPositionArray[3].setX(worldPos.getX() == 0? Environment.WIDTH-1 : worldPos.getX()-1);
    	
    	
		for (int i=0; i< worldPositionArray.length; i++) 
			positionArray[i]=getStateNr(worldPositionArray[i]);
		
		return positionArray;
    }
    
    // --> deze is voor zowel prey als predator goed toch? - Agnes
//    private int[] newPositions(int currentPos) {
//        int[] positionArray = new int[chancesPositions.length];
//
//        //up
//        positionArray[0] = currentPos - Environment.WIDTH < 0
//                ? currentPos + (Environment.HEIGHT * Environment.WIDTH) - Environment.WIDTH
//                : currentPos - Environment.WIDTH;
//        //right
//        positionArray[1] = (currentPos + 1) % Environment.WIDTH == 0
//                ? (currentPos+1) - Environment.WIDTH
//                : currentPos + 1;
//        //left
//        positionArray[2] = currentPos == 0
//                ? Environment.WIDTH
//                : (currentPos - 1) % Environment.WIDTH == Environment.WIDTH - 1
//                ? currentPos + Environment.WIDTH - 1 : currentPos - 1;
//        //down
//        positionArray[3] = currentPos + Environment.WIDTH > (Environment.HEIGHT * Environment.WIDTH -1)
//                ? currentPos - (Environment.HEIGHT * Environment.WIDTH) + Environment.WIDTH
//                : currentPos + Environment.WIDTH;
//        //still
//        positionArray[4] = currentPos;
//
////        System.out.println("newPositions");
////        for (int i=0; i<5; i++)
////            System.out.print(+positionArray[i] + " ");
////        System.out.println();
//
//        return positionArray;
//    }

    private double getPositionValue(int posPredator, int posPrey) {

        //int[] newPreyPositions = newPositionsPrey(posPredator, posPrey);
        int[] newPreyPositions = newPositions(posPrey);
        double[] chancesPreyPositions = chancesPositionsPrey(posPredator, posPrey, newPreyPositions);
        	
        
        double totalValue = 0;
        for (int i = 0; i < 5; i++) {

        	//if (chancesPreyPositions[4]==1) {
          //  	System.out.println(Environment.reward(getPosition(newPreyPositions[i]), getPosition(posPredator)));

        	//}
            totalValue += chancesPreyPositions[i] * // P^{a}_{s s'}
                    (	Environment.reward(getPosition(newPreyPositions[i]), getPosition(posPredator))  // R^{a}_{s s'}
                    	+ (discountFactor * VMatrix[posPredator][newPreyPositions[i]]) // gamma * V_k{s')
                    );

        }
        //if (totalValue>0) {
//        	
//        	for (int i=0; i < chancesPreyPositions.length; i++) 
//        		System.out.print(chancesPreyPositions[i]+" ");
//        	System.out.println();
        	//System.out.println("totalValue: " +totalValue);
        //}
        return totalValue;

    }

    public double calculateValue(int posPredator, int posPrey) {

        double totalPossiblePositionsValue = 0;

        int[] possiblePositions = newPositions(posPredator);

        for (int i = 0; i < chancesPositions.length; i++) {

            totalPossiblePositionsValue += chancesPositions[i] * // pi(s,a)
                    						getPositionValue(possiblePositions[i], posPrey);
           
           // System.out.println("Position value: " + getPositionValue(possiblePositions[i], posPrey));
        }

       // System.out.println("totalPossiblePositionsValue for posPredator "+ posPredator + " (" + getPosition(posPredator).getX() + "," + getPosition(posPredator).getY() + ") : "
       //                     + "and posPrey " + posPrey + " (" + getPosition(posPrey).getX() + "," + getPosition(posPrey).getY() + ") : " + totalPossiblePositionsValue);
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
                    
                    if (i != j)
                    	VMatrix[i][j] = calculateValue(i, j); // calculate new value for state

                  //  System.out.println("VMatrix new value:" + VMatrix[i][j]);
                    maxValueDiff = Math.max(maxValueDiff, Math.abs(VMatrix[i][j] - oldV)); // keep track of maxValueDiff in this iteration
                }
            }

           //System.out.println("maxValueDiff:" + maxValueDiff);
           
        } while (maxValueDiff > cutoffValueDiff); // zolang de grootste updatewaarden groter is dan maxDiff

    }

    public PredatorPolicyEvaluation() {
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
        
        
        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
        	Arrays.fill(VMatrix[i], 0);
        	//VMatrix[i][i]= Environment.reward(getPosition(i), getPosition(i));            
        }
        
        //printVMatrix();
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
	    	FileWriter fstream = new FileWriter("VMatrix.m",false);
	    	BufferedWriter out = new BufferedWriter(fstream);
	    	
	    	out.write("clear;clc;");
	    	out.newLine();
	        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
	        	out.write(String.format("C(%d,:)=[",i+1)); 
	            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
	            	if (j != 0)
	            		out.write(",");
	                String number = String.format("%.3f", VMatrix[i][j]);
	                number = number.replaceAll(",", ".");	         
	                out.write(number);
	            }
	            out.write("];");
	            out.newLine();
	            out.flush();
	        }
	        out.write("imagesc(C)");

	        out.flush();
	        fstream.close();
	        out.close();
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
