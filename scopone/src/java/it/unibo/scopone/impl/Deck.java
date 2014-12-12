package it.unibo.scopone.impl;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.IDeck;
import it.unibo.scopone.structs.Seed;

public class Deck implements IDeck {

	static final int NUMCARDMAX = 40;
	static final int numSeed = 4;
	protected static Card card;
	static Random random = new Random();
	static ArrayList<Card> initialDeck;
	static ArrayList<Card> finalDeck;

	public Deck() {
		initialDeck = new ArrayList<Card>();
		finalDeck = new ArrayList<Card>();
		for (Seed s : Seed.values()){
			generationDeck(s);
		}
		shuffle();
	}

	@Override
	public int cardsLeft() {
		return finalDeck.size();
	}

	@Override
	public ICard dealCard() {
		if (finalDeck.size() == 0)
			throw new IllegalStateException("No more card in the deck");
		ICard card = finalDeck.remove(0);
		return card;
	}

	@Override
	public void shuffle() {
		Random rnd = new Random();
		for (int i = 0; i < NUMCARDMAX; i++) {
			int r = (int) (rnd.nextInt(i + 1));
			finalDeck.add(r, initialDeck.get(i));
		}
//		for (int i = 0; i < NUMCARDMAX; i++) {
//			System.out.println(finalDeck.get(i).getCardStr());
//		}
	}

	private static void generationDeck(Seed inputSeed) {
		for (int i = 1; i <= NUMCARDMAX / numSeed; i++) {
			card = new Card(i, inputSeed);
			initialDeck.add(card);
		}
	}
}
