package test;

public class Card {
	public static final String[] colorName = {"Ã·»¨", "·½¿é", "ºìÌÒ", "ºÚÌÒ"};
	private int color;// 1-4 clubs diamonds hearts spades
	private int num;// 1-13
	public Card(int color, int num) {
		this.color = color;
		this.num = num;
	}
	public int getColor() {
		return color;
	}
	public String getColorName() {
		return colorName[color - 1];
	}
	public int getNum() {
		return num;
	}
	public String toString() {
		return colorName[color-1] + this.num;
	}
}
