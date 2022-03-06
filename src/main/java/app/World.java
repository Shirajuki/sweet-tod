package app;

import java.util.Random;

public class World {
	private String[][] board;
	private String openGlyph = ".", closeGlyph = "#", floodGlyph = "_";

	private void genBoard(int x, int y) {
		double chanceToStartOpen = 0.5f;
		String[][] map = new String[y][x];
		for (int yy = 0; yy < y; yy++) {
			for (int xx = 0; xx < x; xx++) {
				if (new Random().nextDouble() < chanceToStartOpen) {
					map[yy][xx] = this.closeGlyph;
				} else {
					map[yy][xx] = this.openGlyph;
				}
			}
		}
		this.board = map;
	}

	// Returns the number of cells in a ring around (x,y) that are alive.
	private int countOpenNeighbours(String[][] map, int x, int y) {
		int count = 0;
		// Checks
		/*
		 * 0 0 0
		 * 0 1 0
		 * 0 0 0
		 */
		for (int i = -1; i < 2; i++) {
			for (int j = -1; j < 2; j++) {
				int nx = x + i;
				int ny = y + j;
				// If we're looking at the middle point
				if (i == 0 && j == 0) {
					// Do nothing, we don't want to add ourselves in!
				}
				// In case the index we're looking at it off the edge of the map
				else if (nx < 0 || ny < 0 || ny >= map.length || nx >= map[0].length) {
					count = count + 1;
				}
				// Otherwise, a normal check of the neighbour
				else if (map[ny][nx].equals(this.closeGlyph)) {
					count = count + 1;
				}
			}
		}
		return count;
	}

	private void doGenStep(String[][] oldMap, int caveHoleLimit) {
		String[][] newMap = new String[oldMap.length][oldMap[0].length];
		for (int y = 0; y < oldMap.length; y++) {
			for (int x = 0; x < oldMap[0].length; x++) {
				int oNbs = countOpenNeighbours(oldMap, x, y);
				if (oldMap[y][x].equals(this.closeGlyph)) {
					if (oNbs < caveHoleLimit) {
						newMap[y][x] = this.openGlyph;
					} else {
						newMap[y][x] = this.closeGlyph;
					}
				} else {
					if (oNbs > caveHoleLimit) {
						newMap[y][x] = this.closeGlyph;
					} else {
						newMap[y][x] = this.openGlyph;
					}
				}
			}
		}
		this.board = newMap;
	}

	private void floodFill(int x, int y) {
		if (this.board[y][x].equals(this.openGlyph)) {
			this.board[y][x] = this.floodGlyph;
			floodFill(x + 1, y);
			floodFill(x - 1, y);
			floodFill(x, y - 1);
			floodFill(x, y + 1);
		} else {
			return;
		}
	}

	static void printBoard(String[][] board) {
		for (int y = 0; y < board.length; y++) {
			String s = "";
			for (int x = 0; x < board[y].length; x++) {
				s += board[y][x];
			}
			System.out.println(s);
		}
	}

	private void replaceFlood() {
		for (int y = 0; y < this.board.length; y++) {
			for (int x = 0; x < this.board[y].length; x++) {
				if (this.board[y][x].equals(this.openGlyph))
					this.board[y][x] = this.closeGlyph;
				if (this.board[y][x].equals(this.floodGlyph))
					this.board[y][x] = this.openGlyph;
			}
		}
	}

	public static String[][] finalGenMapWithFlooding(int width, int height) {
		World w = new World();
		w.genBoard(width, height);
		// Cellular automata generation steps
		for (int i = 0; i < 4; i++) {
			w.doGenStep(w.board, 5 - i);
		}
		World.printBoard(w.board);
		// Find a random open spot, fluid fill from that spot
		int x = (int) w.board[0].length / 3;
		int y = (int) w.board.length / 3;
		while (!w.board[y][x].equals(w.openGlyph)) {
			x++;
			if (x < 0 || x >= w.board[0].length) {
				x = (int) w.board[0].length / 4;
				y++;
			}
		}
		w.floodFill(x, y);
		w.replaceFlood();
		return w.board;
	}

	public static void main(String[] args) {
		World world = new World();
		world.genBoard(30 * 2, 16 * 2);

		World.printBoard(world.board);
		for (int i = 0; i < 4; i++) {
			System.out.println("\r\n");
			world.doGenStep(world.board, 5 - i);
			World.printBoard(world.board);
		}
		int x = (int) world.board[0].length / 3;
		int y = (int) world.board.length / 3;
		while (!world.board[y][x].equals(world.openGlyph)) {
			x++;
			if (x < 0 || x >= world.board[0].length) {
				x = (int) world.board[0].length / 4;
				y++;
			}

		}
		System.out.println("\r\n");
		world.floodFill(x, y);
		World.printBoard(world.board);
		System.out.println("\r\n");
		world.replaceFlood();
		World.printBoard(world.board);
		System.out.println("\r\n\r\n\r\n\r\n");
		String[][] a = World.finalGenMapWithFlooding(30 * 2, 16 * 2);
		System.out.println("\r\n");
		for (int yyy = 0; yyy < a.length; yyy++) {
			String s = "";
			for (int xxx = 0; xxx < a[yyy].length; xxx++) {
				s += a[yyy][xxx];
			}
			System.out.println(s);
		}

	}
}
