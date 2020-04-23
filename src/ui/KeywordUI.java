package ui;

import parsing.KeywordInterface;

import java.util.Scanner;

public class KeywordUI extends KeywordInterface {
	/**
	 * Runs {@link #useKeywords} on user input
	 * @param args default main arguments
	 */
	public static void main(String[] args) {
		Scanner scan = new Scanner(System.in);
		scan.useDelimiter("\\n");
		boolean flag = true;
		while (flag) {
			String input = scan.next();
			if ('!' == input.charAt(0) || (input.length() >= 4 && "exit".equals(input.substring(0, 4))))
				flag = false;
			else {
				try {
					System.out.println(useKeywords(input));
					flag = true;
				} catch (IllegalArgumentException e) {
					System.out.println(e.getMessage());
					flag = false;
				}
			}
		}
	}
}
