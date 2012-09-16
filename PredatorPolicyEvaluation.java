
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
	private int[] chancesPositionsPrey(int posPredator, int[] PositionPrey) {
		int[] chanceArray = new int[chancesPositions.length];
		boolean predatorNextToPrey = false;
		int predatorEqualsIndex;
		for(int i = 0;i< chancesPositions.length;i++){
			if(PositionPrey[i] == posPredator){
				predatorEqualsIndex = i;
				predatorNextToPrey = true;
				break;
			}
		}
		
		//determine chances
		
		return chanceArray;
	}
	
	/*private int[] newPositionsPrey(int posPredator, int currentPositionPrey){
		return null;
	}*/
	
	private int[] newPositions(int currentPos) {
		int[] positionArray = new int[chancesPositions.length];
		//up
		positionArray[0] = currentPos-Environment.WIDTH < 0?
								currentPos+(Environment.HEIGHT*Environment.WIDTH)-Environment.WIDTH:
								currentPos-Environment.WIDTH;
		//right
		positionArray[1] = (currentPos+1)%Environment.WIDTH == 0?
								currentPos - Environment.WIDTH:
								currentPos +1;
		//left
		positionArray[2] = currentPos == 0? 
								Environment.WIDTH : 
								(currentPos -1)% Environment.WIDTH == Environment.WIDTH-1? 
										currentPos + Environment.WIDTH -1: currentPos -1;
		//down
		positionArray[3] = currentPos+Environment.WIDTH>Environment.HEIGHT*Environment.WIDTH?
								currentPos-(Environment.HEIGHT*Environment.WIDTH)+Environment.WIDTH:
								currentPos+Environment.WIDTH;
		//still
		positionArray[4] = currentPos;
		
		return positionArray;
	}

	private double getPositionValue(int posPredator, int posPrey) {
		
		//int[] newPreyPositions = newPositionsPrey(posPredator, posPrey);
		int[] newPreyPositions = newPositions(posPrey);
		int[] chancesPreyPositions = chancesPositionsPrey(posPredator, newPreyPositions);
		
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
	
	public PredatorPolicyEvaluation(Position startPos) {
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
