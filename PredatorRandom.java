
public class PredatorRandom implements Agent{

	private final static double WAIT = 0.2;
	Position myPos;
	
	public PredatorRandom(Position startPos){
		myPos = startPos;
	}
	public void start() {

    }

	//bla
	@Override
	public void doMove(Position other) {
		
		double chance = Math.random();
		if (chance < WAIT) return;
		
		int myPosX = myPos.getX();
		int myPosY = myPos.getY();

		
		Position[] pos = new Position[4]; 
		pos[0] = new Position(myPosX, (myPosY+Environment.HEIGHT-1)% Environment.HEIGHT);
		pos[1] = new Position((myPosX +Environment.WIDTH+1) % Environment.WIDTH, myPosY);
		pos[2] = new Position(myPosX, (myPosY+Environment.HEIGHT+1)% Environment.HEIGHT);
		pos[3] = new Position((myPosX +Environment.WIDTH-1) % Environment.WIDTH, myPosY);
		
		int index;
	    index = (int)(Math.random()* 4.0);
		myPos = pos[index];
	}

	@Override
	public Position getPos() {
		return myPos;
	}



}
