/**
 * Master AI UvA 2012/2013
 * Autonomous Agents
 * Assignment 1
 *
 * @authors Group 7: Agnes van Belle, Maaike Fleuren, Norbert Heijne, Lydia Mennes
 */

public class Assignment1 {

    private Environment env;
    private View view;
    private int timesteps;
    private static boolean NewRep = false;
    private static final int PredatorRandom = 0, PredatorPolicyEvaluation = 1, PredatorValueIteration = 2, PredatorPolicyIteration = 3;

    public Assignment1() {
        env = new Environment(PredatorRandom);
        view = new View(env);
        timesteps = 0;
    }

    public void start() {

        timesteps = 0;
        env.reset();

        while (!env.isEnded()) {
            env.nextTimeStep();
            timesteps++;
        }
        System.out.println("Timesteps:" + timesteps);
    }

    /**
     * Calculate the variance of an array of ints
     * @param array
     * @param average
     * @return
     */
    public double calculateVariance(int[] array, double average) {

        double variance = 0;
        for (int trial : array) {
            variance += Math.pow(trial, 2);
        }
        variance /= array.length;
        variance -= Math.pow(average, 2);
        return variance;
    }

    /**
     * 2.2 First must
     * Shows average and standard deviation of 100 runs of the number of steps needed in an
     * environment where the predator moves randomly
     */
    public void firstMust() {

        env.setPredatorType(PredatorRandom);

        int nrRuns = 100;
        int totalTimeSteps = 0;
        int timeStepsArray[] = new int[nrRuns];

        for (int i = 0; i < nrRuns; i++) {
            start();
            totalTimeSteps += timesteps;
            timeStepsArray[i] = timesteps;
        }

        double averageTimeSteps = (double) totalTimeSteps / (double) nrRuns;
        double SD = Math.sqrt(calculateVariance(timeStepsArray, averageTimeSteps));


        System.out.println("Average timesteps over " + nrRuns + " trials: " + averageTimeSteps);
        System.out.println("Standard deviation over " + nrRuns + " trials: " + SD);

    }

    /**
     * Policy Evaluation, using "default" state representation
     */
    public void firstShould() {
        env.setPredatorType(PredatorPolicyEvaluation);
        env.reset();
    }

    /**
     * Value Iteration, using "default" state representation
     */
    public void secondMust() {
        env.setPredatorType(PredatorValueIteration);
        env.reset();
    }

    /**
     * Policy Iteration, using "default" state representation
     */
    public void thirdShould() {

        env.setPredatorType(PredatorPolicyIteration);
        env.reset();
    }

    /**
     * Policy Evaluation and Value Iteration, using a more efficient state representation
     */
    public void secondShould() {
        System.out.println("\n===========Policy Evaluation New Rep =============");

        NewRep = true;
        env.setPredatorType(PredatorPolicyEvaluation);
        env.reset();

        System.out.println("\n===========Value Iteration New Rep =============");

        env.setPredatorType(PredatorValueIteration);
        env.reset();
        NewRep = false;
    }

    public static boolean newRep() {
        return NewRep;

    }

    public static void main(String[] args) {
        Assignment1 a = new Assignment1();
        //a.firstMust();
        // a.secondMust();
        // a.firstShould();
        // a.secondShould();
        // a.thirdShould();
    }
}
