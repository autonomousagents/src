import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
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
	
	/**
     * Write a matlab script that plots a colormap of 2D VMatrix to a file
     * @param filename : filename of that script
     */
    public static void writeVMatrix(String filename, double[][] VMatrix)  {
    	try
    	{
            FileWriter fstream = new FileWriter(filename,false);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("clear;clc;");
            out.newLine();
            for (int i=0; i < VMatrix.length; i++) {
                    out.write(String.format("C(%d,:)=[",i+1));
                for (int j=0; j < VMatrix[i].length; j++) {
                    if (j != 0)
                            out.write(",");

                    String number;
                  //  if (i == j) // Set reward at the goal states in the "VMatrix" in the script. Makes a more intuitive colormap.
                     //   number = String.format("%.3f", Environment.reward(Position.getPosition(i), Position.getPosition(j)));
                   // else
                    number = String.format("%.3f", VMatrix[i][j]);
                   
                    out.write(number.replaceAll(",", "."));
                }
                out.write("];");
                out.newLine();
                out.flush();
            }
            out.write("imagesc(C, [ min(min(C)), max(max(C)) ] );");
            out.write("colormap(gray);");
            out.write("axis image");
            out.flush();
            fstream.close();
            out.close();
    	}
    	catch(IOException e)
    	{
    		System.out.println("Error in writeVMatrix(): " + e);
    	}
    }
}
