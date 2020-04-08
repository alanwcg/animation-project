package main;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import world.Tile;
import world.WallTile;
import world.World;

public class AStar {
	
	public static double lastTime = System.currentTimeMillis();
	private static Comparator<Node> nodeSorter = new Comparator<>() {
		@Override
		public int compare(Node n1, Node n2) {
			if(n1.fCost > n2.fCost) {
				return 1;
			}
			if(n1.fCost < n2.fCost) {
				return -1;
			}
			return 0;
		}
	};
	
	public static boolean clean() {
		if(System.currentTimeMillis() - lastTime >= 1000) {
			return true;
		}
		return false;
	}
	
	private static boolean vecInList(List<Node> list, Vector2i vector) {
		for(int i = 0; i < list.size(); i++) {
			if(list.get(i).tile.equals(vector)) {
				return true;
			}
		}
		return false;
	}
	
	private static double getDistance(Vector2i tile, Vector2i goal) {
		double dx = tile.x - goal.x;
		double dy = tile.y - goal.y;
		
		return Math.sqrt(dx * dx + dy * dy);
	}
	
	public static List<Node> findPath(World world, Vector2i start, Vector2i end) {
		lastTime = System.currentTimeMillis();
		List<Node> openList = new ArrayList<>();
		List<Node> closedList = new ArrayList<>();
		
		Node currentNode = new Node(start, null, 0, getDistance(start, end));
		openList.add(currentNode);
		while(openList.size() > 0) {
			Collections.sort(openList, nodeSorter);
			currentNode = openList.get(0);
			if(currentNode.tile.equals(end)) {
				List<Node> path = new ArrayList<>();
				while(currentNode.parent != null) {
					path.add(currentNode);
					currentNode = currentNode.parent;
				}
				openList.clear();
				closedList.clear();
				return path;
			}
			
			openList.remove(currentNode);
			closedList.add(currentNode);
			
			for(int i = 0; i < 9; i++) {
				if(i == 4) {
					continue;
				}
				int x = currentNode.tile.x;
				int y = currentNode.tile.y;
				int xi = (i % 3) - 1;
				int yi = (i / 3) - 1;
				Tile tile = World.tiles[x + xi + ((y + yi) * World.WIDTH)];
				if(tile == null) {
					continue;
				}
				if(tile instanceof WallTile) {
					continue;
				}
				
				if(i == 0) {
					Tile test1 = World.tiles[x + xi + 1 + ((y + yi) * World.WIDTH)];
					Tile test2 = World.tiles[x + xi + ((y + yi + 1) * World.WIDTH)];
					if(test1 instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				} else if(i == 2) {
					Tile test1 = World.tiles[x + xi - 1 + ((y + yi) * World.WIDTH)];
					Tile test2 = World.tiles[x + xi + ((y + yi + 1) * World.WIDTH)];
					if(test1 instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				} else if(i == 6) {
					Tile test1 = World.tiles[x + xi + 1 + ((y + yi) * World.WIDTH)];
					Tile test2 = World.tiles[x + xi + ((y + yi - 1) * World.WIDTH)];
					if(test1 instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				} else if(i == 8) {
					Tile test1 = World.tiles[x + xi - 1 + ((y + yi) * World.WIDTH)];
					Tile test2 = World.tiles[x + xi + ((y + yi - 1) * World.WIDTH)];
					if(test1 instanceof WallTile || test2 instanceof WallTile) {
						continue;
					}
				}
				
				Vector2i a = new Vector2i(x + xi, y + yi);
				double gCost = currentNode.gCost + getDistance(currentNode.tile, a);
				double hCost = getDistance(a, end);
				
				Node node = new Node(a, currentNode, gCost, hCost);
				if(vecInList(closedList, a) && gCost >= currentNode.gCost) {
					continue;
				}
				
				if(!vecInList(openList, a)) {
					openList.add(node);
				} else if(gCost < currentNode.gCost) {
					openList.remove(currentNode);
					openList.add(node);
				}
			}
			
		}
		closedList.clear();
		return null;
	}

}
