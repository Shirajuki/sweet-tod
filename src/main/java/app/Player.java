package app;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

public class Player extends Entity {
	// Public
	public int x = 14, y = 7;
	public int rx = 0, ry = 0;
	public Color color = Color.WHITE;
	public String glyph = "@";
	public boolean ascended = false;
	public boolean gameOverScreenShown = false;
	public int floor = 0;
	public int candy = 10;
	public String name = "Bob", race = "Human";
	// Private
	private final int MAX_INVENTORY_SPACE = 10;
	private int steps = 0;
	private int killCount = 0;
	private int duplicateBuffIndex = 0;
	private String deathMsg;
	private List<String> radius = new ArrayList<>();
	private List<Buff> buffs = new ArrayList<>();

	public Player(String[][] b, List<Tile> gameArr, List<Entity> entityArr, List<String> infoArr, List<Item> itemArr) {
		super(b, gameArr, entityArr, infoArr, itemArr);
		this.board = b;
		this.gameArr = gameArr;
		this.entityArr = entityArr;
		this.infoArr = infoArr;
		this.itemArr = itemArr;
		// INFO
		this.name = "Alpha";
		this.race = "Tester";
		// STATS
		this.stats.put("hp", 100.0);
		this.stats.put("hpMax", 100.0);
		this.stats.put("lvl", 1.0);
		this.stats.put("exp", 1.0);
		this.stats.put("atk", 1.0);
		this.stats.put("def", 1.0);
		this.stats.put("lck", 1.0);
		this.stats.put("viewRange", 6.0);
		// BONUS STATS
		this.bstats.put("hpMax", 0.0);
		this.bstats.put("atk", 0.0);
		this.bstats.put("def", 0.0);
		this.bstats.put("lck", 0.0);
		this.bstats.put("viewRange", 0.0);
		// SPAWN
		this.spawnPos();
		// INITS
		this.initInventorySpace(MAX_INVENTORY_SPACE);
		this.initEquipmentSpace(8);
	}

	// LOAD PLAYER
	public void loadSaveFile(List<String> ls) {
		this.name = ls.get(0);
		this.race = ls.get(1);
		// STATS
		this.floor = Integer.parseInt(ls.get(2));
		this.steps = Integer.parseInt(ls.get(3));
		this.killCount = Integer.parseInt(ls.get(4));
		// STATS
		this.stats.put("hp", Double.parseDouble(ls.get(11)));
		this.stats.put("hpMax", Double.parseDouble(ls.get(6)));
		this.stats.put("lvl", Double.parseDouble(ls.get(7)));
		this.stats.put("exp", Double.parseDouble(ls.get(13)));
		this.stats.put("atk", Double.parseDouble(ls.get(12)));
		this.stats.put("def", Double.parseDouble(ls.get(8)));
		this.stats.put("lck", Double.parseDouble(ls.get(9)));
		this.stats.put("viewRange", Double.parseDouble(ls.get(10)));
		// BONUS STATS
		this.bstats.put("hpMax", Double.parseDouble(ls.get(15)));
		this.bstats.put("atk", Double.parseDouble(ls.get(19)));
		this.bstats.put("def", Double.parseDouble(ls.get(16)));
		this.bstats.put("lck", Double.parseDouble(ls.get(17)));
		this.bstats.put("viewRange", Double.parseDouble(ls.get(18)));
		// SPAWN
		this.spawnPos();
		// INITS
		int localCheck = 0;
		this.inventory = new ArrayList<>();
		this.equipment = new ArrayList<>();
		for (int i = 21; i < ls.size(); i++) {
			if (ls.get(i).equals("")) {
				localCheck++;
				continue;
			}
			if (localCheck == 0) {
				this.inventory.add(new Item(ls.get(i).split(",")));
			} else {
				this.equipment.add(new Item(ls.get(i).split(",")));
			}
		}
	}

	public List<String> getPlayerSaveInfo() {
		List<String> ls = new ArrayList<>();
		ls.add(name);
		ls.add(race);
		ls.add("" + floor);
		ls.add("" + steps);
		ls.add("" + killCount);
		ls.add("");
		for (String key : this.stats.keySet()) {
			ls.add("" + this.stats.get(key));
		}
		ls.add("");
		for (String key : this.bstats.keySet()) {
			ls.add("" + this.bstats.get(key));
		}
		ls.add("");
		for (int i = 0; i < this.inventory.size(); i++) {
			ls.add(this.inventory.get(i).toString());
		}
		ls.add("");
		for (int i = 0; i < this.equipment.size(); i++) {
			ls.add(this.equipment.get(i).toString());
		}
		return ls;
	}

	public void updateBoard(String[][] b) {
		this.board = b;
	}

	private void ascendFloor() {
		this.floor++;
		this.ascended = true;
	}

	public void spawnPos() {
		for (int y = 0; y < board.length; y++) {
			for (int x = 0; x < board[0].length; x++) {
				if (board[y][x].equals(".")) {
					this.x = x;
					this.y = y;
				}
			}
		}
	}

	public int getTotalStat(String stat) {
		int amt1 = this.stats.get(stat).intValue();
		int amt2 = this.bstats.get(stat).intValue();
		return amt1 + amt2;
	}

	private boolean checkIsEntityAndAtk(int x, int y) {
		for (int i = entityArr.size() - 1; i >= 0; i--) {
			Entity e = entityArr.get(i);
			if (e.alive) {
				if (e.x == x && e.y == y) {
					int dmg = Math.max(0,
							(int) ((this.stats.get("atk") + this.bstats.get("atk")) - (e.stats.get("def") + e.bstats.get("def"))));
					dmg = (int) (Math.random() * dmg) + 1;
					e.attacked(dmg);
					String text = "Attacked " + e.name + " for " + dmg + " damage.";
					// Update info text
					infoArr.add(text);
					Tile t = (Tile) gameArr.get(0);
					t.updateText(infoArr);
					return true;
				}
			}
		}
		return false;
	}

	@Override
	public void drop(Item item) {
		// Make copy of the item, then removes it.
		Item i = new Item(item);
		i.setPos(this.x, this.y);
		infoArr.add("You dropped " + item.name());
		itemArr.add(i);
		this.inventory.remove(item);
	}

	public void equip(Item item) {
		// Checks for items available slots and open slots
		int itemSlot = item.itemSlot();
		int itemSlot2 = item.itemSlot2();
		boolean openSlot1 = equipment.get(itemSlot).name().equals("None");
		// If the item canEquip only on 1 hand
		if (itemSlot2 == -1) {
			if (!openSlot1) {
				this.inventory.add((Item) equipment.get(itemSlot));
			}
			// If the item canEquip as dual-hand
			if (item.dualHanded()) {
				if (!equipment.get(3).name().equals("None")) {
					this.inventory.add((Item) equipment.get(3));
				}
			}
			// If the item canEquip as right Hand, then replace dual-hand if found
			if (itemSlot == 3) {
				if (equipment.get(1).dualHanded()) {
					this.inventory.add((Item) equipment.get(1));
					equipment.set(1, new Item("?", Color.WHITE, "None", false, false, false));
				}
			}
			// If the item canEquip as left Hand, then replace dual-hand if found
			if (itemSlot == 1) {
				if (equipment.get(itemSlot).dualHanded()) {
					equipment.set(3, new Item("?", Color.WHITE, "None", false, false, false));
				}
			}
			//
			equipment.set(itemSlot, item);
		}
		// If the item canEquip on either left or right / both hands
		else {
			boolean openSlot2 = equipment.get(itemSlot2).name().equals("None");
			if (openSlot1) {
				equipment.set(itemSlot, item);
			} else {
				if (openSlot2) {
					equipment.set(itemSlot2, item);
				} else {
					this.inventory.add((Item) equipment.get(itemSlot));
					equipment.set(itemSlot, item);
				}
			}
		}
		// Remove equipped item from inventory, and send message.
		this.inventory.remove(item);
		infoArr.add("You equipped " + item.name());
		this.updateEquipmentStat();
	}

	public void examine(Item item) {
		String text = item.details() + ".";
		infoArr.add(text);
	}

	public void eat(Item item) {
		infoArr.add("You ate " + item.name() + ".");
		infoArr.add("Healed for " + item.healingValue() + " hp.");
		this.stats.put("hp", this.stats.get("hp") + item.healingValue());
		if (this.stats.get("hp") > this.stats.get("hpMax"))
			this.stats.put("hp", this.stats.get("hpMax"));
		if (item.hasBuff()) {
			addBuffs(item.buffs());
		}
		this.inventory.remove(item);
	}

	public void addBuff(Buff b) {
		if (hasBuffAlready(b)) {
			updateBuff(b);
		} else {
			this.buffs.add(b);
		}
		updateEquipmentStat();
	}

	public boolean hasBuffAlready(Buff b2) {
		for (int i = 0; i < this.buffs.size(); i++) {
			Buff b = buffs.get(i);
			if (b2.name().equals(b.name())) {
				duplicateBuffIndex = i;
				return true;
			}
		}
		return false;
	}

	public void updateBuff(Buff b) {
		this.buffs.set(duplicateBuffIndex, b);
	}

	public void addBuffs(List<Buff> bb) {
		for (int i = 0; i < bb.size(); i++) {
			Buff b = bb.get(i);
			addBuff(b);
		}
	}

	public void quaff(Item item) {
		infoArr.add("You drank " + item.name() + ".");
		this.inventory.remove(item);
		if (item.hasBuff()) {
			addBuffs(item.buffs());
		}
	}

	public void unequip(Item item) {
		int itemSlot = item.itemSlot();
		this.inventory.add((Item) equipment.get(itemSlot));
		equipment.set(itemSlot, new Item("?", Color.WHITE, "None", false, false, false));
		infoArr.add("You unequipped " + item.name());
		this.updateEquipmentStat();
	}

	public void updateEquipmentStat() {
		this.bstats.put("hpMax", 0.0);
		this.bstats.put("atk", 0.0);
		this.bstats.put("def", 0.0);
		this.bstats.put("lck", 0.0);
		this.bstats.put("viewRange", 0.0);
		for (int i = 0; i < this.equipment.size(); i++) {
			Item item = this.equipment.get(i);
			if (!item.name().equals("None")) {
				if (item.vitalityValue() != 0)
					this.bstats.put("hpMax", this.bstats.get("hpMax") + item.vitalityValue());
				if (item.attackValue() != 0)
					this.bstats.put("atk", this.bstats.get("atk") + item.attackValue());
				if (item.defenseValue() != 0)
					this.bstats.put("def", this.bstats.get("def") + item.defenseValue());
				if (item.luckValue() != 0)
					this.bstats.put("lck", this.bstats.get("lck") + item.luckValue());
			}
		}
		updateBuffStat();
	}

	public void updateBuffStat() {
		for (int i = 0; i < this.buffs.size(); i++) {
			Buff buff = this.buffs.get(i);
			if (!buff.name().equals("None")) {
				if (buff.vitalityValue() != 0)
					this.bstats.put("hpMax", this.bstats.get("hpMax") + buff.vitalityValue());
				if (buff.attackValue() != 0)
					this.bstats.put("atk", this.bstats.get("atk") + buff.attackValue());
				if (buff.defenseValue() != 0)
					this.bstats.put("def", this.bstats.get("def") + buff.defenseValue());
				if (buff.luckValue() != 0)
					this.bstats.put("lck", this.bstats.get("lck") + buff.luckValue());
				if (buff.viewRangeValue() != 0)
					this.bstats.put("viewRange", this.bstats.get("viewRange") + buff.viewRangeValue());
			}
		}
	}

	public void pickup() {
		for (int i = itemArr.size() - 1; i >= 0; i--) {
			Item item = itemArr.get(i);
			if (!item.pickedUp) {
				if (item.x == this.x && item.y == this.y) {
					if (item.glyph().equals("<")) {
						ascendFloor();
					} else {
						if (this.inventory.size() >= MAX_INVENTORY_SPACE) {
							infoArr.add("Inventory is full, cannot pick up.");
							break;
						} else {
							String text = "Picked up " + item.name() + ".";
							infoArr.add(text);
							Tile t = (Tile) gameArr.get(0);
							t.updateText(infoArr);
							item.pickedUp = true;
							this.inventory.add(item);
							break;
						}
					}
				}
			}
		}
	}

	public String getStringTotalStat(String stat) {
		int amt1 = this.stats.get(stat).intValue();
		int amt2 = this.bstats.get(stat).intValue();
		return "" + (amt1 + amt2);
	}

	public String getStringBonusStat(String stat) {
		int amt = this.bstats.get(stat).intValue();
		String s = "";
		if (amt > 0) {
			s = "(+" + amt + ")";
		} else if (amt < 0) {
			s = "(" + amt + ")";
		}
		return s;
	}

	public String getStringStat(String stat) {
		return "" + this.stats.get(stat).intValue();
	}

	public String getStringEquipment(int slot) {
		Item item = this.equipment.get(slot);
		String s = item.name();
		if (this.equipment.get(1).dualHanded() && slot == 3) {
			s = this.equipment.get(1).name();
		}
		return s;
	}

	public void stepBuffCountUpdate() {
		for (int i = this.buffs.size() - 1; i >= 0; i--) {
			Buff b = this.buffs.get(i);
			b.addSteps(1);
			if (b.steps() >= b.maxSteps()) {
				this.buffs.remove(i);
				infoArr.add("The buff " + b.name() + " has gone out!");
				updateEquipmentStat();
			}
		}
	}

	public List<String> status() {
		List<String> s = new ArrayList<>();
		String name = " ".repeat(9 - this.name.length()) + this.name;
		String race = " ".repeat(9 - this.race.length()) + this.race;
		String hp = " ".repeat(9 - (getStringStat("hp") + "/" + getStringTotalStat("hpMax")).length())
				+ (getStringStat("hp") + "/" + getStringStat("hpMax"));
		String lvl = " ".repeat(9 - getStringStat("lvl").length()) + getStringStat("lvl");
		String exp = " ".repeat(9 - getStringStat("exp").length()) + getStringStat("exp");
		String atk = " ".repeat(9 - (getStringStat("atk") + getStringBonusStat("atk")).length()) + getStringStat("atk")
				+ getStringBonusStat("atk");
		String def = " ".repeat(9 - (getStringStat("def") + getStringBonusStat("def")).length()) + getStringStat("def")
				+ getStringBonusStat("def");
		String lck = " ".repeat(9 - (getStringStat("lck") + getStringBonusStat("lck")).length()) + getStringStat("lck")
				+ getStringBonusStat("lck");
		String cdy = " ".repeat(7 - ("" + this.candy).length()) + this.candy;

		String[] equip = {
				getStringEquipment(0) + " ".repeat(17 - getStringEquipment(0).length()),
				getStringEquipment(1) + " ".repeat(17 - getStringEquipment(1).length()),
				getStringEquipment(2) + " ".repeat(17 - getStringEquipment(2).length()),
				getStringEquipment(3) + " ".repeat(17 - getStringEquipment(3).length()),
				getStringEquipment(4) + " ".repeat(17 - getStringEquipment(4).length()),
				getStringEquipment(5) + " ".repeat(17 - getStringEquipment(5).length()),
				getStringEquipment(6) + " ".repeat(17 - getStringEquipment(6).length()),
				getStringEquipment(7) + " ".repeat(17 - getStringEquipment(7).length()),
		};

		String[] buff = {
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
				" ".repeat(16),
		};
		for (int i = this.buffs.size() - 1; i >= 0; i--) {
			String bname = this.buffs.get(i).name();
			buff[this.buffs.size() - 1 - i] = bname + " ".repeat(16 - bname.length());
		}
		s.add("+----------------+-----------------------+-----------------+");
		s.add("|      Info:     |       Equipment:      |      Buffs:     |");
		s.add("+----------------+-----------------------+-----------------+");
		s.add("| NAME:" + name + " |        [a]            | " + buff[0] + "|");
		s.add("| RACE:" + race + " |     [b][c][d] [g]     | " + buff[1] + "|");
		s.add("| HP:  " + hp + " |        [e]    [h]     | " + buff[2] + "|");
		s.add("| LVL: " + lvl + " |      [f] [f]          | " + buff[3] + "|");
		s.add("| EXP: " + exp + " |                       | " + buff[4] + "|");
		s.add("| ATK: " + atk + " |  a: " + equip[0] + " | " + buff[5] + "|");
		s.add("| DEF: " + def + " |  b: " + equip[1] + " | " + buff[6] + "|");
		s.add("| LCK: " + lck + " |  c: " + equip[2] + " | " + buff[7] + "|");
		s.add("| CANDY: " + cdy + " |  d: " + equip[3] + " | " + buff[8] + "|");
		s.add("|                |  e: " + equip[4] + " | " + buff[9] + "|");
		s.add("|                |  f: " + equip[5] + " |                 |");
		s.add("|                |  g: " + equip[6] + " |                 |");
		s.add("|                |  h: " + equip[7] + " |                 |");
		s.add("+----------------+-----------------------+-----------------+");
		return s;
	}

	public void receiveCandy(int amt) {
		this.candy += amt;
	}

	public boolean receiveExp(double exp) {
		this.addExp(exp);
		// System.out.println(this.stats.get("exp")+" [EXP]
		// "+(nextLevel(this.stats.get("lvl").intValue())));
		if (Math.floor(this.stats.get("exp") / (nextLevel(this.stats.get("lvl").intValue()))) > 0) {
			this.levelUp();
			return true;
		}
		return false;
	}

	public int nextLevel(int level) {
		return (int) Math.round(0.04 * (Math.pow(level, 3)) + 0.8 * (Math.pow(level, 2)) + 2 * level) + 10;
	}

	public void levelUp() {
		this.stats.put("exp", this.stats.get("exp") % (nextLevel(this.stats.get("lvl").intValue())));
		this.stats.put("lvl", this.stats.get("lvl") + 1);
		this.stats.put("hp", this.stats.get("hpMax"));
	}

	public void modifyHP(int amt) {
		this.stats.put("hpMax", this.stats.get("hpMax") + amt);
		this.stats.put("hp", this.stats.get("hp") + amt);
		infoArr.add("You look healthier.");
	}

	public void modifyATK(int amt) {
		this.stats.put("atk", this.stats.get("atk") + amt);
		infoArr.add("You look a little stronger.");
	}

	public void modifyDEF(int amt) {
		this.stats.put("def", this.stats.get("def") + amt);
		infoArr.add("You look a little tougher.");
	}

	public void modifyLCK(int amt) {
		this.stats.put("lck", this.stats.get("lck") + amt);
		infoArr.add("You feel a little luckier.");
	}

	public void modifyViewRange(int amt) {
		if (this.stats.get("viewRange") < 10) {
			this.stats.put("viewRange", this.stats.get("viewRange") + amt);
			infoArr.add("You look a little more aware.");
		} else {
			randomSkillUp(3);
		}
	}

	public void randomSkillUp(int n) {
		int rnd = new Random().nextInt(n);
		modifyLCK(1);
		switch (rnd) {
			case 0:
				modifyHP(10);
				break;
			case 1:
				modifyATK(1);
				break;
			case 2:
				modifyDEF(1);
				break;
			default:
				modifyViewRange(1);
				break;
		}
	}

	public List<String> levelUpOptionsListed() {
		List<String> s = new ArrayList<>();
		s.add("LEVEL UP!");
		s.add("You leveled to " + this.stats.get("lvl").intValue() + "!");
		s.add("Choose which stat to skillup.");
		s.add("------------------------------");
		s.add("1 : to increase hit points");
		s.add("2 : to increase attack value");
		s.add("3 : to increase defense value");
		if (this.stats.get("viewRange") < 10) {
			s.add("4 : to increase view range");
			s.add("5-0 : to random skillup");
		} else {
			s.add("4-0 : to random skillup");
		}
		s.add("[ESC] to force cancel skilling up.");
		return s;
	}

	public boolean alive() {
		return this.alive;
	}

	public String getDeathMessage() {
		return this.deathMsg;
	}

	public void setDeathMessage(String msg) {
		this.deathMsg = msg;
	}

	public String getDeathInfo() {
		String s = "Lasted " + this.steps + " turns and " + this.floor + " floors. Killed " + this.killCount + " monsters.";
		return s;
	}

	@Override
	public void addExp(double amt) {
		this.stats.put("exp", this.stats.get("exp") + amt);
	}

	public void killedEnemy() {
		this.killCount++;
	}

	@Override
	public void attacked(int atk) {
		Double d = this.stats.get("hp");
		d -= atk;
		this.stats.put("hp", d);
		if (this.stats.get("hp") <= 0)
			this.alive = false;
	}

	@Override
	public void moveX(int n) {
		if (checkIsEntityAndAtk(this.x + n, this.y)) {
		} else if (!board[this.y][this.x + n].equals("#")) {
			this.x += n;
			this.steps++;
			stepBuffCountUpdate();
		}
	}

	@Override
	public void moveY(int n) {
		if (checkIsEntityAndAtk(this.x, this.y + n)) {
		} else if (!board[this.y + n][this.x].equals("#")) {
			this.y += n;
			this.steps++;
			stepBuffCountUpdate();
		}
	}

	public void setPlayerRadius() {
		this.radius.clear();
		for (int y = board.length - 1; y >= 0; y--) {
			for (int x = board[y].length; x >= 0; x--) {
				if (Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) <= Math.pow(getTotalStat("viewRange"), 2)
						&& Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) > Math.pow(getTotalStat("viewRange"), 2) * 0.6) {
					radius.add(x + "-" + y);
				}
			}
		}
	}

	public boolean inRadius(int x, int y) {
		return (Math.pow(this.x - x, 2) + Math.pow(this.y - y, 2) <= Math.pow(getTotalStat("viewRange"), 2));
	}

	public boolean inLineRadius(int x, int y) {
		for (int i = this.radius.size() - 1; i >= 0; i--) {
			String[] splitted = this.radius.get(i).split("-");
			this.rx = Integer.parseInt(splitted[0]);
			this.ry = Integer.parseInt(splitted[1]);
			if (this.rx == x && this.ry == y) {
				return true;
			}
		}
		return false;
	}
}
