package app;

import javafx.scene.paint.Color;
import java.util.Arrays;
import java.util.List;

import app.Game;
import app.Item;
import app.Player;
import app.World;

public class GameTest extends junit.framework.TestCase {
	private Game g = null;

	@Override
	protected void setUp() {
		g = new Game();
		g.createGameAll();
		g.p.setPlayerRadius();
		g.drawGameBoard();
	}

	@Override
	protected void tearDown() {
		g = null;
	}

	public void testGameInitialize() {
		// Check items added to gameArr
		assertNotSame(0.0, g.gameArr.size());
		// Check player initialized
		Player p = g.p;
		assertEquals(true, p.alive);
		assertEquals(g.gameArr, p.gameArr);
	}

	public void testGameMap() {
		String[][] board = g.board;
		String[][] worldMap = World.finalGenMapWithFlooding(30 * 3, 16 * 4);
		assertEquals(worldMap.length, board.length);
		assertEquals(worldMap[0].length, board[0].length);
	}

	private Item testGameItemIronSword() {
		Item i = new Item("↑", Color.LIGHTCYAN, "Iron sword", true, false, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(4);
		return i;
	}

	private Item testGameItemIronChestplate() {
		Item i = new Item("[", Color.LIGHTCYAN, "Iron Chestplate", true, false, false);
		i.modifyItemSlot(2);
		i.modifyDefenseValue(6);
		return i;
	}

	private Item testGameItemWoodenShield() {
		Item i = new Item("ᗢ", Color.DARKGOLDENROD, "Wooden Shield", true, false, false);
		i.modifyItemSlot(3);
		i.modifyDefenseValue(2);
		return i;
	}

	private Item testGameItemBaguette() {
		Item i = new Item("&", Color.LEMONCHIFFON, "Baguette", true, true, false);
		i.modifyItemSlot(1);
		i.modifyAttackValue(1);
		i.modifyHealingValue(3);
		return i;
	}

	private Item testGameItemMushroom() {
		Item i = new Item("⍾", Color.DARKGOLDENROD, "Mushroom", false, true, false);
		i.modifyHealingValue(2);
		return i;
	}

	private String[] saveArray() {
		String[] s = { "Alpha", "Tester", "0", "0", "1", "", "110.0", "2.0", "6.0", "6.0", "7.0", "110.0", "6.0", "8.0", "",
				"0.0", "8.0", "0.0", "0.0", "1.0", "", "", "/,0xffffffff,None,false,false,false,0,0,0,-1,false,0,0,0,0,0,[]",
				"&,0xfffacdff,Baguette,true,true,false,0,0,1,-1,false,1,0,0,0,3,[]",
				"[,0xe0ffffff,Iron Chestplate,true,false,false,0,0,2,-1,false,0,6,0,0,0,[]",
				"ᗢ,0xb8860bff,Wooden Shield,true,false,false,0,0,3,-1,false,0,2,0,0,0,[]",
				"/,0xffffffff,None,false,false,false,0,0,0,-1,false,0,0,0,0,0,[]",
				"/,0xffffffff,None,false,false,false,0,0,0,-1,false,0,0,0,0,0,[]",
				"/,0xffffffff,None,false,false,false,0,0,0,-1,false,0,0,0,0,0,[]",
				"/,0xffffffff,None,false,false,false,0,0,0,-1,false,0,0,0,0,0,[]" };
		return s;
	}

	public void testGameNewPlayer() {
		Player p = g.p;
		assertEquals(true, p.alive);
		assertEquals(g.gameArr, p.gameArr);
		// STATS
		assertEquals(100.0, p.stats.get("hp"));
		assertEquals(100.0, p.stats.get("hpMax"));
		assertEquals(1.0, p.stats.get("lvl"));
		assertEquals(1.0, p.stats.get("exp"));
		assertEquals(1.0, p.stats.get("atk"));
		assertEquals(1.0, p.stats.get("def"));
		assertEquals(1.0, p.stats.get("lck"));
		assertEquals(6.0, p.stats.get("viewRange"));
		// BONUS STATS
		assertEquals(0.0, p.bstats.get("hpMax"));
		assertEquals(0.0, p.bstats.get("atk"));
		assertEquals(0.0, p.bstats.get("def"));
		assertEquals(0.0, p.bstats.get("lck"));
		assertEquals(0.0, p.bstats.get("viewRange"));
		// pickup + inventory test
		p.inventory.add(testGameItemIronSword());
		p.inventory.add(testGameItemIronChestplate());
		p.inventory.add(testGameItemWoodenShield());
		p.inventory.add(testGameItemBaguette());
		p.inventory.add(testGameItemMushroom());
		assertEquals(5, p.inventory.size());
		// eat
		p.stats.put("hp", 50.0);
		assertEquals(50.0, p.stats.get("hp"));
		p.eat(p.inventory.get(4));
		assertEquals(52.0, p.stats.get("hp"));
		// equip iron sword
		p.equip(p.inventory.get(0));
		assertEquals(4.0, p.bstats.get("atk"));
		assertEquals(5, p.getTotalStat("atk"));
		// equip iron chestplate
		p.equip(p.inventory.get(0));
		assertEquals(6.0, p.bstats.get("def"));
		assertEquals(7, p.getTotalStat("def"));
		// equip wooden shield
		p.equip(p.inventory.get(0));
		assertEquals(8.0, p.bstats.get("def"));
		assertEquals(9, p.getTotalStat("def"));
		// equip baguette
		p.equip(p.inventory.get(0));
		assertEquals(1.0, p.bstats.get("atk"));
		assertEquals(2, p.getTotalStat("atk"));
		// drop
		p.drop(p.inventory.get(0));
		assertEquals(0, p.inventory.size());
		// pickup
		p.pickup();
		assertEquals(1, p.inventory.size());
		p.drop(p.inventory.get(0));
		assertEquals(0, p.inventory.size());
		// receive exp + lvl up
		assertEquals(1.0, p.stats.get("exp"));
		p.receiveExp(10);
		assertEquals(11.0, p.stats.get("exp"));
		p.receiveExp(10);
		assertEquals(8.0, p.stats.get("exp"));
		assertEquals(2.0, p.stats.get("lvl"));
		assertEquals(100.0, p.stats.get("hp"));
		// stat up
		p.modifyHP(10);
		p.modifyATK(5);
		p.modifyDEF(5);
		p.modifyLCK(5);
		p.modifyViewRange(1);
		assertEquals(110.0, p.stats.get("hp"));
		assertEquals(110.0, p.stats.get("hpMax"));
		assertEquals(6.0, p.stats.get("atk"));
		assertEquals(6.0, p.stats.get("def"));
		assertEquals(6.0, p.stats.get("lck"));
		assertEquals(7.0, p.stats.get("viewRange"));
		// test save characters
		p.killedEnemy();
		List<String> ls = p.getPlayerSaveInfo();
		String[] saveArr = new String[ls.size()];
		ls.toArray(saveArr);
		String[] saveArray = saveArray();
		assertEquals(Arrays.toString(saveArr), Arrays.toString(saveArray));
	}

	public void testGameLoadPlayer() {
		String[] saveArray = saveArray();
		List<String> saveList = Arrays.asList(saveArray);
		g.p.loadSaveFile(saveList);
		Player p = g.p;
		assertEquals(110.0, p.stats.get("hp"));
		assertEquals(110.0, p.stats.get("hpMax"));
		assertEquals(8.0, p.stats.get("exp"));
		assertEquals(2.0, p.stats.get("lvl"));
		assertEquals(6.0, p.stats.get("atk"));
		assertEquals(6.0, p.stats.get("def"));
		assertEquals(6.0, p.stats.get("lck"));
		assertEquals(7.0, p.stats.get("viewRange"));
		assertEquals("Baguette", p.getStringEquipment(1));
		assertEquals("Iron Chestplate", p.getStringEquipment(2));
		assertEquals("Wooden Shield", p.getStringEquipment(3));
	}

	public void testGameSaveImplListedLeaderboard() {
		List<String> lb = g.save.listedLeaderboard();
		String lbString = lb.get(0);
		assertEquals("DATE,NAME,REASON,FLOOR,LVL,CANDIES", lbString);
	}

	public void testGameSaveImplWriteLeaderboard() {
		List<String> lb1 = g.save.listedLeaderboard();
		String[] lb1Arr = new String[lb1.size()];
		lb1.toArray(lb1Arr);
		g.save.writeLeaderboard(lb1);
		List<String> lb2 = g.save.listedLeaderboard();
		String[] lb2Arr = new String[lb2.size()];
		lb2.toArray(lb2Arr);
		assertEquals(Arrays.toString(lb1Arr), Arrays.toString(lb2Arr));
	}

	public void testGameSaveImplSaveNLoadChar() {
		// save to file
		String[] saveArray = saveArray();
		List<String> ls = Arrays.asList(saveArray);
		g.save.saveChar(ls);
		// load from file
		List<String> lc = g.save.loadChar();
		assertNotNull(lc);
	}

	public void testGameSaveImplSaveNLoadCharNull() {
		// save to file
		String[] saveArray = { "This", "is", "not", "a", "correct", "savefile!" };
		List<String> ls = Arrays.asList(saveArray);
		g.save.saveChar(ls);
		// load from file
		List<String> lc = g.save.loadChar();
		assertNull(lc);
	}
}
