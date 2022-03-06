package app;

import java.io.IOException;
import java.util.List;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.paint.Color;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.layout.VBox;
import javafx.event.EventHandler;

public class GameController {
	@FXML
	Button newGameBtn;
	@FXML
	Button loadGameBtn;
	@FXML
	Button leaderboardBtn;
	@FXML
	Button exitBtn;
	private Game g = new Game();

	@FXML
	void initialize() {
		newGameBtn.setOnAction(e -> menu(0));
		loadGameBtn.setOnAction(e -> menu(1));
		leaderboardBtn.setOnAction(e -> menu(2));
		exitBtn.setOnAction(e -> menu(3));
	}

	private void startGame() {
		System.out.println("[Start new game]: starting...");
		Scene scene = new Scene(g.gamePaneElements, Settings.GAME_WIDTH(), Settings.GAME_HEIGHT(), Color.BLACK);
		scene.getStylesheets().add(getClass().getResource("application2.css").toExternalForm());
		Stage stage = ((Stage) newGameBtn.getScene().getWindow());
		stage.setTitle("IN GAME");
		stage.setScene(scene);
		scene.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				// System.out.println(event.getText() + " - " + event.getCode());
				if (g.gameStarted) {
					// New Screens
					if (g.screen.newScreen()) {
						switch (event.getCode()) {
							case DIGIT1:
								g.newInputScreen(0);
								break;
							case DIGIT2:
								g.newInputScreen(1);
								break;
							case DIGIT3:
								g.newInputScreen(2);
								break;
							case DIGIT4:
								g.newInputScreen(3);
								break;
							case DIGIT5:
								g.newInputScreen(4);
								break;
							case DIGIT6:
								g.newInputScreen(5);
								break;
							case DIGIT7:
								g.newInputScreen(6);
								break;
							case DIGIT8:
								g.newInputScreen(7);
								break;
							case DIGIT9:
								g.newInputScreen(8);
								break;
							case DIGIT0:
								g.newInputScreen(9);
								break;
							case ESCAPE:
								g.resetLoop();
								break;
						}
					} else {
						// Movement
						g.infoArr.clear();
						// GUI Controls
						switch (event.getText()) {
							case "?":
								g.screen.drawHelpScreen(3, 4);
								break; // Open help
							case "+":
								g.screen.drawHelpScreen(3, 4);
								break; // Open help
							case "g":
								g.p.pickup();
								break; // Pickup Item
							case ",":
								g.p.pickup();
								break; // Pickup Item
							case "d":
								g.screen.drawPlayerInventory(2, "drop");
								break; // Open inventory (all [drop])
							case "u":
								g.screen.drawPlayerInventory(2, "unequip");
								break; // Open inventory (unequipable)
							case "w":
								g.screen.drawPlayerInventory(2, "canEquip");
								break; // Open inventory (equipable)
							case "i":
								g.screen.drawPlayerInventory(2, "listed");
								break; // Open inventory (all [listed])
							case "s":
								g.screen.drawPlayerStatus(3, 5);
								break; // Open player status
							case "e":
								g.screen.drawPlayerInventory(2, "canEat");
								break; // Open inventory (eatable)
							case "x":
								g.screen.drawPlayerInventory(2, "examine");
								break; // Open inventory (all [examine])
							case "q":
								g.screen.drawPlayerInventory(2, "canDrink");
								break; // Open inventory (drinkable)
							default:
								g.resetLoop();
								break;
						}
						switch (event.getCode()) {
							case UP:
								g.p.moveY(-1);
								break;
							case DOWN:
								g.p.moveY(1);
								break;
							case LEFT:
								g.p.moveX(-1);
								break;
							case RIGHT:
								g.p.moveX(1);
								break;
							case K:
								g.p.moveY(-1);
								break;
							case J:
								g.p.moveY(1);
								break;
							case H:
								g.p.moveX(-1);
								break;
							case L:
								g.p.moveX(1);
								break;
							case PERIOD:
								g.screen.drawMenu(3, 8);
								break; // Open menu
							case SPACE:
								System.out.println(g.p.x + "-" + g.p.y);
								break;
						}
						g.gameLoop();
					}
					if (g.returnToMainMenu) {
						backGameMenu();
					}
				}
			}
		});
		g.createGameAll();
		g.p.setPlayerRadius();
		g.drawGameBoard();
	}

	public void backGameMenu() {
		try {
			gameMenu();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void backGameMenu2() {
		try {
			gameMenu2();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void gameMenu() throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("App.fxml")));
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = ((Stage) g.gameArr.get(0).getScene().getWindow());
		stage.setTitle("MENU");
		stage.setScene(scene);
	}

	public void gameMenu2() throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("App.fxml")));
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = ((Stage) newGameBtn.getScene().getWindow());
		stage.setTitle("MENU");
		stage.setScene(scene);
	}

	public void leaderboard(List<String> ls) throws IOException {
		Scene scene = new Scene(FXMLLoader.load(getClass().getResource("leaderboard.fxml")));
		scene.getStylesheets().add(getClass().getResource("application.css").toExternalForm());
		Stage stage = ((Stage) newGameBtn.getScene().getWindow());
		stage.setTitle("LEADERBOARD");
		stage.setScene(scene);
		VBox box = (VBox) scene.lookup("#vbox");
		// Add texts!
		for (int i = 0; i < ls.size(); i++) {
			Text t = new Text();
			t.setFont(Font.font(20));
			t.setText(ls.get(i));
			box.getChildren().add(t);
		}
	}

	public void menu(int n) {
		switch (n) {
			case 0:
				g = new Game();
				g.gameStarted = true;
				startGame();
				System.out.println("[Start new game]: initialized!");
				break;
			case 1:
				System.out.println("[Load saved game]: reading file...");
				if (g.loadChar())
					startGame();
				break;
			case 2:
				System.out.println("[Leaderboard]: reading file...");
				try {
					leaderboard(g.save.listedLeaderboard());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			default:
				System.out.println("[Exit]: exiting...");
				// exitBtn.getScene().getWindow().hide();
				Platform.exit();
				System.exit(0);
				break;
		}
	}
}