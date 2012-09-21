
/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */

import java.util.ArrayList;
import java.util.Arrays;

public class PredatorPolicyImprovement extends PolicyIterationPart {

    private double VMatrix[][];
    private double policyMatrix[][][];
    private boolean policyStable;
    public static final double doubleEpsilon = 0.00001; // to compare double values with some tolerance

    /**
     * Constructor
     * @param p: Policy Matrix. Randomly initialized if calles by PredatorPolicyIteration
     * @see PredatorPolicyIteration
     */
    public PredatorPolicyImprovement(double p[][][]) {
        policyMatrix = p;
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
    }

    
    /**
     *
     * @param posNrPredator (state as a number)
     * @param posNrPrey (state as a number)
     * @return As an array: the new probabilities for the five actions the predator can make
     *                      (i.e. probability distribution over actions)
     *                      order is: up, right, down, left, still
     */
    private double[] bestActionRow(int posNrPredator, int posNrPrey) {

        double bestValue = Environment.minimumReward;
        ArrayList<Direction> bestActions = new ArrayList<Direction>();
        double bestActionRow[] = new double[Direction.nrMoves];
        Arrays.fill(bestActionRow, 0);
        int[] possiblePositionsNumbers = newPositionsNumbers(posNrPredator);

        for (int i = 0; i < Direction.nrMoves; i++) {

            super.setVMatrix(VMatrix);

            double thisActionValue = getPositionValue(possiblePositionsNumbers[i], posNrPrey);

            if (thisActionValue > bestValue) {
                bestValue = thisActionValue;
                bestActions.clear();
                bestActions.add(Direction.enumValues[i]);
            }
            else if (Math.abs(thisActionValue - bestValue) < doubleEpsilon) {
                bestActions.add(Direction.enumValues[i]);
            }
        }
        int nrBestActions = bestActions.size();
        for (int i = 0; i < nrBestActions; i++) { // fill with probabilities
            bestActionRow[bestActions.get(i).intValue] = 1.0 / nrBestActions;
        }
        return bestActionRow;
    }

    
    /**
     *
     * @param policyOne : policy probability distribution for one state
     * @param policyTwo : policy probability distribution for the same state
     * @return true if the two polcies are the same for that state
     */
    public boolean samePolicies(double policyOne[], double policyTwo[]) {

        for (int i = 0; i < Direction.nrMoves; i++) {

            if (Math.abs(policyOne[i] - policyTwo[i]) > doubleEpsilon) { // check if they have not same probabilities
                //System.out.println(policyOne[i] +  "  vs " + policyTwo[i]);

                return false;
            }
        }
        return true;
    }

    /**
     * Start policy improvement
     */
    public void start() {
        policyStable = true;

        for (int posNrPredator = 0; posNrPredator < Environment.HEIGHT * Environment.WIDTH; posNrPredator++) {
            for (int posNrPrey = 0; posNrPrey < Environment.HEIGHT * Environment.WIDTH; posNrPrey++) {

                if (posNrPredator != posNrPrey) {
                    double givenPolicyActions[] = Arrays.copyOf(policyMatrix[posNrPredator][posNrPrey], Direction.nrMoves);

                    double currentBestActions[] = bestActionRow(posNrPredator, posNrPrey);
                    for (int k = 0; k < Direction.nrMoves; k++) {
                        policyMatrix[posNrPredator][posNrPrey][k] = currentBestActions[k];
                    }


                    if (!samePolicies(givenPolicyActions, currentBestActions)) {
                        policyStable = false;
                        // System.out.println("state for positions [" + getPosition(posNrPredator).getX()+","+ getPosition(posNrPredator).getY() +
                        //           "] and [" + getPosition(posNrPrey).getX()+","+ getPosition(posNrPrey).getY() + "]differ in policy");

                    }
                }
            }
        }
        if (!policyStable) {
            System.out.println("Policy improver says: Policy not stable yet");
        }

    }

    public boolean isFinished() {
        return policyStable;
    }

    @Override
    public void setVMatrix(double[][] v) {
        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            for (int j = 0; j < Environment.HEIGHT * Environment.WIDTH; j++) {
                VMatrix[i][j] = v[i][j];
            }
        }
    }

    public double[][][] getPolicy() {
        return policyMatrix;
    }

}
