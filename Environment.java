
public class Environment {
	public final static int HEIGHT = 11;
	public final static int WIDTH = 11;
	private boolean isEnded;
	private Agent predator;
	private Agent prey;
	
	public Environment(){
		isEnded = false;
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
}
