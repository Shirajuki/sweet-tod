package app;

import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import java.util.Scanner;
import java.util.ArrayList;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;

public class SaveImpl implements Save {
	private boolean isDouble(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (s.charAt(i) == '.')
				return true;
		}
		return false;
	}

	private boolean isNumeric(String s) {
		try {
			int i = Integer.parseInt(s);
		} catch (NumberFormatException nfe) {
			return false;
		}
		return true;
	}

	public boolean stringItem(String s) {
		// "↑,0xe0ffffff,Iron sword,true,false,false,15,28,1,-1,false,4,0,0,0,0,[]"
		List<String> bools = new ArrayList<>();
		bools.add("false");
		bools.add("true");
		String[] i = s.split(",");
		if (i.length == 17) {
			return (i[0] instanceof String && i[1].length() == 10 && i[2] instanceof String && bools.contains(i[3])
					&& bools.contains(i[4])
					&& bools.contains(i[5]) && isNumeric(i[6]) && isNumeric(i[7]) && isNumeric(i[8]) && isNumeric(i[9])
					&& bools.contains(i[10]) && isNumeric(i[11]) && isNumeric(i[12]) && isNumeric(i[13]) && isNumeric(i[14])
					&& isNumeric(i[15]));
		}
		return false;
	}

	@Override
	public List<String> loadChar() {
		List<String> ls = new ArrayList<>();
		int spaces = 0;
		try {
			Scanner in = new Scanner(new FileReader("player.txt"));
			while (in.hasNext()) {
				String line = in.nextLine();
				if (line.equals(""))
					spaces++;
				ls.add(line);
			}
			in.close();
		} catch (FileNotFoundException e1) {
			System.out.println("[Error]: player savefile could not be found!");
		}
		// Check valid loadfile
		if (spaces == 4 && ls.size() >= 24) {
			try {
				assertTrue(ls.get(0) instanceof String);
				assertTrue(ls.get(1) instanceof String);
				assertTrue(isNumeric(ls.get(2)));
				assertTrue(isNumeric(ls.get(3)));
				assertTrue(isNumeric(ls.get(4)));
				assertTrue(ls.get(5).equals(""));
				//
				assertTrue(isDouble(ls.get(6)));
				assertTrue(isDouble(ls.get(7)));
				assertTrue(isDouble(ls.get(8)));
				assertTrue(isDouble(ls.get(9)));
				assertTrue(isDouble(ls.get(10)));
				assertTrue(isDouble(ls.get(11)));
				assertTrue(isDouble(ls.get(12)));
				assertTrue(isDouble(ls.get(13)));
				assertTrue(ls.get(14).equals(""));
				//
				assertTrue(isDouble(ls.get(15)));
				assertTrue(isDouble(ls.get(16)));
				assertTrue(isDouble(ls.get(17)));
				assertTrue(isDouble(ls.get(18)));
				assertTrue(isDouble(ls.get(19)));
				assertTrue(ls.get(20).equals(""));
				for (int i = 21; i < ls.size(); i++) {
					if (ls.get(i).equals(""))
						continue;
					assertTrue(stringItem(ls.get(i)));
				}
				System.out.println("[Load saved game]: player savefile found and loaded!");
				return ls;
			} catch (Exception e) {
				System.out.println("[Error]: player savefile found, but could not be read!");
			}
		} else {
			System.out.println("[Error]: player savefile found, but could not be read!");
		}
		return null;
	}

	@Override
	public void saveChar(List<String> ls) {
		try {
			PrintWriter outFile = new PrintWriter("player.txt");
			for (String string : ls) {
				outFile.println(string);
			}
			outFile.close();
		} catch (FileNotFoundException e2) {
			System.out.println("[Error]: player savefile could not be created.");
		}
	}

	@Override
	public List<String> listedLeaderboard() {
		List<String> ls = new ArrayList<>();
		try {
			Scanner in = new Scanner(new FileReader("leaderboard.txt"));
			while (in.hasNext()) {
				String line = in.nextLine();
				ls.add(line);
			}
			in.close();
		} catch (FileNotFoundException e1) {
			System.out.println("[Error]: leaderboard could not be opened. Creating.");
			try {
				PrintWriter outFile = new PrintWriter("leaderboard.txt");
				outFile.println("DATE,NAME,REASON,FLOOR,LVL,CANDIES");
				outFile.close();
			} catch (FileNotFoundException e2) {
				System.out.println("[Error]: file 'leaderboard.txt' could not be created.");
			}
		}
		return ls;
	}

	@Override
	public void writeLeaderboard(List<String> ls) {
		try {
			PrintWriter outFile = new PrintWriter("leaderboard.txt");
			for (String string : ls) {
				outFile.println(string);
			}
			outFile.close();
		} catch (FileNotFoundException e2) {
			System.out.println("[Error]: file 'leaderboard.txt' could not be created.");
		}
	}

}
