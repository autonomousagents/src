
public class Assignment1 {

	private Environment env;
	private View view;
	private int timesteps;
	
	public Assignment1(){
		env = new Environment();
		view = new View(env);
		timesteps = 0;
	}
	
	public void start(){
		timesteps = 0;
		while(!env.isEnded()){
			env.nextTimeStep();
			timesteps++;
			view.print();
		}
	}
	
	public static void main (String[] args) {
		Assignment1 a = new Assignment1();
		a.start();
	}
	
	
}
