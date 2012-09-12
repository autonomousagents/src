
public class Assignment1 {

	private Environment env;
	private View view;
	private int timesteps;
	
	
	public Assignment1() {
		env = new Environment();
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
		// timesteps heeft waarde van anatal timesteps nu
	}
	
	public void firstMust() {
		
		int nrRuns = 100;
		int totalTimeSteps = 0;
	
		for (int i=0; i < nrRuns; i++) {
			
			start();
			totalTimeSteps += timesteps;
			
		}
		System.out.println("Average timesteps over "  + nrRuns + " trials: " + (double)totalTimeSteps/(double)nrRuns);
		
	}
	
	public static void main (String[] args) {
		Assignment1 a = new Assignment1();
		//a.start();
		a.firstMust();
	}
	
	
}
