package app;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Pos;
import javafx.scene.paint.Color;
import javafx.scene.layout.Pane;

public class Screen {
	private String[][] board;
	private Player p;
	private List<Tile> gameArr;
	private List<String> infoArr;
	private List<Entity> entityArr;
	private List<Item> itemArr;
	private Pane gamePaneElements;
	public boolean newScreen = false;
	public String screenState = "none";

	public Screen(String[][] b, Player p, Pane gamePaneElements, List<Tile> gameArr, List<Entity> entityArr,
			List<String> infoArr, List<Item> itemArr) {
		this.board = b;
		this.p = p;
		this.gameArr = gameArr;
		this.entityArr = entityArr;
		this.infoArr = infoArr;
		this.itemArr = itemArr;
		this.gamePaneElements = gamePaneElements;
	}

	public void updateBoard(String[][] b) {
		this.board = b;
	}

	public boolean newScreen() {
		return this.newScreen;
	}

	public String screenState() {
		return this.screenState;
	}

	public void setNewScreen(boolean state) {
		this.newScreen = state;
	}

	public void setScreenState(String state) {
		this.screenState = state;
	}

	// CREATE METHODS
	public void createBlackScreen() {
		Tile blck = new Tile("", Color.BLACK, Color.BLACK);
		blck.changeSize(Settings.GAME_WIDTH(), Settings.GAME_HEIGHT());
		blck.updateTilePos(0, 0, Pos.TOP_LEFT);
		gameArr.add(blck);
		gamePaneElements.getChildren().add(blck);
	}

	public void createGameInfo() {
		String text = "bresenhams-line-generation-algorithm owo";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.GAME_WIDTH() / 3 - 4 * Settings.TILE_SIZE());
		tile.setTranslateY(Settings.GAME_HEIGHT() - Settings.TILE_SIZE() * 2 - (1 - 1) * 18);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	public void createPlayerInfo() {
		String text = p.stats.get("hp").intValue() + "hp/" + p.stats.get("hpMax").intValue() + "hp";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.TILE_SIZE() * 3 - text.length() / 4 * Settings.TILE_SIZE());
		tile.setTranslateY(Settings.GAME_HEIGHT() - Settings.TILE_SIZE() * 2);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	public void createPlayerInventory() {
		String text = "";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.GAME_WIDTH() - 10 * Settings.TILE_SIZE() - text.length());
		tile.setTranslateY(Settings.TILE_SIZE() * 2 - (1 - 1) * 18);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	public void createPlayerStatus() {
		String text = "";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.GAME_WIDTH() - 10 * Settings.TILE_SIZE() - text.length());
		tile.setTranslateY(Settings.TILE_SIZE() * 2 - (1 - 1) * 18);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	public void createHelpScreen() {
		String text = "";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.GAME_WIDTH() - 10 * Settings.TILE_SIZE() - text.length());
		tile.setTranslateY(Settings.TILE_SIZE() * 2 - (1 - 1) * 18);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	public void createGameOverScreen() {
		String text = "";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.GAME_WIDTH() - 10 * Settings.TILE_SIZE() - text.length());
		tile.setTranslateY(Settings.TILE_SIZE() * 2 - (1 - 1) * 18);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	public void createMenu() {
		String text = "";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.GAME_WIDTH() - 10 * Settings.TILE_SIZE() - text.length());
		tile.setTranslateY(Settings.TILE_SIZE() * 2 - (1 - 1) * 18);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	public void createLevelUpScreen() {
		String text = "";
		Tile tile = new Tile(text, Color.WHITE, Color.TRANSPARENT, true);
		tile.setTranslateX(Settings.GAME_WIDTH() - 10 * Settings.TILE_SIZE() - text.length());
		tile.setTranslateY(Settings.TILE_SIZE() * 2 - (1 - 1) * 18);
		tile.setAlignment(Pos.CENTER);
		gameArr.add(tile);
		gamePaneElements.getChildren().add(tile);
	}

	// DRAW METHODS
	public void drawGameInfo(int n) {
		// String text = "bresenhams-line-generation-algorithm owo";
		Tile t = (Tile) gameArr.get(n);
		t.setTranslateY(Settings.GAME_HEIGHT() - Settings.TILE_SIZE() * 2 - (infoArr.size() - 1) * 18 - 5);
		t.updateText(infoArr);
	}

	public void drawPlayerInfo(int n) {
		List<String> playerArr = new ArrayList<>();
		String hp = "hp  " + " ".repeat(6 - p.stats.get("hp").toString().length()) + p.stats.get("hp").intValue();
		String lvl = "lvl " + " ".repeat(6 - p.stats.get("lvl").toString().length()) + p.stats.get("lvl").intValue();
		String exp = "exp " + " ".repeat(6 - p.stats.get("exp").toString().length()) + p.stats.get("exp").intValue();
		playerArr.add(lvl);
		playerArr.add(hp);
		playerArr.add(exp);
		int translateX = Settings.TILE_SIZE() * 3 - hp.length() / 4 * Settings.TILE_SIZE() - 10;
		int translateY = Settings.GAME_HEIGHT() - Settings.TILE_SIZE() * 2 - (playerArr.size() - 1) * 18;
		Pos alignment = Pos.CENTER;
		Tile t = (Tile) gameArr.get(n);
		t.updateText(playerArr);
		t.updateTilePos(translateX, translateY, alignment);
	}

	public void drawPlayerInventory(int n, String filter) {
		String text = "";
		int c = 0;
		if (filter.equals("unequip")) {
			for (int i = 0; i < p.equipment.size(); i++) {
				if (!p.equipment.get(i).name().equals("None")) {
					if (p.equipment.get(i).name().length() > text.length()) {
						text = p.equipment.get(i).name();
						c++;
					}
				}
			}
		} else {
			for (int i = 0; i < p.inventory.size(); i++) {
				if (p.inventory.get(i).hasModifier(filter)) {
					if (p.inventory.get(i).name().length() > text.length()) {
						text = p.inventory.get(i).name();
						c++;
					}
				}
			}
		}
		if (c == 0)
			text = "No items to be found!";
		Tile t = (Tile) gameArr.get(n);
		double moveX = 5;
		if (filter == "listed")
			moveX -= 1.5;
		int translateX = (int) (Settings.GAME_WIDTH() - moveX * Settings.TILE_SIZE()
				- (text.length()) * Settings.TILE_SIZE() / 2);
		int translateY = Settings.TILE_SIZE();
		Pos alignment = Pos.CENTER_RIGHT;
		t.updateTilePos(translateX, translateY, alignment);
		t.updateTile("", Color.WHITE, Color.TRANSPARENT);
		if (filter.equals("unequip")) {
			t.updateText(p.getEquipmentListed());
		} else {
			t.updateText(p.getInventoryListed(filter));
		}
		if (c == 0) {
			t.updateTextString("No items to be found!\n[ESC] to close...");
		}
		newScreen = true;
		screenState = filter;
	}

	public void drawLevelUpScreen(int n) {
		Tile t = (Tile) gameArr.get(n);
		int translateX = Settings.TILE_SIZE();
		int translateY = Settings.TILE_SIZE();
		Pos alignment = Pos.CENTER_RIGHT;
		t.updateTilePos(translateX, translateY, alignment);
		t.updateTile("", Color.WHITE, Color.TRANSPARENT);
		t.updateText(p.levelUpOptionsListed());
		newScreen = true;
		screenState = "levelup";
	}

	public void drawPlayerStatus(int n1, int n2) {
		Tile blck = (Tile) gameArr.get(n1);
		blck.updateTile("", Color.BLACK, Color.BLACK);

		Tile t = (Tile) gameArr.get(n2);
		int translateX = Settings.TILE_SIZE();
		int translateY = Settings.TILE_SIZE();
		Pos alignment = Pos.CENTER_RIGHT;
		t.updateTilePos(translateX, translateY, alignment);
		t.updateTile("", Color.WHITE, Color.TRANSPARENT);
		t.updateText(p.status());
	}

	public void drawHelpScreen(int n1, int n2) {
		List<String> helpArr = new ArrayList<>();
		helpArr.add("-- SweetToD HELP-PAGE --");
		helpArr.add("Ascend the Tower Of Desires to protect your desires, your sweet");
		helpArr.add("tooth! The monsters from the tower has banded together to steal");
		helpArr.add("steal all the candies your town owns! The more you ascend the");
		helpArr.add("tower, the more difficulty it gets. Use what you find to avoid ");
		helpArr.add("dying while ascending... Hoard back all the sweets!");
		helpArr.add("[g] or [,] to pick up");
		helpArr.add("[d] to drop");
		helpArr.add("[w] to equip an item");
		helpArr.add("[e] to eat food");
		helpArr.add("[s] to open status");
		helpArr.add("[u] to unequip");
		helpArr.add("[i] to open inventory");
		helpArr.add("[x] to examine your items");
		helpArr.add("[q] to quaff a potion");
		helpArr.add("[?] or [+] for help");
		helpArr.add("[PERIOD] or [.] to open game menu");
		helpArr.add("[ESC] exit out of displayed screens");
		helpArr.add("-- press any key to continue --");
		Tile blck = (Tile) gameArr.get(n1);
		blck.updateTile("", Color.BLACK, Color.BLACK);
		Tile t = (Tile) gameArr.get(n2);
		t.updateTile("", Color.WHITE, Color.TRANSPARENT);
		int translateX = Settings.TILE_SIZE();
		int translateY = Settings.TILE_SIZE() / 2;
		Pos alignment = Pos.CENTER_RIGHT;
		t.updateTilePos(translateX, translateY, alignment);
		t.updateText(helpArr);
	}

	public void drawGameOverScreen(int n1, int n2) {
		List<String> deathArr = new ArrayList<>();
		deathArr.add("-- YOU DID NOT SURVIVE!!! --");
		deathArr.add("Level " + p.getStringStat("lvl"));
		deathArr.add(p.getDeathMessage());
		deathArr.add(p.getDeathInfo());
		deathArr.add("[ESC] to return to main menu");
		Tile blck = (Tile) gameArr.get(n1);
		blck.updateTile("", Color.BLACK, Color.BLACK);
		Tile t = (Tile) gameArr.get(n2);
		t.updateTile("", Color.WHITE, Color.TRANSPARENT);
		int translateX = Settings.TILE_SIZE();
		int translateY = Settings.TILE_SIZE() / 2;
		Pos alignment = Pos.CENTER_RIGHT;
		t.updateTilePos(translateX, translateY, alignment);
		t.updateText(deathArr);
		newScreen = true;
		screenState = "gameOver";
		p.gameOverScreenShown = true;
	}

	// RESETS!
	public void resetBlackScreen(int n) {
		Tile blck = (Tile) gameArr.get(n);
		blck.updateTile("", Color.BLACK, Color.TRANSPARENT);
	}

	public void resetScreenText(int n) {
		Tile t = (Tile) gameArr.get(n);
		t.updateTile("", Color.TRANSPARENT, Color.TRANSPARENT);
	}

	public void drawMenu(int n1, int n2) {
		List<String> menuArr = new ArrayList<>();
		menuArr.add("-- GAME MENU --");
		menuArr.add("1 : Exit to menu");
		menuArr.add("2 : Save character");
		menuArr.add("[ESC] to return back to game screen");
		Tile blck = (Tile) gameArr.get(n1);
		blck.updateTile("", Color.BLACK, Color.BLACK);
		Tile t = (Tile) gameArr.get(n2);
		t.updateTile("", Color.WHITE, Color.TRANSPARENT);
		int translateX = Settings.TILE_SIZE();
		int translateY = Settings.TILE_SIZE() / 2;
		Pos alignment = Pos.CENTER_RIGHT;
		t.updateTilePos(translateX, translateY, alignment);
		t.updateText(menuArr);
		newScreen = true;
		screenState = "menu";
	}
}
