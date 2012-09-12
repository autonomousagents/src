
public class PredatorPolicyEvaluation implements Agent {
													// up, right, down left, still
	private final static double chancesPositions[] = {0.2 , 0.2, 0.2, 0.2, 0.2};
	private double VMatrix[][];
	
	
//	/**
//	* @param position of state in QMatrix or NMatrix
//	* @return State (struct) with correct x and y coordinates (fields)
//	*/
//	State getState(int nr) {
//
//	    int xpos= nr % worldWidth;
//	    int ypos= (nr - xpos) / worldWidth;
//	    State s ={xpos, ypos, nr};
//	    return s;
//	}
//
//
//	/**
//	* @param State (struct) with correct x and y coordinates (fields)
//	* @return position of state in QMatrix or NMatrix
//	*/
//	int getStateNr(State state) { // positie (hoogte) in QMatrix en NMatrix tabel
//
//	    return state.y * worldWidth + state.x;
//	}

	
	// return array always length of chancesPositions.length
	// ORDER IS: // up, right, down left, still
	private int[] chancesPositionsPrey(int currentPositionPrey) {
		
		
		return null;
	}

	private double getPositionValue(int posPredator, int posPrey) {
		
		int[] chancesPreyPositions = chancesPositionsPrey(posPrey);
		//int[] newPreyPositions = newPositionsPrey(posPredator, posPrey);
		
		double totalValue=0;
		for (int i=0; i < chancesPreyPositions.length; i++) {
			
			totalValue += chancesPreyPositions[i]  ;//* ( Reward(state) + discountFactor*(VMatrix[ posPredator ] [ newPreyPositions[i] ]
			
		}
		return (Double) null;
		
	}
	
	private int[] newPositionsPredator(int currentPositionPredator) {
		
		
		return null;
	}
	
	public double calculateValue(int posPredator, int posPrey) {
		
		double total =0;
		
		int[] possiblePositions = newPositionsPredator(posPredator);
		
		for (int i=0; i < 5 /*aantal posities */; i++) {			
			
			double TEMP  = chancesPositions[i] * getPositionValue( possiblePositions[i] , posPrey);
			
		}
		
		return (Double) null;
	}
	
	/**
	 * Start van policy evaluation
	 */
	public void start() {
		
		double maxDiff = 0.01;
		
		// zolang de grootste updatewaarden groter is dan maxDiff
			// voor elke positie in VMatrix
				// bereken state-waarde:
				// Vmatrix[i][j] = calculateValue(i,j);
		
	}
	
	
	public PredatorPolicyEvaluation() {
		 VMatrix = new double[Environment.HEIGHT*Environment.WIDTH][Environment.HEIGHT*Environment.WIDTH];
	}
	
	@Override
	public void doMove(Position other) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Position getPos() {
		// TODO Auto-generated method stub
		return null;
	}

}
