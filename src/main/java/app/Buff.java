package app;

import javafx.scene.paint.Color;

public class Buff {
	private String glyph;
	private String name;
	private Color color;
	private int steps = 0;
	private int maxSteps = 0;

	public String glyph() {
		return glyph;
	}

	public Color color() {
		return color;
	}

	public String name() {
		return name;
	}

	public int maxSteps() {
		return maxSteps;
	}

	public void modifyMaxSteps(int amount) {
		this.maxSteps = amount;
	}

	public int steps() {
		return steps;
	}

	public void addSteps(int amount) {
		this.steps += amount;
	}

	public Buff(String glyph, Color color, String name, int maxSteps) {
		this.glyph = glyph;
		this.color = color;
		this.name = name;
		this.maxSteps = maxSteps;
	}

	@Override
	public String toString() {
		return (glyph + "," + color + "," + name + "," + maxSteps + "," + attackValue + "," + defenseValue + ","
				+ vitalityValue + "," + luckValue + "," + viewRangeValue);
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

	private int viewRangeValue;

	public int viewRangeValue() {
		return viewRangeValue;
	}

	public void modifyViewRangeValue(int amount) {
		this.viewRangeValue = amount;
	}
}
