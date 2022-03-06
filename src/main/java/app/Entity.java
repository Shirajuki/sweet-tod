package app;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import javafx.scene.paint.Color;

public class Entity {
	// Public
	public String[][] board;
	public int x = 14, y = 7;
	public boolean alive = true;
	public Color color = Color.GREEN;
	public String glyph = "f";
	public String name = "Unknown";
	// Public List and HashMaps
	public List<Tile> gameArr;
	public List<String> infoArr;
	public List<Entity> entityArr;
	public List<Item> itemArr;
	public List<Item> inventory = new ArrayList<>();
	public List<Item> equipment = new ArrayList<>();
	public HashMap<String, Double> stats = new HashMap<String, Double>();
	public HashMap<String, Double> bstats = new HashMap<String, Double>();
	// Private
	private Player p;
	private int candy = new Random().nextInt(10) + 1;
	private Buff buff = new Buff(null, null, null, 0);

	public Entity(String[][] b, Player p, List<Tile> gameArr, List<Entity> entityArr, List<String> infoArr,
			List<Item> itemArr) {
		this.board = b;
		this.p = p;
		this.gameArr = gameArr;
		this.entityArr = entityArr;
		this.infoArr = infoArr;
		this.itemArr = itemArr;
		// STATS
		this.stats.put("hp", 10.0);
		this.stats.put("hpMax", 10.0);
		this.stats.put("atk", 1.0);
		this.stats.put("def", 1.0);
		this.stats.put("lvl", 1.0);
		this.stats.put("exp", 1.0);
	}

	public Entity(String[][] b, List<Tile> gameArr, List<Entity> entityArr, List<String> infoArr, List<Item> itemArr) {
		this.board = b;
		this.gameArr = gameArr;
		this.entityArr = entityArr;
		this.infoArr = infoArr;
		this.itemArr = itemArr;
	}

	public void initEquipmentSpace(int n) {
		for (int i = 0; i < n; i++) {
			equipment.add(new Item("/", Color.WHITE, "None", false, false, false));
		}
	}

	public void initInventorySpace(int n) {
		// inventory.add(new Item("?",Color.RED,"TestItem",true,true,true));
	}

	public void addExp(double amt) {
		this.stats.put("exp", this.stats.get("exp") + amt);
	}

	public int candy() {
		return this.candy;
	}

	public void drop(Item item) {
		Item i = new Item(item);
		i.setPos(this.x, this.y);
		itemArr.add(i);
	}

	public boolean hasDrops() {
		return this.inventory.size() > 0;
	}

	public void addDropTable(Item item) {
		this.inventory.add(item);
	}

	public void dropItemFromDropTable() {
		int rnd = new Random().nextInt(1);
		if (rnd < this.inventory.size()) {
			Item item = this.inventory.get(rnd);
			drop(item);
		}

	}

	public List<Item> getEquipment() {
		List<Item> invArr = new ArrayList<>();
		for (int i = 0; i < this.equipment.size(); i++) {
			if (!this.equipment.get(i).name().equals("None")) {
				invArr.add(this.equipment.get(i));
			}
		}
		return invArr;
	}

	public List<String> getEquipmentListed() {
		List<String> invArr = new ArrayList<>();
		int c = 1;
		for (int i = 0; i < this.equipment.size(); i++) {
			if (!this.equipment.get(i).name().equals("None")) {
				invArr.add(c + ": " + this.equipment.get(i).name());
				c++;
				if (c == 10)
					c = 0;
			}
		}
		return invArr;
	}

	public List<String> getInventoryListed(String filter) {
		List<String> invArr = new ArrayList<>();
		int c = 1;
		for (int i = 0; i < this.inventory.size(); i++) {
			if (filter.equals("listed")) {
				invArr.add(this.inventory.get(i).name());
			} else {
				if (this.inventory.get(i).hasModifier(filter)) {
					invArr.add(c + ": " + this.inventory.get(i).name());
					c++;
					if (c == 10)
						c = 0;
				}
			}
		}
		return invArr;
	}

	public List<Item> getInventory(String filter) {
		List<Item> invArr = new ArrayList<>();
		for (int i = 0; i < this.inventory.size(); i++) {
			if (this.inventory.get(i).hasModifier(filter)) {
				invArr.add(this.inventory.get(i));
			}
		}
		return invArr;
	}

	public void assignMonster(int x, int y, String glyph, String name, Color color, double hp, double atk, double def) {
		this.x = x;
		this.y = y;
		// DISPLAY
		this.glyph = glyph;
		this.name = name;
		this.color = color;
		// STATS
		this.stats.put("hpMax", hp);
		this.stats.put("atk", atk);
		this.stats.put("def", def);
		// BONUS STATS
		this.bstats.put("hpMax", 0.0);
		this.bstats.put("atk", 0.0);
		this.bstats.put("def", 0.0);
	}

	public boolean isPlayer(int x, int y) {
		return (p.x == x && p.y == y);
	}

	private boolean isEntity(int x, int y) {
		for (int i = entityArr.size() - 1; i >= 0; i--) {
			Entity e = entityArr.get(i);
			if (e.alive) {
				if (e.x == x && e.y == y) {
					return true;
				}
			}
		}
		return false;
	}

	public void attacked(int atk) {
		Double d = this.stats.get("hp");
		d -= atk;
		this.stats.put("hp", d);
		if (this.stats.get("hp") <= 0)
			this.alive = false;
	}

	private boolean attacked = false;
	private boolean pathfindingFound = false;

	public void movePattern() {
		if (this.name.equals("Bat") || this.name.equals("Rat")) {
			this.batAI();
		} else if (this.name.equals("Slime")) {
			this.slimeAI();
		} else if (this.name.equals("Zombie")) {
			this.zombieAI();
		} else if (this.name.equals("Goblin")) {
			this.goblinAI();
		} else if (this.name.equals("Spider") || this.name.equals("Giant Rat")) {
			this.spiderAI();
		}
	}

	// AIs
	public void batAI() {
		int rndLoop = 1 + new Random().nextInt(2);
		for (int i = 0; i < rndLoop; i++) {
			if (this.attacked) {
				this.attacked = false;
				break;
			}
			int rnd = new Random().nextInt(7);
			if (rnd == 1) {
				moveX(1);
			} else if (rnd == 2) {
				moveX(-1);
			} else if (rnd == 3) {
				moveY(1);
			} else if (rnd == 4) {
				moveY(-1);
			}
		}
	}

	public void slimeAI() {
		for (int i = 0; i < 2; i++) {
			if (this.pathfindingFound || this.attacked) {
				this.pathfindingFound = false;
				this.attacked = false;
				break;
			}
			int rnd = new Random().nextInt(7);
			boolean wlked = false;
			if (rnd == 0) {
				if (this.x < p.x) {
					moveX(1);
				}
				wlked = true;
			} else if (rnd == 1) {
				if (this.x > p.x) {
					moveX(-1);
				}
				wlked = true;
			} else if (rnd == 2) {
				if (this.y < p.y) {
					moveY(1);
				}
				wlked = true;
			} else if (rnd == 3) {
				if (this.y > p.y) {
					moveY(-1);
				}
				wlked = true;
			}
			if (!wlked) {
				if (this.x < p.x) {
					moveX(1);
				} else if (this.x > p.x) {
					moveX(-1);
				} else if (this.y < p.y) {
					moveY(1);
				} else if (this.y > p.y) {
					moveY(-1);
				}
			}
		}
	}

	public void spiderAI() {
		for (int i = 0; i < 3; i++) {
			if (this.pathfindingFound || this.attacked) {
				this.pathfindingFound = false;
				this.attacked = false;
				break;
			}
			int rnd = new Random().nextInt(5);
			boolean wlked = false;
			if (rnd == 0) {
				if (this.x < p.x) {
					moveX(1);
				}
				wlked = true;
			} else if (rnd == 1) {
				if (this.x > p.x) {
					moveX(-1);
				}
				wlked = true;
			} else if (rnd == 2) {
				if (this.y < p.y) {
					moveY(1);
				}
				wlked = true;
			} else if (rnd == 3) {
				if (this.y > p.y) {
					moveY(-1);
				}
				wlked = true;
			}
			if (!wlked) {
				if (this.x < p.x) {
					moveX(1);
				} else if (this.x > p.x) {
					moveX(-1);
				} else if (this.y < p.y) {
					moveY(1);
				} else if (this.y > p.y) {
					moveY(-1);
				}
			}
		}
	}

	public void zombieAI() {
		for (int i = 0; i < 4; i++) {
			if (this.pathfindingFound || this.attacked) {
				this.pathfindingFound = false;
				this.attacked = false;
				break;
			}
			int rnd = new Random().nextInt(6);
			boolean wlked = false;
			if (rnd == 0) {
				if (this.x < p.x) {
					moveX(1);
				}
				wlked = true;
			} else if (rnd == 1) {
				if (this.x > p.x) {
					moveX(-1);
				}
				wlked = true;
			} else if (rnd == 2) {
				if (this.y < p.y) {
					moveY(1);
				}
				wlked = true;
			} else if (rnd == 3) {
				if (this.y > p.y) {
					moveY(-1);
				}
				wlked = true;
			}
			if (!wlked) {
				if (this.x < p.x) {
					moveX(1);
				} else if (this.x > p.x) {
					moveX(-1);
				} else if (this.y < p.y) {
					moveY(1);
				} else if (this.y > p.y) {
					moveY(-1);
				}
			}
		}
	}

	public void goblinAI() {
		for (int i = 0; i < 7; i++) {
			if (this.pathfindingFound || this.attacked) {
				this.pathfindingFound = false;
				this.attacked = false;
				break;
			}
			int rnd = new Random().nextInt(4);
			boolean wlked = false;
			if (rnd == 0) {
				if (this.x < p.x) {
					moveX(1);
				}
				wlked = true;
			} else if (rnd == 1) {
				if (this.x > p.x) {
					moveX(-1);
				}
				wlked = true;
			} else if (rnd == 2) {
				if (this.y < p.y) {
					moveY(1);
				}
				wlked = true;
			} else if (rnd == 3) {
				if (this.y > p.y) {
					moveY(-1);
				}
				wlked = true;
			}
			if (!wlked) {
				if (this.x < p.x) {
					moveX(1);
				} else if (this.x > p.x) {
					moveX(-1);
				} else if (this.y < p.y) {
					moveY(1);
				} else if (this.y > p.y) {
					moveY(-1);
				}
			}
		}
	}

	public void moveX(int n) {
		if (isPlayer(this.x + n, this.y)) {
			this.attackTile(this.x + n, this.y);
			this.attacked = true;
		} else if (board[this.y][this.x + n].equals(".") && !isEntity(this.x + n, this.y)) {
			this.x += n;
			pathfindingFound = true;
		}
	}

	public void moveY(int n) {
		if (isPlayer(this.x, this.y + n)) {
			this.attackTile(this.x, this.y + n);
			this.attacked = true;
		} else if (board[this.y + n][this.x].equals(".") && !isEntity(this.x, this.y + n)) {
			this.y += n;
			pathfindingFound = true;
		}
	}

	public Buff buff() {
		return this.buff;
	}

	public void modifyBuff(Buff b) {
		this.buff = b;
	}

	public void attackTile(int x, int y) {
		int dmg = Math.max(0,
				(int) ((this.stats.get("atk") + this.bstats.get("atk")) - (p.stats.get("def") + p.bstats.get("def"))));
		dmg = (int) (Math.random() * dmg) + 1;
		p.attacked(dmg);
		String text = this.name + " attacked you for " + dmg + " damage.";
		infoArr.add(text);
		// Inflict debuff
		int rnd = new Random().nextInt(5);
		if (rnd == 1 && this.buff.maxSteps() > 0) {
			p.addBuff(this.buff);
			infoArr.add("You got infected by the debuff " + this.buff.name() + "!");
		}
		if (p.stats.get("hp") <= 0) {
			p.setDeathMessage("Killed by " + this.name);
		}
		Tile t = (Tile) gameArr.get(0);
		t.updateText(infoArr);
	}
}
