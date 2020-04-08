package world;

public class Camera {
	
	public static int x, y;
	
	public static int clamp(int current, int max) {
		if(current < 0) {
			current = 0;
		}
		if(current > max) {
			current = max;
		}
		return current;
	}

}
