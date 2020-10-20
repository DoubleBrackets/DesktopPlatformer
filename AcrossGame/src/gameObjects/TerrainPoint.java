package gameObjects;

public class TerrainPoint {
	public int x,y,width,height;
	public String tag;
	public TerrainPoint(int x, int y, int width, int height, String type)
	{
		this.x = x;
		this.y = y;
		this.width= width;
		this.height = height;
		tag = type;
	}
}
