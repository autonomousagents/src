import java.util.Arrays;

public class View {
	
	private final static String EMPTY = "_";
	private final static String PREY = "b";
	private final static String PREDATOR = "P";
	private Environment env;
	String[][] world;
	
	public View(Environment env){
		this.env = env;
		world = new String[Environment.HEIGHT][Environment.WIDTH];		
		fill();
	}
	
	public void print(){
		world[env.getPreyPos().getY()][env.getPreyPos().getX()] = PREY;
		world[env.getPredatorPos().getY()][env.getPredatorPos().getX()] = PREDATOR;

		for(String[] row: world){
			for (String place : row) {
			//System.out.println(row.toString());
			//Arrays.toString(row);
				System.out.print(place + "  ");
			}
			System.out.println();
		}
		System.out.println();
		
		world[env.getPredatorPos().getY()][env.getPredatorPos().getX()] = EMPTY;
		world[env.getPreyPos().getY()][env.getPreyPos().getX()] = EMPTY;
	}
	
	private void fill(){
		for(String[] row: world){
			Arrays.fill(row, EMPTY);
		}
	}
}
