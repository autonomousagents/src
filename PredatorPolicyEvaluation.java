/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */

import java.util.Arrays;

public class PredatorPolicyEvaluation extends PolicyIterationPart implements Agent {

    private double policyMatrix[][][];
    private double VMatrix[][];
    private double cutoffValueDiff = 0;

    /**
     * Constructor
     */
    public PredatorPolicyEvaluation() {
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];
        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            Arrays.fill(VMatrix[i], 0);
        }

        policyMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH][Direction.nrMoves];

        initRandomPolicy();

        start();
        View.writeVMatrix("VMatrix.m", VMatrix);
        printVMatrix();

    }

    /**
     * Constructor when Value Matrix is given (e.g. when used in PredatorPolicyIteration)
     * @param v : value matrix
     * @see PredatorPolicyIteration
     */
    public PredatorPolicyEvaluation(double v[][]) {
        VMatrix = v;
        policyMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH][Direction.nrMoves];

        initRandomPolicy();
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

        super.setVMatrix(VMatrix);

        for (int i = 0; i < Direction.nrMoves; i++) {

            totalPossiblePositionsValue += policyMatrix[posNrPredator][posNrPrey][i] // pi(s,a)
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
            for (int posNrPredator = 0; posNrPredator < Environment.HEIGHT * Environment.WIDTH; posNrPredator++) {
                for (int posNrPrey = 0; posNrPrey < Environment.HEIGHT * Environment.WIDTH; posNrPrey++) {

                    double oldV = VMatrix[posNrPredator][posNrPrey];

                    if (posNrPredator != posNrPrey) {
                        VMatrix[posNrPredator][posNrPrey] = calculateValue(posNrPredator, posNrPrey); // calculate new value for state
                    }
                    maxValueDiff = Math.max(maxValueDiff, Math.abs(VMatrix[posNrPredator][posNrPrey] - oldV)); // keep track of maxValueDiff in this iteration
                }
            }
            System.out.format("Policy Evaluation, iteration number: %d; maxValueDiff = %.15f %n", nrIterations, maxValueDiff);

        } while (maxValueDiff > cutoffValueDiff); // zolang de grootste updatewaarden groter is dan maxDiff
    }

    /**
     * Initialize the policy to be the random policy we had to implement in the first SC (should/could-have)
     */
    private void initRandomPolicy() {

        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            for (int j = 0; j < Environment.HEIGHT * Environment.WIDTH; j++) {
                Arrays.fill(policyMatrix[i][j], 0.2);
            }
        }

    }

    /**
     *  print the VMatrix values
     */
    public void printVMatrix() {

        for (int j = 0; j < Environment.HEIGHT * Environment.WIDTH; j++) {
            System.out.format("%8d", j);
        }
        System.out.println();
        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            System.out.format("%8d", i);
            System.out.print("  ");
            for (int j = 0; j < Environment.HEIGHT * Environment.WIDTH; j++) {
                System.out.format("[%.5f]", VMatrix[i][j]);
            }
            System.out.println();
        }
    }
    

    public double[][] getVMatrix() {
        return VMatrix;

    }

    public void setPolicy(double p[][][]) {
        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            for (int j = 0; j < Environment.HEIGHT * Environment.WIDTH; j++) {
                for (int k = 0; k < Direction.nrMoves; k++) {
                    policyMatrix[i][j][k] = p[i][j][k];
                }
            }
        }
    }

    public void doMove(Position other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Position getPos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
