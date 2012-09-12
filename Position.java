
public class Position {
	private int x;
	private int y;
	
	public Position(){
		this.x = 0;
		this.y = 0;
	}
	
	public Position(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public int getX(){
		return this.x;
	}
	
	public int getY(){
		return this.y;
	}
	
	public void setX(int x){
		this.x = x;
	}
	
	public void setY(int y){
		this.y = y;
	}
	
	public boolean equals(Position other){
		if(this.x == other.getX() && this.y == other.getY()) return true;
		return false;
	}
	
}