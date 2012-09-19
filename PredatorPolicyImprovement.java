import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;


public class PredatorPolicyImprovement {
	
	private double VMatrix[][];
    private double policyMatrix[][][];
    private boolean policyStable ;


    private final static double chancePreyMoves = 0.2;
    private final static double nrMovesPrey = 4;
    private double discountFactor = 0.8;

    public static final double doubleEpsilon = 0.00001; // to compare double values with some tolerance

    public PredatorPolicyImprovement(double p[][][]) {
    	policyMatrix = p;
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
    }

    /**
     * @param number of a position (index of row/column in VMatrix)
     * @return a Position object with correct x and y coordinates (fields), denoting the corresponding Cartesian position
     */
    Position getPosition(int posNr) {

        int x = posNr % Environment.WIDTH;
        int y = (int)(  (posNr - x) / Environment.WIDTH);
        return new Position(x, y);
    }

    /**
     * @param Position object with correct x and y coordinates (fields), denoting a Cartesian position
     * @return the corresponding number of that position (index of row/column in VMatrix)
     */
    int getPosNr(Position position) {

        return position.getY() * Environment.WIDTH + position.getX();
    }

    private double[] chancesPositionsPrey(int posPredator, int posPrey, int[] positionsPrey) {

    	if(posPrey == posPredator) // then chance of standing still = 1
    		return new double[]{0,0,0,0,1}; // up, right, down, left, still

    	else {
	    	//init to default (not next to predator)
	        double[] chanceArray = {chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, // up, right, down, left
                                    1 - chancePreyMoves};// still
	        // find in what direction prey will stand on  predator, if there is one
	        int predatorEqualsIndex = -1;
	        for (int i = 0; i < Direction.nrMoves && predatorEqualsIndex == -1; i++) {
	            if (positionsPrey[i] == posPredator)
	                predatorEqualsIndex = i;
	        }
	        // if prey was next to predator at that direction-index
	        if (predatorEqualsIndex != -1) {
	            for (int i = 0; i < Direction.nrMoves - 1; i++)
	                chanceArray[i] = chancePreyMoves / (nrMovesPrey - 1);
	            chanceArray[predatorEqualsIndex] = 0;
	        }
	        return chanceArray;
    	}
    }

     private double getPositionValue(int posNrPredator, int posNrPrey) {

        int[] newPreyPositions = newPositionsNumbers(posNrPrey);
        double[] chancesPreyPositions = chancesPositionsPrey(posNrPredator, posNrPrey, newPreyPositions);

        double totalValue = 0;
        for (int i = 0; i < chancesPreyPositions.length; i++) {

            totalValue +=   chancesPreyPositions[i] * // P^{a}_{s s'}
                            (	Environment.reward(getPosition(newPreyPositions[i]), getPosition(posNrPredator))  // R^{a}_{s s'}
                                + (discountFactor * VMatrix[posNrPredator][newPreyPositions[i]]) // gamma * V_k{s')
                            );
        }
        return totalValue;
    }



     private int[] newPositionsNumbers(int currentPosNr) {

    	Position currentPos = getPosition(currentPosNr); // convert position-number to Position object
    	int[] positionNumbersArray = new int[Direction.nrMoves];
    	Position[] positionsArray = new Position[Direction.nrMoves];
    	for (int i=0; i < positionsArray.length; i++)
    		positionsArray[i]= new Position(currentPos);

    	// up
    	positionsArray[0].setY(currentPos.getY() == 0 ? Environment.HEIGHT-1 : currentPos.getY()-1);
    	//right
    	positionsArray[1].setX(currentPos.getX() == Environment.WIDTH-1 ? 0 : currentPos.getX()+1);
    	// down
    	positionsArray[2].setY(currentPos.getY() == Environment.HEIGHT-1 ? 0 : currentPos.getY()+1);
    	//left
    	positionsArray[3].setX(currentPos.getX() == 0? Environment.WIDTH-1 : currentPos.getX()-1);

		for (int i=0; i< positionsArray.length; i++) // re-convert found Position objects to position-numbers
			positionNumbersArray[i]=getPosNr(positionsArray[i]);

		return positionNumbersArray;
    }


    private double[] bestActionRow(int posNrPredator, int posNrPrey) {
    	
    	double bestValue = Environment.minimumReward;
    	ArrayList<Direction> bestActions = new ArrayList<Direction>();
        double  bestActionRow[] = new double[Direction.nrMoves] ;
        Arrays.fill(bestActionRow,0);
        int[] possiblePositionsNumbers = newPositionsNumbers(posNrPredator);

    	for(int i=0; i < Direction.nrMoves; i++) {

    		double thisActionValue = getPositionValue(possiblePositionsNumbers[i],posNrPrey);

            if (thisActionValue > bestValue) {
                bestValue = thisActionValue;
                bestActions.add(Direction.enumValues[i]);
            }
            else if (Math.abs(thisActionValue - bestValue) < doubleEpsilon) {
                bestActions.add(Direction.enumValues[i]);
            }
    	}
        int nrBestActions = bestActions.size();
        for (int i=0; i < nrBestActions; i++) { // fill with probabilities
            bestActionRow[bestActions.get(i).intValue] = 1.0 / nrBestActions;
        }
    	return bestActionRow;
    }

    public boolean samePolicies(double policyOne[], double policyTwo[]) {

        for (int i=0; i < Direction.nrMoves; i++) {
            
            if (Math.abs(policyOne[i] - policyTwo[i]) > doubleEpsilon) { // check if they have not same probabilities
              //  System.out.println(policyOne[i] +  "  vs " + policyTwo[i]);
              
                return false;
            }
        }
        return true;
    }
  
    
    public void start() {
    	policyStable = true;
    	

    	for (int posNrPredator = 0; posNrPredator < Environment.HEIGHT *  Environment.WIDTH ; posNrPredator++) {
            for (int posNrPrey = 0; posNrPrey < Environment.HEIGHT *  Environment.WIDTH ; posNrPrey++) {
            	
            	double givenPolicyActions[] =  Arrays.copyOf(policyMatrix[posNrPredator][posNrPrey], Direction.nrMoves);

                double currentBestActions[] = bestActionRow(posNrPredator, posNrPrey);
                for (int k=0; k < Direction.nrMoves; k++)
                        policyMatrix[posNrPredator][posNrPrey][k] = currentBestActions[k];
                
                 
                if (!samePolicies(givenPolicyActions, currentBestActions)) {
                    policyStable = false;
                   // System.out.println("state for positions [" + getPosition(posNrPredator).getX()+","+ getPosition(posNrPredator).getY() +
                          //      "] and [" + getPosition(posNrPrey).getX()+","+ getPosition(posNrPrey).getY() + "]differ in policy");
                   
                }
            }
    	}
         System.out.println();
        if (!policyStable)
            System.out.println("Policy improver says: Policy not stable yet");
    	
    }
    
    public boolean isFinished() {
    	return policyStable;    	
    }
    
    public void setVMatrix(double[][] v) {
    	for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++)
                VMatrix[i][j]=v[i][j];
        }
    }
    
    public double[][][] getPolicy() {
    	return policyMatrix;    	
    }
    
//    public void writePolicyMatrix(String filename)  {
//    	try
//    	{
//            FileWriter fstream = new FileWriter(filename,false);
//            BufferedWriter out = new BufferedWriter(fstream);
//
//            out.write("clear;clc;");
//            out.newLine();
//            for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
//                for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
//                    for (int k=0; k < Direction.nrMoves; k++) {
//                         out.write(String.format("P(%d,%d,:)=[",i+1,j+1));
//                         if (k != 0)
//                            out.write(",");
//
//                        String number;
//                        number = String.format("%.3f", policyMatrix[i][j][k]);
//                        out.write(number.replaceAll(",", "."));
//                    }
//                }
//                out.write("];");
//                out.newLine();
//                out.flush();
//            }
//            out.write("imagesc(C, [ min(min(C)), max(max(C)) ] );");
//            out.write("colormap(gray);");
//            out.write("axis image");
//            out.flush();
//            fstream.close();
//            out.close();
//    	}
//    	catch(IOException e)
//    	{
//    		System.out.println("Error in writeVMatrix(): " + e);
//    	}
//    }

}
