/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */

import java.util.Arrays;
import java.util.Collections;

public class PredatorPolicyIteration implements Agent {

    private double VMatrix[][];
    private double policyMatrix[][][]; // predatorstates,preystates,probabilities per actie
    private PredatorPolicyEvaluation policyEvaluator;
    private PredatorPolicyImprovement policyImprover;

    public PredatorPolicyIteration() {

        policyMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH][Direction.nrMoves];
        VMatrix = new double[Environment.HEIGHT * Environment.WIDTH][Environment.HEIGHT * Environment.WIDTH];

        initMatricesRandom();

        policyEvaluator = new PredatorPolicyEvaluation(VMatrix);
        policyImprover = new PredatorPolicyImprovement(policyMatrix);

        start();
    }

    /**
     * Start policy iteration
     */
    public void start() {

        int iter = 0;

        do {
            iter++;
            System.out.println("==> Policy Iteration: iteration nr. " + iter);

            policyEvaluator.setPolicy(policyMatrix);
            policyEvaluator.start();

            // we got the values, now give them to improver
            setVMatrix(policyEvaluator.getVMatrix());
            policyImprover.setVMatrix(VMatrix);

            // start improving
            policyImprover.start();
            setPolicy(policyImprover.getPolicy());


        } while (!policyImprover.isFinished());

        printAskedValues();

    }

    /**
     * Outputs the values of all states in which the prey is located at (5; 5).
     **/
     public void printAskedValues() {

        Position preyPos = new Position(5,5);
        int posNrPrey = preyPos.getPosNr();

        System.out.println("The values of all states in which the prey is located at (5,5), in grid representation: ");

        double gridArray[][] = new double[Environment.HEIGHT][Environment.WIDTH];

        for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
            Position predatorPos = Position.getPosition(i);
            gridArray[predatorPos.getY()][predatorPos.getX()] = VMatrix[i][posNrPrey];
         }

        for (int i=0; i < Environment.HEIGHT; i++) {
             for (int j=0; j < Environment.WIDTH; j++) {
                System.out.format("[%8.6f]", gridArray[i][j]);
            }
             System.out.println();
        }
    }


     /**
      * @param size : size (length) of return array
      * @return array with a value in [0,1) at each position such that the values together sum to 1
      */
    private double[] randomProbabilities(int size) {
        double[] array = new double[size];
        for (int i = 0; i < size; i++) {
            array[i] = 1;
            for (int j = 0; j < i; j++) {
                array[i] -= array[j];
            }
            array[i] *= Math.random();
        }
        Collections.shuffle(Arrays.asList(array));
        return array;
    }


    /**
     * Initialize the policy and value matrices randomly
     */
    private void initMatricesRandom() {

        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            for (int j = 0; j < Environment.HEIGHT * Environment.WIDTH; j++) {

                if (i != j) {
                    // policy
                    double randomArray[] = randomProbabilities(Direction.nrMoves);

                    policyMatrix[i][j] = Arrays.copyOf(randomArray, Direction.nrMoves);
                    // VMatrix
                    VMatrix[i][j] = Environment.minimumReward + Math.random() * (Environment.maximumReward - Environment.minimumReward);
                }
                else {
                    VMatrix[i][j] = 0;
                    Arrays.fill(policyMatrix[i][j], 0);
                }

            }
        }


    }

    public void setPolicy(double p[][][]) {
       
        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            for (int j = 0; j < Environment.HEIGHT * Environment.WIDTH; j++) {
                System.arraycopy(p[i][j], 0, policyMatrix[i][j], 0, Direction.nrMoves);
            }
        }
    }

    public void setVMatrix(double[][] v) {
       
        for (int i = 0; i < Environment.HEIGHT * Environment.WIDTH; i++) {
            System.arraycopy(v[i], 0, VMatrix[i], 0, Environment.HEIGHT * Environment.WIDTH);
        }
    }

    public void doMove(Position other) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public Position getPos() {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
