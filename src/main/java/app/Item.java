package app;

import java.util.List;
import java.util.ArrayList;

import javafx.scene.paint.Color;

public class Item {
	public boolean pickedUp = false;
	public int x = 0, y = 0;

	public void setPos(int xx, int yy) {
		this.x = xx;
		this.y = yy;
	}

	private List<Buff> buffs = new ArrayList<>();

	public List<Buff> buffs() {
		return buffs;
	}

	public void addBuff(Buff b) {
		this.buffs.add(b);
	}

	public Boolean hasBuff() {
		return this.buffs.size() > 0;
	}

	private String glyph;

	public String glyph() {
		return glyph;
	}

	private Color color;

	public Color color() {
		return color;
	}

	private String name;

	public String name() {
		return name;
	}

	private Boolean canEquip;

	public Boolean canEquip() {
		return canEquip;
	}

	private Boolean canEat;

	public Boolean canEat() {
		return canEat;
	}

	private Boolean canDrink;

	public Boolean canDrink() {
		return canDrink;
	}

	private int itemSlot;

	public int itemSlot() {
		return itemSlot;
	}

	public void modifyItemSlot(int amount) {
		itemSlot = amount;
	}

	private int itemSlot2 = -1;

	public int itemSlot2() {
		return itemSlot2;
	}

	public void modifyItemSlot2(int amount) {
		itemSlot2 = amount;
	}

	private boolean dualHanded = false;

	public boolean dualHanded() {
		return dualHanded;
	}

	public void modifyDualHanded(boolean bool) {
		this.dualHanded = bool;
	}

	private int attackValue;

	public int attackValue() {
		return attackValue;
	}

	public void modifyAttackValue(int amount) {
		this.attackValue = amount;
	}

	private int defenseValue;

	public int defenseValue() {
		return defenseValue;
	}

	public void modifyDefenseValue(int amount) {
		this.defenseValue = amount;
	}

	private int vitalityValue;

	public int vitalityValue() {
		return vitalityValue;
	}

	public void modifyVitalityValue(int amount) {
		this.vitalityValue = amount;
	}

	private int luckValue;

	public int luckValue() {
		return luckValue;
	}

	public void modifyLuckValue(int amount) {
		this.luckValue = amount;
	}

	private int healingValue;

	public int healingValue() {
		return healingValue;
	}

	public void modifyHealingValue(int amount) {
		this.healingValue = amount;
	}

	public Item(String glyph, Color color, String name, boolean canEquip, boolean canEat, boolean canDrink) {
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.canEquip = canEquip;
		this.canEat = canEat;
		this.canDrink = canDrink;
	}

	public Item(String glyph, Color color, String name, int x, int y, boolean canEquip, boolean canEat,
			boolean canDrink) {
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.canEquip = canEquip;
		this.canEat = canEat;
		this.canDrink = canDrink;
		this.x = x;
		this.y = y;
	}

	public Item(Item i) {
		this.glyph = i.glyph;
		this.color = i.color;
		this.name = i.name;
		this.canEquip = i.canEquip;
		this.canEat = i.canEat;
		this.canDrink = i.canDrink;
		this.x = i.x;
		this.y = i.y;
		this.itemSlot = i.itemSlot;
		this.itemSlot2 = i.itemSlot2;
		this.dualHanded = i.dualHanded;
		this.attackValue = i.attackValue;
		this.defenseValue = i.defenseValue;
		this.vitalityValue = i.vitalityValue;
		this.luckValue = i.luckValue;
		this.healingValue = i.healingValue;
		this.buffs = i.buffs;
	}

	public Item(String[] i) {
		this.glyph = i[0];
		this.color = Color.web(i[1]);
		this.name = i[2];
		this.canEquip = Boolean.valueOf(i[3]);
		this.canEat = Boolean.valueOf(i[4]);
		this.canDrink = Boolean.valueOf(i[5]);
		this.x = Integer.parseInt(i[6]);
		this.y = Integer.parseInt(i[7]);
		this.itemSlot = Integer.parseInt(i[8]);
		this.itemSlot2 = Integer.parseInt(i[9]);
		this.dualHanded = Boolean.valueOf(i[10]);
		this.attackValue = Integer.parseInt(i[11]);
		this.defenseValue = Integer.parseInt(i[12]);
		this.vitalityValue = Integer.parseInt(i[13]);
		this.luckValue = Integer.parseInt(i[14]);
		this.healingValue = Integer.parseInt(i[15]);
	}

	@Override
	public String toString() {
		return (this.glyph + "," + this.color + "," + this.name + "," + this.canEquip + "," + this.canEat + ","
				+ this.canDrink + "," + this.x + "," + this.y + "," + this.itemSlot + "," + this.itemSlot2 + ","
				+ this.dualHanded + "," + this.attackValue + "," + this.defenseValue + "," + this.vitalityValue + ","
				+ this.luckValue + "," + this.healingValue + "," + this.buffs);
	}

	public boolean hasModifier(String mod) {
		switch (mod) {
			case "canEquip":
				return this.canEquip;
			case "canEat":
				return this.canEat;
			case "canDrink":
				return this.canDrink;
			default:
				return true;
		}
	}

	public String details() {
		String details = "";
		if (attackValue != 0)
			details += " Attack value of: " + this.attackValue;
		if (defenseValue != 0)
			details += " Defense value of: " + this.defenseValue;
		if (vitalityValue != 0)
			details += " Vitality value of: " + this.vitalityValue;
		if (luckValue != 0)
			details += " Luck value of: " + this.luckValue;
		if (healingValue != 0)
			details += " Heals for: " + this.healingValue;
		return details;
	}
}
