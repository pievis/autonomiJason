package it.unibo.scopone.impl;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Seed;

public class Card implements ICard {

	int number;
	Seed seed;

	public Card(int number, Seed seed) {
		this.number = number;
		this.seed = seed;
	}

	@Override
	public String getCardStr() {
		String seedStr = (seed + "").toLowerCase();
		String str = "card(" + number + "," + seedStr + ")";
		return str;
	}

	@Override
	public int getNumber() {
		return number;
	}

	@Override
	public Seed getSeed() {
		return seed;
	}
}
