
public class Assignment1 {

	private Environment env;
	private View view;
	private int timesteps;
	
	private static final int PredatorRandom = 0, PredatorPolicyEvaluation = 1, PredatorValueIteration = 2, PredatorPolicyIteration=3;
	
	public Assignment1() {
		env = new Environment(PredatorRandom);
		view = new View(env);
		timesteps = 0;
	}
	
	public void start(){
		
		timesteps = 0;
		env.reset();
		
		while(!env.isEnded()){			
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
		for (int trial : array) 			
			variance += Math.pow(trial, 2);			
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
		
		for (int i=0; i < nrRuns; i++) {			
			start();
			totalTimeSteps += timesteps;
			timeStepsArray[i] = timesteps;			
		}
		
		double averageTimeSteps = (double)totalTimeSteps/(double)nrRuns;
		double SD = Math.sqrt(calculateVariance(timeStepsArray,averageTimeSteps));
		
		
		System.out.println("Average timesteps over "  + nrRuns + " trials: " + averageTimeSteps);
		System.out.println("Standard deviation over " + nrRuns + " trials: " + SD);
		
	}

        public void firstShould() {            
            env.setPredatorType(PredatorPolicyEvaluation);
            env.reset();
        }
	
	public static void main (String[] args) {
		Assignment1 a = new Assignment1();
		//a.start();
		a.firstMust();
               // a.firstShould();
	}
	
	
}
