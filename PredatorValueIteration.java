import java.util.Arrays;


public class PredatorValueIteration implements Agent{

	private final static double WAIT = 0.2;
	Position myPos;
	Position startPosition;
	private double v[][][][] = new double[Environment.HEIGHT][Environment.WIDTH][Environment.HEIGHT][Environment.WIDTH];
	double theta, gamma;
	
	public PredatorValueIteration(Position startPos){
		myPos = startPos;
		startPosition=startPos;
		
		fillV();
		calcV();
		//bepaal Policy
	}

	private void calcV() {
		double delta ;
		double[] vPerAction= new double [5];
		do {
			delta = 0.0;
			double vNew[][][][] = new double[Environment.HEIGHT][Environment.WIDTH][Environment.HEIGHT][Environment.WIDTH];
			for(int i = 0; i < Environment.HEIGHT; i++){
				for(int j = 0; j < Environment.WIDTH; j++){
					for(int k = 0; k < Environment.HEIGHT; k++){
						for(int l = 0; l<Environment.WIDTH;l++){
							Arrays.fill(vPerAction,0);
							for(int m = 0; l<5;l++){
								vPerAction = calcVPerAction(i,j,k,l);
							}
							vNew[i][j][k][l] = getMaximum(vPerAction);
							//als absoluut(vNew-v)> delta dan delta = |vnew-v|
						}
					}
				}
			}
			v = vNew;
		} while(delta>theta);
	}
	private double[] calcVPerAction(int xPred,int yPred,int xPrey, int yPrey) {
		//intitaliseer de rij die je terug gaat geven
		//vraag posities op waar predator heen kan
		//voor elke positie
			//vraag states op waar je kan zijn na uitvoeren actie (locaties prey, pas op constraint dat prey niet tegen predator aan komt)
			// calc V met deze gegevens		
		
		//deze return aanpassen
		return null;
	}
	
	private Position [] getPostions(Position p){
		Position[] pos  = new Position[5];
		pos[0] = new Position(p.getX(), (p.getY()+Environment.HEIGHT-1)% Environment.HEIGHT);
		pos[1] = new Position((p.getX() +Environment.WIDTH+1) % Environment.WIDTH, p.getY());
		pos[2] = new Position(p.getX(), (p.getY()+Environment.HEIGHT+1)% Environment.HEIGHT);
		pos[3] = new Position((p.getX() +Environment.WIDTH-1) % Environment.WIDTH, p.getY());
		pos[4] = new Position(p.getX(),p.getY());
		return pos;
	}

	// private calcPolicy(){...}
	
	@Override
	public void doMove(Position other) {
		// TODO Auto-generated method stub
		//uit policy opvragen 
	}

	@Override
	public Position getPos() {
		return myPos;
	}
	
	public void restart(){
		myPos = startPosition;
	}
	
	private void fillV() {
		for(int i = 0; i < Environment.HEIGHT; i++){
			for(int j = 0; j < Environment.WIDTH; j++){
				for(int k = 0; k < Environment.HEIGHT; k++){
					Arrays.fill(v[i][j][k], 0.0);
				}
			}
		}
	}
	
	public void setParams(double theta, double gamma){
		this.theta=theta;
		this.gamma = gamma;
	}
	
	private double getMaximum(double [] values){
		double max = values[0];
		for(int i = 1; i<values.length;i++){
			if (values[i]>max){
				max = values[i];
			}
		}
		return max;
	}
}
