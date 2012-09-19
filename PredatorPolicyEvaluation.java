
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;




public class PredatorPolicyEvaluation implements Agent {

    //private final static double chancesPositions[] = {0.2, 0.2, 0.2, 0.2, 0.2}; // up, right, down left, still
    private double policyMatrix[][][];
    private final static double chancePreyMoves = 0.2;
    private final static double nrMovesPrey = 4;
    private double VMatrix[][];
    private double discountFactor = 0.8;
    private double cutoffValueDiff = 0.01;


    /**
     * Constructor
     */
    public PredatorPolicyEvaluation() {
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++)
        	Arrays.fill(VMatrix[i], 0);

        policyMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH]
		                                                                   [Direction.nrMoves];

        initRandomPolicy();

        start();
        writeVMatrix("VMatrix.m");

    }

    /**
     * Constructor when policy is given
     */
    public PredatorPolicyEvaluation(double v[][]) {
    	VMatrix = v;
        policyMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH]
                                 [Direction.nrMoves];

        initRandomPolicy();

        //start();
        //writeVMatrix("VMatrix.m");
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

    /**
     *
     * @param posNrPredator : the number (row-index of VMatrix) corresponding to the Cartesian position of the predator in the grid world
     * @param posNrPrey : the number (column-index of VMatrix) corresponding to the Cartesian position of the prey in the grid world
     * @param positionsPrey : an array containing the corresponding numbers of all new Cartesian positions the prey could move to
     * @return an array indicating the corresponding chances (to the array positionsPrey) of the prey moving to that position
     */
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


    /**
     *
     * @param currentPosNr : number corresponding to the Cartesian position of the prey / predator (resp. column- or row-index of VMatrix)
     * @return an array containing the corresponding numbers of all new Cartesian positions the prey / predator could move to
     */
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

    /**
     *
     * @param posNrPredator : the number (row-index of VMatrix) corresponding to the Cartesian position of the predator in the grid world
     * @param posNrPrey : the number (column-index of VMatrix) corresponding to the Cartesian position of the prey in the grid world
     * @return the total expected Value the predator can get while being on the position corresponding to posNrPredator,
     *         considering the movements the prey might make
     */
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


    /**
     *
     * @param posNrPredator :  the number (row-index of VMatrix) corresponding to the Cartesian position of the predator in the grid world
     * @param posNrPrey : the number (column-index of VMatrix) corresponding to the Cartesian position of the prey in the grid world
     * @return the total expected Value the predator can get from doing any possible move from its position given by posNrPredator
     */
    public double calculateValue(int posNrPredator, int posNrPrey) {

        double totalPossiblePositionsValue = 0;
        int[] possiblePositionsNumbers = newPositionsNumbers(posNrPredator);

        for (int i = 0; i < Direction.nrMoves; i++) {

            totalPossiblePositionsValue +=  policyMatrix[posNrPredator][posNrPrey][i]  // pi(s,a)
                    						* getPositionValue(possiblePositionsNumbers[i], posNrPrey);
        }
        return totalPossiblePositionsValue;
    }


    /**
     * Starts policy evaluation
     */
    public void start() {

        double maxValueDiff;
        int nrIterations = 0;

        do {
            maxValueDiff = 0;
            nrIterations++;
            // for every position in VMatrix
            for (int posNrPredator = 0; posNrPredator < Environment.HEIGHT *  Environment.WIDTH ; posNrPredator++) {
                for (int posNrPrey = 0; posNrPrey < Environment.HEIGHT *  Environment.WIDTH ; posNrPrey++) {

                    double oldV = VMatrix[posNrPredator][posNrPrey];

                    if (posNrPredator != posNrPrey)
                    	VMatrix[posNrPredator][posNrPrey] = calculateValue(posNrPredator, posNrPrey); // calculate new value for state

                    maxValueDiff = Math.max(maxValueDiff, Math.abs(VMatrix[posNrPredator][posNrPrey] - oldV)); // keep track of maxValueDiff in this iteration
                }
            }
          //  System.out.format("Policy Evaluation, iteration number: %d; maxValueDiff = %.3f %n", nrIterations, maxValueDiff);

        } while (maxValueDiff > cutoffValueDiff); // zolang de grootste updatewaarden groter is dan maxDiff
    }


    
    
    
    private void initRandomPolicy () {
    	
        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) 
        	for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) 
        		Arrays.fill(policyMatrix[i][j], 0.2);
        
    }

    /**
     *  print the VMatrix values
     */
    public void printVMatrix() {

    	for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) 
    		System.out.format("%7d", j);
    	
        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
        	System.out.format("%7d",i);  System.out.print("  ");
            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++)
                System.out.format("[%.3f]", VMatrix[i][j]);
            System.out.println();
        }
    }

    /**
     * Write a matlab script that plots a colormap of the VMatrix to a file
     * @param filename : filename of that script
     */
    public void writeVMatrix(String filename)  {
    	try
    	{
            FileWriter fstream = new FileWriter(filename,false);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("clear;clc;");
            out.newLine();
            for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
                    out.write(String.format("C(%d,:)=[",i+1));
                for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
                    if (j != 0)
                            out.write(",");

                    String number;
                    if (i == j) // Set reward at the goal states in the "VMatrix" in the script. Makes a more intuitive colormap.
                        number = String.format("%.3f", Environment.reward(getPosition(i), getPosition(j))); 
                    else
                        number = String.format("%.3f", VMatrix[i][j]);
                   
                    out.write(number.replaceAll(",", "."));
                }
                out.write("];");
                out.newLine();
                out.flush();
            }
            out.write("imagesc(C, [ min(min(C)), max(max(C)) ] );");
            out.write("colormap(gray);");
            out.write("axis image");
            out.flush();
            fstream.close();
            out.close();
    	}
    	catch(IOException e)
    	{
    		System.out.println("Error in writeVMatrix(): " + e);
    	}
    }
    
    public double[][] getVMatrix() {
    	
    	return VMatrix;
    	
    }
    
    public void setPolicy(double p[][][]) {
    	for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
            for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
                for (int k=0; k < Direction.nrMoves; k++)
                    policyMatrix[i][j][k] = p[i][j][k];
            }
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
