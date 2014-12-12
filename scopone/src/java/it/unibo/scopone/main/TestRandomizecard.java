package it.unibo.scopone.main;

import java.util.ArrayList;
import java.util.Random;

import it.unibo.scopone.impl.*;
import it.unibo.scopone.structs.Seed;

public class TestRandomizecard {
	static final int NUMCARDMAX = 40;
	static final int numSeed = 4;
	protected static Card card;
	static Random random = new Random();
	static ArrayList<Card> initialDeck;
	static ArrayList<Card> finalDeck;

	public static void main(String[] args) {
//		int bast = 0;
//		int coppe = 0;
//		int den = 0;
//		int spad = 0;
//		int var = 2;
//		int basto = 0;
//		int number = 1000000;
//		for (int i = 0; i <= number; i++) {
////			if (i % (number/100) == 0) {
//			if (i % (number/10) == 0) {
//				System.out.println(i / (number/100) + "%");
//			}
			initialDeck = new ArrayList<Card>();
			finalDeck = new ArrayList<Card>();
			generationDeck(Seed.BASTONI);
			generationDeck(Seed.COPPE);
			generationDeck(Seed.DENARI);
			generationDeck(Seed.SPADE);
			randomCard();
//			if (finalDeck.get(var).getSeed() == Seed.BASTONI) {
//				bast++;
////				if (finalDeck.get(var).getNumber() == 1) {
////					basto++;
////				}
//			}
//			if (finalDeck.get(var).getSeed() == Seed.COPPE) {
//				coppe++;
//			}
//			if (finalDeck.get(var).getSeed() == Seed.DENARI) {
//				den++;
//			}
//			if (finalDeck.get(var).getSeed() == Seed.SPADE) {
//				spad++;
//			}
//		}
//		System.out.println(bast);
//		System.out.println(coppe);
//		System.out.println(den);
//		System.out.println(spad);
//		System.out.println((bast + coppe + den + spad));
	}

	public static void generationDeck(Seed inputSeed) {
		for (int i = 1; i <= NUMCARDMAX/numSeed; i++) {
			card = new Card(i, inputSeed);
			initialDeck.add(card);
		}
	}

	// serve a mischiare le carte
	public static void randomCard() {
		Random rnd = new Random();
		for (int i = 0; i < NUMCARDMAX; i++) {
			int r = (int) (rnd.nextInt(i + 1));
			finalDeck.add(r, initialDeck.get(i));
		}
		 for (int i = 0; i < NUMCARDMAX; i++) {
		 System.out.println(finalDeck.get(i).getCardStr());
		 }
	}
}
