package app;

import java.util.List;
import java.util.Random;
import javafx.scene.paint.Color;

// DONE!
public class Factory {
	private String[][] board;
	private Player p;
	private List<Tile> gameArr;
	private List<String> infoArr;
	private List<Entity> entityArr;
	private List<Item> itemArr;

	public Factory(String[][] b, Player p, List<Tile> gameArr, List<Entity> entityArr, List<String> infoArr,
			List<Item> itemArr) {
		this.board = b;
		this.p = p;
		this.gameArr = gameArr;
		this.entityArr = entityArr;
		this.infoArr = infoArr;
		this.itemArr = itemArr;
	}

	public void updateBoard(String[][] b) {
		this.board = b;
	}

	public String openSpot() {
		int rndX = new Random().nextInt(board[0].length);
		int rndY = new Random().nextInt(board.length);
		if (p.inRadius(rndX, rndY)) {
			return openSpot();
		}
		for (int y = rndY; y < board.length; y++) {
			for (int x = rndX; x < board[0].length; x++) {
				if (board[rndY][rndX].equals("."))
					return rndX + "-" + rndY;
				rndX = x;
				rndY = y;
			}
		}
		return openSpot();
	}

	public void openSpotMonster(Entity e, String glyph, String name, Color color, int hp, int atk, int def) {
		String[] splitted = openSpot().split("-");
		int x = Integer.parseInt(splitted[0]);
		int y = Integer.parseInt(splitted[1]);
		e.assignMonster(x, y, glyph, name, color, hp, atk, def);
	}

	public void openSpotItem(Item i) {
		String[] splitted = openSpot().split("-");
		int x = Integer.parseInt(splitted[0]);
		int y = Integer.parseInt(splitted[1]);
		i.setPos(x, y);
		if (i.glyph().equals("<"))
			System.out.println(x + "-STAIRS-" + y);
	}

	// MONSTERS
	public void newBat(int hp, int atk, int def) {
		Entity bat = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		bat.modifyBuff(addBuffBlindness(10, 1));
		openSpotMonster(bat, "B", "Bat", Color.GREENYELLOW, hp, atk, def);
		entityArr.add(bat);
	}

	public void newFungus(int hp, int atk, int def) {
		Entity fungus = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		openSpotMonster(fungus, "f", "Fungus", Color.GREEN, hp, atk, def);
		fungus.addDropTable(newMushroom());
		entityArr.add(fungus);
	}

	public void newGiantRat(int hp, int atk, int def) {
		Entity rat = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		openSpotMonster(rat, "R", "Giant Rat", Color.LIGHTCYAN, hp, atk, def);
		entityArr.add(rat);
	}

	public void newRat(int hp, int atk, int def) {
		Entity rat = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		rat.modifyBuff(addBuffWeakness(10, 1));
		openSpotMonster(rat, "r", "Rat", Color.LIGHTCYAN, hp, atk, def);
		entityArr.add(rat);
	}

	public void newSlime(int hp, int atk, int def) {
		Entity slime = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		openSpotMonster(slime, "S", "Slime", Color.CYAN, hp, atk, def);
		entityArr.add(slime);
	}

	public void newSpider(int hp, int atk, int def) {
		Entity spider = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		openSpotMonster(spider, "S", "Spider", Color.BLUE, hp, atk, def);
		spider.addDropTable(newSpiderGland());
		entityArr.add(spider);
	}

	public void newZombie(int hp, int atk, int def) {
		Entity zombie = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		zombie.modifyBuff(addBuffWeakness(10, 1));
		openSpotMonster(zombie, "Z", "Zombie", Color.WHITE, hp, atk, def);
		entityArr.add(zombie);
	}

	public void newGoblin(int hp, int atk, int def) {
		Entity goblin = new Entity(board, p, gameArr, entityArr, infoArr, itemArr);
		openSpotMonster(goblin, "G", "Goblin", Color.LIME, hp, atk, def);
		goblin.addDropTable(newBaguette());
		goblin.addDropTable(newIronChestplate());
		goblin.addDropTable(newIronLeggings());
		goblin.addDropTable(newIronSword());
		entityArr.add(goblin);
	}

	// BUFFS
	public Buff addBuffResistance(int steps, int level) {
		Buff b = new Buff("R", Color.AQUAMARINE, "Resistance", steps);
		b.modifyDefenseValue(3 * level);
		return b;
	}

	public Buff addBuffStrength(int steps, int level) {
		Buff b = new Buff("S", Color.AQUAMARINE, "Strength", steps);
		b.modifyAttackValue(3 * level);
		return b;
	}

	public Buff addBuffBlindness(int steps, int level) {
		Buff b = new Buff("B", Color.AQUAMARINE, "Blindness", steps);
		b.modifyViewRangeValue(-2 * level);
		return b;
	}

	public Buff addBuffHealthUp(int steps, int level) {
		Buff b = new Buff("H", Color.AQUAMARINE, "Health Up", steps);
		b.modifyVitalityValue(20 * level);
		return b;
	}

	public Buff addBuffWeakness(int steps, int level) {
		Buff b = new Buff("W", Color.AQUAMARINE, "Weakness", steps);
		b.modifyAttackValue(-2 * level);
		return b;
	}

	// GAME OBJECTS
	public void newStair() {
		Item i = new Item("<", Color.KHAKI, "Staircase up: [g] to ascend a floor.", true, false, false);
		openSpotItem(i);
		itemArr.add(i);
	}

	// MISC
	public Item newBrick() {
		Item i = new Item("∎", Color.BROWN, "Brick", true, true, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(1);
		i.modifyHealingValue(-10);
		i.addBuff(addBuffWeakness(20, 1));
		i.addBuff(addBuffResistance(20, 1));
		return i;
	}

	// LEFT-HANDED WEAPONS
	public Item newWoodenSword() {
		Item i = new Item("↑", Color.DARKGOLDENROD, "Wooden sword", true, false, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(2);
		return i;
	}

	public Item newIronSword() {
		Item i = new Item("↑", Color.LIGHTCYAN, "Iron sword", true, false, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(4);
		return i;
	}

	public Item newDagger() {
		Item i = new Item("↑", Color.LIGHTCYAN, "Dagger", true, false, false);
		i.modifyItemSlot(1);
		i.modifyItemSlot2(3);
		i.modifyAttackValue(2);
		return i;
	}

	public Item newSpear() {
		Item i = new Item("/", Color.LIGHTCYAN, "Spear", true, false, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(6);
		i.modifyDualHanded(true);
		return i;
	}

	// ARMOR
	public Item newMagicHat() {
		Item i = new Item("^", Color.BLUEVIOLET, "Magic hat", true, false, false);
		i.modifyItemSlot(0);
		i.modifyVitalityValue(10);
		i.modifyLuckValue(1);
		return i;
	}

	public Item newLeatherArmor() {
		Item i = new Item("(", Color.DARKGOLDENROD, "Leather armor", true, false, false);
		i.modifyItemSlot(2);
		i.modifyDefenseValue(2);
		return i;
	}

	public Item newHelmet() {
		Item i = new Item("∆", Color.LIGHTCYAN, "Helmet", true, false, false);
		i.modifyItemSlot(0);
		i.modifyDefenseValue(2);
		return i;
	}

	public Item newIronChestplate() {
		Item i = new Item("[", Color.LIGHTCYAN, "Iron Chestplate", true, false, false);
		i.modifyItemSlot(2);
		i.modifyDefenseValue(6);
		return i;
	}

	public Item newIronLeggings() {
		Item i = new Item("[", Color.LIGHTCYAN, "Iron Leggings", true, false, false);
		i.modifyItemSlot(4);
		i.modifyDefenseValue(4);
		return i;
	}

	public Item newWoodenShield() {
		Item i = new Item("ᗢ", Color.DARKGOLDENROD, "Wooden Shield", true, false, false);
		i.modifyItemSlot(3);
		i.modifyDefenseValue(2);
		return i;
	}

	// RINGS
	public Item newRingOfProtection() {
		Item i = new Item("=", Color.GOLD, "Ring of Prot", true, false, false);
		i.modifyItemSlot(6);
		i.modifyItemSlot2(7);
		i.modifyDefenseValue(4);
		return i;
	}

	public Item newRingOfStrength() {
		Item i = new Item("=", Color.GOLD, "Ring of Str", true, false, false);
		i.modifyItemSlot(6);
		i.modifyItemSlot2(7);
		i.modifyAttackValue(4);
		return i;
	}

	// POTIONS Ꭳ !
	public Item newLesserHealingPotion() {
		Item i = new Item("Ꭳ", Color.SALMON, "Lesser HP-pot", false, false, true);
		i.modifyHealingValue(10);
		return i;
	}

	public Item newStrengthPotion() {
		Item i = new Item("!", Color.YELLOW, "Strength pot", false, false, true);
		i.addBuff(addBuffStrength(30, 1));
		return i;
	}

	public Item newResistancePotion() {
		Item i = new Item("!", Color.MEDIUMSPRINGGREEN, "Resistance pot", false, false, true);
		i.addBuff(addBuffResistance(30, 1));
		return i;
	}

	public Item newVitalityPotion() {
		Item i = new Item("!", Color.SALMON, "Vitality pot", false, false, true);
		i.addBuff(addBuffHealthUp(30, 1));
		return i;
	}

	public Item newSuperStrengthPotion() {
		Item i = new Item("!", Color.WHITE, "SStrength potion", false, false, true);
		i.addBuff(addBuffStrength(30, 2));
		return i;
	}

	// FOODS
	public Item newBaguette() {
		Item i = new Item("&", Color.LEMONCHIFFON, "Baguette", true, true, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(1);
		i.modifyHealingValue(3);
		return i;
	}

	public Item newSpiderGland() {
		Item i = new Item("&", Color.DARKSALMON, "Spider Gland", false, true, false);
		i.modifyHealingValue(8);
		return i;
	}

	public Item newMushroom() {
		Item i = new Item("⍾", Color.DARKGOLDENROD, "Mushroom", false, true, false);
		i.modifyHealingValue(2);
		return i;
	}

	public Item newCookie() {
		Item i = new Item("%", Color.PINK, "Cookie", true, true, false);
		i.modifyItemSlot(3);
		i.modifyDefenseValue(1);
		i.modifyHealingValue(1);
		return i;
	}

	public Item newCandyCane() {
		Item i = new Item("ʈ", Color.PINK, "Candy cane", true, true, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(3);
		i.modifyHealingValue(5);
		return i;
	}

	public Item newLiquoriceStick() {
		Item i = new Item("\\", Color.PINK, "Liquorice stick", true, true, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(2);
		i.modifyHealingValue(4);
		return i;
	}

	public Item newCandyCone() {
		Item i = new Item("∆", Color.PINK, "Candy cone", true, true, false);
		i.modifyItemSlot(3);
		i.modifyDefenseValue(1);
		i.modifyHealingValue(1);
		return i;
	}

	public Item newCottonCandy() {
		Item i = new Item("¤", Color.PINK, "Cotton candy", true, true, false);
		i.modifyItemSlot(0);
		i.modifyDefenseValue(1);
		i.modifyHealingValue(1);
		return i;
	}

	// RANDOMSWITCHES
	public void newRandomWeakMob() {
		int rnd = new Random().nextInt(4);
		switch (rnd) {
			case 0:
				newSpider(15 + 5 * p.floor, 8 + 3 * p.floor, 2 + 2 * p.floor);
				break;
			case 1:
				newSlime(14 + 5 * p.floor, 6 + 2 * p.floor, 2 + 2 * p.floor);
				break;
			case 2:
				newGiantRat(10 + 5 * p.floor, 10 + 3 * p.floor, 2 + 2 * p.floor);
				break;
			case 3:
				newRat(10 + 5 * p.floor, 8 + 3 * p.floor, 2 + 2 * p.floor);
				break;
			default:
				newBat(10 + 5 * p.floor, 6 + 2 * p.floor, 2 + 2 * p.floor);
				break;
		}
	}

	public void newRandomRings() {
		int rnd = new Random().nextInt(3);
		Item i;
		switch (rnd) {
			case 1:
				i = newRingOfProtection();
				break;
			case 2:
				i = newRingOfStrength();
				break;
			default:
				newRandomMisc();
				return;
		}
		openSpotItem(i);
		itemArr.add(i);
	}

	public void newRandomMisc() {
		int rnd = new Random().nextInt(8);
		Item i;
		switch (rnd) {
			default:
				i = newBrick();
				break;
		}
		openSpotItem(i);
		itemArr.add(i);
	}

	public void newRandomFood() {
		int rnd = new Random().nextInt(8);
		Item i;
		switch (rnd) {
			case 0:
				i = newCookie();
				break;
			case 1:
				i = newCandyCane();
				break;
			case 2:
				i = newLiquoriceStick();
				break;
			case 3:
				i = newCandyCone();
				break;
			case 4:
				i = newCottonCandy();
				break;
			default:
				i = newBaguette();
				break;
		}
		openSpotItem(i);
		itemArr.add(i);
	}

	public void newRandomArmor() {
		int rnd = new Random().nextInt(6);
		Item i;
		switch (rnd) {
			case 0:
				i = newIronChestplate();
				break;
			case 1:
				i = newIronLeggings();
				break;
			case 2:
				i = newMagicHat();
				break;
			case 3:
				i = newWoodenShield();
				break;
			case 4:
				i = newHelmet();
				break;
			default:
				i = newLeatherArmor();
				break;
		}
		openSpotItem(i);
		itemArr.add(i);
	}

	public void newRandomSword() {
		int rnd = new Random().nextInt(4);
		Item i;
		switch (rnd) {
			case 0:
				i = newIronSword();
				break;
			case 1:
				i = newDagger();
				break;
			case 2:
				i = newSpear();
				break;
			default:
				i = newWoodenSword();
				break;
		}
		openSpotItem(i);
		itemArr.add(i);
	}

	public void newRandomPotions() {
		int rnd = new Random().nextInt(6);
		Item i;
		switch (rnd) {
			case 0:
				i = newStrengthPotion();
				break;
			case 1:
				i = newResistancePotion();
				break;
			case 2:
				i = newVitalityPotion();
				break;
			case 3:
				i = newVitalityPotion();
				break;
			case 4:
				i = newSuperStrengthPotion();
				break;
			default:
				i = newLesserHealingPotion();
				break;
		}
		openSpotItem(i);
		itemArr.add(i);
	}

}
