package app;

import javafx.scene.paint.Color;
import javafx.geometry.Pos;
import javafx.scene.layout.Pane;
import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.text.SimpleDateFormat;

public class Game {
	public static void main(String[] args) {
		Game g = new Game();
		g.createGameAll();
		g.p.setPlayerRadius();
		g.drawGameBoard();
	}

	String[][] board = World.finalGenMapWithFlooding(30 * 3, 16 * 4);
	private final List<List<Tile>> tileArr = new ArrayList<>();
	public final List<Tile> gameArr = new ArrayList<>();
	private final List<Entity> entityArr = new ArrayList<>();
	private final List<Item> itemArr = new ArrayList<>();
	public final List<String> infoArr = new ArrayList<>();

	private final int GAME_WIDTH = Settings.GAME_WIDTH();
	private final int GAME_HEIGHT = Settings.GAME_HEIGHT();
	private final int TILE_SIZE = Settings.TILE_SIZE();
	private boolean isOnSurface = false;
	// ENTITY
	private int foundEntityIndex = 0;
	// ITEM
	private int foundItemIndex = 0;
	// Game Settings
	public Pane gamePaneElements = new Pane();
	public Player p = new Player(board, gameArr, entityArr, infoArr, itemArr);
	public final Screen screen = new Screen(board, p, gamePaneElements, gameArr, entityArr, infoArr, itemArr);
	private final Factory factory = new Factory(board, p, gameArr, entityArr, infoArr, itemArr);
	public Save save = new SaveImpl();
	public boolean gameStarted = false;
	public boolean returnToMainMenu = false;

	public void createGameAll() {
		// reset
		tileArr.clear();
		gameArr.clear();
		entityArr.clear();
		itemArr.clear();
		infoArr.clear();
		// updateBoard
		p.updateBoard(board);
		factory.updateBoard(board);
		gamePaneElements.getChildren().clear();
		createGameBoard(board);
		// DEFAULT GUI SCREENS
		screen.createGameInfo(); // 0
		screen.createPlayerInfo(); // 1
		screen.createPlayerInventory(); // 2
		// NEW SCREENS
		screen.createBlackScreen(); // 3
		screen.createHelpScreen(); // 4
		screen.createPlayerStatus(); // 5
		screen.createLevelUpScreen(); // 6
		screen.createGameOverScreen(); // 7
		screen.createMenu(); // 8
		spawnMonsters();
		gameLoop();
		resetLoop();
	}

	public void gameLoop() {
		p.setPlayerRadius();
		moveEntities(p);
		drawGameBoard();
		screen.drawGameInfo(0);
		screen.drawPlayerInfo(1);
		if (!p.alive()) {
			resetLoop();
			screen.drawGameOverScreen(3, 7);
			List<String> lb = save.listedLeaderboard();
			SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yy");
			lb.add(formatter.format(new Date()) + "," + p.name + "," + p.getDeathMessage() + "," + p.floor + ","
					+ p.getStringStat("lvl") + "," + p.candy);
			save.writeLeaderboard(lb);
			save.saveChar(new ArrayList<String>());
		}
		if (p.ascended) {
			reloadGame();
		}
	}

	public void reloadGame() {
		board = World.finalGenMapWithFlooding(30 * 3, 16 * 4);
		p.ascended = false;
		createGameAll();
		p.spawnPos();
		resetLoop();
		gameLoop();
	}

	public void resetLoop() {
		if (!p.alive() && p.gameOverScreenShown) {
			returnToMainMenu();
		}
		// Reset Screens
		screen.resetScreenText(2); // inventory
		screen.resetScreenText(5); // status
		screen.resetScreenText(6); // levelup
		screen.resetBlackScreen(3);
		screen.resetScreenText(4); // help
		screen.resetScreenText(7); // gameOver
		screen.resetScreenText(8); // menu
		screen.setScreenState("none");
		screen.setNewScreen(false);
	}

	public void returnToMainMenu() {
		this.returnToMainMenu = true;
	}

	public boolean loadChar() {
		List<String> ls = save.loadChar();
		if (ls != null) {
			gameStarted = true;
			reloadGame();
			p.loadSaveFile(ls);
			return true;
		}
		return false;
	}

	public void saveGameCharacter() {
		infoArr.add("Saving...");
		save.saveChar(p.getPlayerSaveInfo());
		infoArr.add("Save success!");
	}

	public void newInputScreen(int n) {
		infoArr.clear();
		if (screen.screenState.equals("menu")) {
			switch (n) {
				case 0:
					returnToMainMenu();
					break;
				case 1:
					saveGameCharacter();
					break;
				default:
					return;
			}
			resetLoop();
		} else if (screen.screenState.equals("gameOver")) {
			// Only allow [ESC], redirects to [ESC]
		} else if (screen.screenState.equals("levelup")) {
			switch (n) {
				case 0:
					p.modifyHP(10);
					break;
				case 1:
					p.modifyATK(1);
					break;
				case 2:
					p.modifyDEF(1);
					break;
				case 3:
					p.modifyViewRange(1);
					break;
				default:
					p.randomSkillUp(4);
					break;
			}
		} else if (screen.screenState.equals("unequip")) {
			List<Item> equipmentShown = p.getEquipment();
			for (int i = 0; i < equipmentShown.size(); i++) {
				if (n == i) {
					Item item = equipmentShown.get(i);
					p.unequip(item);
					// resetLoop();
				}
			}
		} else {
			List<Item> inventoryShown = p.getInventory(screen.screenState);
			if (n >= inventoryShown.size())
				return;
			Item item = inventoryShown.get(n);
			switch (screen.screenState) {
				case "canEquip":
					p.equip(item);
					break;
				case "examine":
					p.examine(item);
					break;
				case "canDrink":
					p.quaff(item);
					break;
				case "canEat":
					p.eat(item);
					break;
				case "drop":
					p.drop(item);
					break;
				default:
					break;
			}
		}
		gameLoop();
		resetLoop();
	}

	public void spawnMonsters() {
		for (int i = 0; i < 10; i++) {
			factory.newRandomSword();
		}
		for (int i = 0; i < 10; i++) {
			factory.newRandomArmor();
		}
		for (int i = 0; i < 25; i++) {
			factory.newRandomFood();
		}
		for (int i = 0; i < 4; i++) {
			factory.newRandomRings();
		}
		for (int i = 0; i < 12; i++) {
			factory.newRandomMisc();
		}
		for (int i = 0; i < 12; i++) {
			factory.newRandomPotions();
		}
		for (int i = 0; i < 100; i++) {
			factory.newFungus(10, 0, 1);
		}
		for (int i = 0; i < 55; i++) {
			factory.newRandomWeakMob();
		}
		for (int i = 0; i < 7; i++) {
			factory.newZombie(25, 14, 4);
		}
		for (int i = 0; i < 8; i++) {
			factory.newGoblin(35, 21, 5);
		}
		factory.newStair();
	}

	public void createGameBoard(String[][] b) {
		for (int y = 0; y < b.length; y++) {
			List<Tile> tileArrY = new ArrayList<>();
			for (int x = 0; x < b[y].length; x++) {
				String tileType = "";
				Color color = Color.rgb(24, 24, 24);
				Color bgColor = Color.BLACK;
				Tile tile = new Tile(tileType, color, bgColor);
				tile.setTranslateX(x * TILE_SIZE + 50);
				tile.setTranslateY(y * TILE_SIZE + 30);
				tileArrY.add(tile);
				gamePaneElements.getChildren().add(tile);
			}
			tileArr.add(tileArrY);
		}
		System.out.println(tileArr);
	}

	// https://www.geeksforgeeks.org/bresenhams-line-generation-algorithm/
	public void drawGameBoard() {
		for (int y = board.length - 1; y >= 0; y--) {
			for (int x = board[y].length - 1; x >= 0; x--) {
				String glyph = "";
				Color color = Color.rgb(24, 24, 24);
				Color bgColor = null;
				if (p.x == x && p.y == y) {
					color = p.color;
					glyph = p.glyph;
					bgColor = Color.rgb(50, 50, 50);
					if (isItem(x, y))
						infoArr.add("This is a " + itemArr.get(foundItemIndex).name());
				} else {
					if (isOnSurface) {
						color = Color.WHITE;
						glyph = board[y][x];
						bgColor = Color.rgb(50, 50, 50);
						if (isEntity(x, y)) {
							Entity e = entityArr.get(foundEntityIndex);
							color = e.color;
							glyph = e.glyph;
						} else if (isItem(x, y)) {
							Item item = itemArr.get(foundItemIndex);
							color = item.color();
							glyph = item.glyph();
						}
						Tile t = (Tile) tileArr.get(y).get(x);
						t.updateTile(glyph, color, bgColor);
						t.updateTilePos(x * TILE_SIZE - (p.x * TILE_SIZE) + GAME_WIDTH / 2 - 2 * TILE_SIZE,
								y * TILE_SIZE - (p.y * TILE_SIZE) + GAME_HEIGHT / 2 + TILE_SIZE, Pos.CENTER);
						continue;
					}
					if (p.inRadius(x, y)) {
						glyph = board[y][x];
						if (glyph.equals(".")) {
							Tile tt = (Tile) tileArr.get(y).get(x);
							if (!tt.visited())
								tt.setVisited(true);
						}
						// BRESENHAM LINE
						int dx = Math.abs(x - p.x);
						int dy = Math.abs(y - p.y);

						int sx = p.x < x ? 1 : -1;
						int sy = p.y < y ? 1 : -1;
						int err = dx - dy;
						int xx = p.x, yy = p.y;
						while (true) {
							if (board[yy][xx].equals("#")) {
								color = Color.rgb(24, 24, 24);
								bgColor = null;
								glyph = "";
								Tile t = (Tile) tileArr.get(yy).get(xx);
								if (!t.visited())
									t.setVisited(true);
								break;
							} else {
								color = Color.WHITE;
								if (p.inLineRadius(xx, yy)) {
									bgColor = Color.rgb(32, 32, 32);
									color = Color.DARKGRAY;
								} else {
									bgColor = Color.rgb(50, 50, 50);
								}
								// THIS IS MAKING THE GAME LAG vvvv
								if (isEntity(x, y)) {
									Entity e = entityArr.get(foundEntityIndex);
									color = e.color;
									glyph = e.glyph;
								} else if (isItem(x, y)) {
									Item item = itemArr.get(foundItemIndex);
									color = item.color();
									glyph = item.glyph();
								}
							}
							if (xx == x && yy == y)
								break;

							// Add slope to increment angle formed
							int e2 = err * 2;
							// Slope error reached limit, time to
							// increment x or y and update slope error.
							if (e2 > -dx) {
								err -= dy;
								xx += sx;
							}
							if (e2 < dx) {
								err += dx;
								yy += sy;
							}
						}
					}
				}

				Tile t = (Tile) tileArr.get(y).get(x);
				if (t.visited() && !glyph.equals(board[y][x]) && !(x == p.x && y == p.y) && !isEntity(x, y)
						&& !(isItem(x, y))) {
					color = Color.rgb(18, 18, 18);
					glyph = board[y][x];
					if (board[y][x] == "#" && p.inRadius(x, y)) {
						color = Color.WHITE;
					}
				}
				t.updateTile(glyph, color, bgColor);
				t.updateTilePos(x * TILE_SIZE - (p.x * TILE_SIZE) + GAME_WIDTH / 2 - 2 * TILE_SIZE,
						y * TILE_SIZE - (p.y * TILE_SIZE) + GAME_HEIGHT / 2 + TILE_SIZE, Pos.CENTER);
			}
		}
	}

	public void moveEntities(Player p) {
		for (int i = entityArr.size() - 1; i >= 0; i--) {
			Entity e = entityArr.get(i);
			if (e.alive && p.inRadius(e.x, e.y)) {
				e.movePattern();
			}
		}
	}

	public boolean isEntity(int x, int y) {
		for (int i = entityArr.size() - 1; i >= 0; i--) {
			Entity e = entityArr.get(i);
			if (e.alive) {
				if (e.x == x && e.y == y) {
					foundEntityIndex = i;
					return true;
				}
			} else {
				infoArr.add("Enemy deeeeeed!");
				infoArr.add("Received some xp and candies!");
				if (p.receiveExp(e.stats.get("exp"))) {
					screen.drawLevelUpScreen(6);
				}
				;
				p.killedEnemy();
				p.receiveCandy(e.candy());
				if (e.hasDrops())
					e.dropItemFromDropTable();
				entityArr.remove(i);
			}
		}
		return false;
	}

	public boolean isItem(int x, int y) {
		for (int i = itemArr.size() - 1; i >= 0; i--) {
			Item item = itemArr.get(i);
			if (item.pickedUp) {
				itemArr.remove(i);
			} else {
				if (item.x == x && item.y == y) {
					foundItemIndex = i;
					return true;
				}
			}
		}
		return false;
	}
}
