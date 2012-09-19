import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;


public class PredatorPolicyImprovement {
	
	private double VMatrix[][];
    private double policyMatrix[][][];
    private boolean finished ;
    
    public PredatorPolicyImprovement(double p[][][]) {
    	policyMatrix = p;
    	finished=false;
    }
    
    
    public boolean isFinished() {
    	return finished;    	
    }
    
    public void setVMatrix(double[][] v) {
    	VMatrix=v;
    }
    
    public void writePolicyMatrix(String filename)  {
    	/*try
    	{
            FileWriter fstream = new FileWriter(filename,false);
            BufferedWriter out = new BufferedWriter(fstream);

            out.write("clear;clc;");
            out.newLine();
            for (int i=0; i < Environment.HEIGHT*Environment.WIDTH; i++) {
                    out.write(String.format("C(%d,:)=[",i+1));
                for (int j=0; j < Environment.HEIGHT*Environment.WIDTH; j++) {
                    if (j != 0)
                            out.write(",");

                    String number;
                    if (i == j) // Set reward at the goal states in the "VMatrix" in the script. Makes a more intuitive colormap.
                        number = String.format("%.3f", Environment.reward(getPosition(i), getPosition(j))); 
                    else
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
    	}*/
    }

}
