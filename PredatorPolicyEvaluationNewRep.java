/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */


import java.util.Arrays;

public class PredatorPolicyEvaluationNewRep implements Agent {

    private final static double chancesPositions[] = {0.2, 0.2, 0.2, 0.2, 0.2}; // up, right, down left, still
    private double policyMatrix[][][];
    private StateRepresentation VHolder;
    private double cutoffValueDiff = 0;
    
    private final static int preypos = 0;
    public final static double chancePreyMoves = 0.2;
    public final static double nrMovesPrey = 4;
    public final static double discountFactor = 0.8;
    


    /**
     * Constructor
     */
    public PredatorPolicyEvaluationNewRep() {
        

        policyMatrix = new double[StateRepresentation.stateRepHeight][StateRepresentation.stateRepWidth]
		                             [StateRepresentation.nrActions];
        VHolder = new StateRepresentation();

        start();
//        View.writeVMatrix("VMatrix.m", VMatrix);
        VHolder.printLatexTable();

    }

//    /**
//     * Constructor when policy is given
//     */
//    public PredatorPolicyEvaluation(double v[][]) {
//    	VMatrix = v;
//        policyMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH]
//                                 [Direction.nrMoves];
//
//        initRandomPolicy();
//
//        //start();
//        //writeVMatrix("VMatrix.m");
//    }
    
    

    /**
     *
     * @param posNrPredator :  the number (row-index of VMatrix) corresponding to the Cartesian position of the predator in the grid world
     * @param posNrPrey : the number (column-index of VMatrix) corresponding to the Cartesian position of the prey in the grid world
     * @return the total expected Value the predator can get from doing any possible move from its position given by posNrPredator
     */
    public double calculateValue(int posNrPredator) {

        double totalPossiblePositionsValue = 0;
        int[] possiblePositionsNumbers = newPositionsNumbers(posNrPredator);
        
        for (int i = 0; i < Direction.nrMoves; i++) {

            totalPossiblePositionsValue +=  chancesPositions[i]  // pi(s,a)
                    						* getPositionValue(possiblePositionsNumbers[i]);
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
            // for every position in VHolder state representation
            for (int posNrPredator = 0; posNrPredator < StateRepresentation.nrStates ; posNrPredator++) {

                    double oldV = VHolder.getVvalue(posNrPredator);

                    if (posNrPredator != preypos)
                    	VHolder.setVvalue(posNrPredator, calculateValue(posNrPredator)); // calculate new value for state

                    maxValueDiff = Math.max(maxValueDiff, Math.abs(VHolder.getVvalue(posNrPredator) - oldV)); // keep track of maxValueDiff in this iteration
                
            }
            System.out.format("Policy Evaluation, iteration number: %d; maxValueDiff = %.15f %n", nrIterations, maxValueDiff);

        } while (maxValueDiff > cutoffValueDiff); // zolang de grootste updatewaarden groter is dan maxDiff
        View.writeVMatrix("VMatrix.m", getVMatrix());
    }
    
    private void initRandomPolicy() {
    	
        for (int i=0; i < policyMatrix.length; i++) 
        	for (int j=0; j < policyMatrix[i].length; j++) 
        		Arrays.fill(policyMatrix[i][j], 0.2);
        
    }
    
    public double[][] getVMatrix() {
    	
    	return VHolder.getMatrix();

    }
    
    public void setPolicy(double p[][][]) {
    	for (int i=0; i < policyMatrix.length; i++) {
            for (int j=0; j < policyMatrix[i].length; j++) {
                for (int k=0; k < Direction.nrMoves; k++)
                    policyMatrix[i][j][k] = p[i][j][k];
            }
        }
    }

    public void doMove(Position other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Position getPos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public double[] chancesPositionsPrey(int posPredator, int[] positionsPrey) {

   	if(preypos == posPredator) // then chance of standing still = 1
   		return new double[]{0,0,0,0,1}; // up, right, down, left, still

   	else {
	    	//init to default (not next to predator)
	        double[] chanceArray = {chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, chancePreyMoves / nrMovesPrey, // up, right, down, left
                                   1 - chancePreyMoves};// still
	        // if prey was next to predator at that direction-index
	        if (posPredator == 1) {
	            for (int i = 0; i < Direction.nrMoves - 1; i++)
	                chanceArray[i] = chancePreyMoves / (nrMovesPrey - 1);
	            chanceArray[0] = 0;
	        }
	        return chanceArray;
   	}
   }

   public int[] newPositionsNumbers(int currentPosNr) {

   		int[] positionNumbersArray = new int[Direction.nrMoves];
   		//VerticalApproach //up
   		positionNumbersArray[0] = VHolder.getLinearIndexForAction(currentPosNr, StateRepresentation.Action.VerticalApproach);
   		//HorizontalRetreat //right
   		positionNumbersArray[1] = VHolder.getLinearIndexForAction(currentPosNr, StateRepresentation.Action.HorizontalRetreat);
   		//VerticalRetreat //down
   		positionNumbersArray[2] = VHolder.getLinearIndexForAction(currentPosNr, StateRepresentation.Action.VerticalRetreat);
   		//HorizontalApproach //left
   		positionNumbersArray[3] = VHolder.getLinearIndexForAction(currentPosNr, StateRepresentation.Action.HorizontalApproach);
   		//Wait //still
   		positionNumbersArray[4] = VHolder.getLinearIndexForAction(currentPosNr, StateRepresentation.Action.Wait);

		return positionNumbersArray;
   }

   public double getPositionValue(int posNrPredator) {

       int[] newPreyPositions = newPositionsNumbers(posNrPredator);
       for(int positionNumber: newPreyPositions) if(positionNumber == 0 && posNrPredator != 0) positionNumber = 1;
       double[] chancesPreyPositions = chancesPositionsPrey(posNrPredator, newPreyPositions);

       double totalValue = 0;

       
       for (int i = 0; i < chancesPreyPositions.length; i++) {

           totalValue +=   chancesPreyPositions[i] * // P^{a}_{s s'}
                           (VHolder.getReward(newPreyPositions[i])  // R^{a}_{s s'}
                               + (discountFactor * VHolder.getVvalue(newPreyPositions[i])) // gamma * V_k{s')
                           );
       }
       return totalValue;
   }
}
