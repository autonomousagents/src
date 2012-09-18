
public class Environment {
	public final static int HEIGHT = 11;
	public final static int WIDTH = 11;

	private boolean isEnded;
	private Agent predator;
	private Agent prey;
	private int predatorType=-1;
	
	public Environment(int predatorType){
		
		this.predatorType = predatorType;
		this.isEnded = false;
		
		//reset();
	}
	
	public void nextTimeStep(){
		predator.doMove(getPreyPos());
		if (!checkForEnd()) {
			prey.doMove(getPredatorPos());
		}		
	}
	
	public boolean isEnded(){
		return isEnded;
	}
	
	public Position getPreyPos(){
		return prey.getPos();
	}
	
	public Position getPredatorPos(){
		return predator.getPos();
	}
	
	private boolean checkForEnd(){
		isEnded = getPredatorPos().equals(getPreyPos());
		return isEnded;
	}
	
	public void reset() {

            prey = new Prey(new Position(5,5));
            isEnded = false;

		switch(predatorType) {
			case 0: predator = new PredatorRandom(new Position(0,0)); break;
			case 1: predator = new PredatorPolicyEvaluation(); break;
			case 2: predator = new PredatorValueIteration(new Position(0,0)); break;
			case 3: //predator =  new PredatorPolicyIteration(..); break;
			default: break;
		}
		
		
	}
	
	public static double reward( Position prey, Position predator ){
		if (prey.getX()==predator.getX() && prey.getY()==predator.getY())
			return 10;
		else
			return 0;
	}

        public void setPredatorType(int nr) {

            predatorType = nr;
        }

}
