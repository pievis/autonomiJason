package it.unibo.scopone.structs;

import java.util.ArrayList;
import java.util.List;

import utils.Lists;
import it.unibo.scopone.interfaces.*;

public class Rules {
	// ---
	// STRUCT:
	// presa = contiene la carta lasciata sul tavolo
	// ---

	/**
	 * se butto una carta e lo stesso valore e' sul tavolo, la devo prendere.
	 * altrimenti raccolgo con la somma
	 * 
	 * @param card
	 *            = carta giocata
	 * @param tableCards
	 *            = carte sul tavolo
	 */
	public static boolean azionePossibile(ICard card, List<ICard> tableCards,
			List<ICard> presa) {
		// Butto una carta e non faccio prese
		if (presa.size() == 0) {
			boolean esistePresa = existPresa(card, tableCards);
			if (esistePresa) {
				log("Non puoi usare la carta " + card.getCardStr()
						+ ", una presa e' possibile.");
				return false;
			}
			return true;
		} else {
			// Butto una carta e faccio una presa
			boolean presaPossibile = presaPossibile(card, tableCards, presa);
			if (!presaPossibile) {
				log("Non puoi usare la carta " + card.getCardStr()
						+ " con la presa specificata");
				String takingStr = "Taking: [";
				if (presa.size() != 0)
					for (ICard c : presa)
						takingStr += c.getCardStr() + " ";
				log(takingStr + "]");
				return false;
			} else
				return true;
		}
	}

	/**
	 * esiste una presa
	 * 
	 * @param card
	 * @param tableCards
	 * @return true se la mossa è possibile
	 */
	public static boolean existPresa(ICard card, List<ICard> tableCards) {
		for (ICard c : tableCards)
			if (card.getNumber() == c.getNumber())
				return true;

		if (getPrese(card, tableCards).size() == 0)
			return false;
		else
			return true;
	}

	/**
	 * @param card
	 * @param tableCards
	 * @param presa
	 * @return true se è possibile fare la presa richiesta
	 */
	public static boolean presaPossibile(ICard card, List<ICard> tableCards,
			List<ICard> presa) {
		List<List<ICard>> possibiliPrese = getPrese(card, tableCards);
		for (List<ICard> subp : possibiliPrese) {
			boolean equalsPresa = true;
			if (subp.size() != presa.size())
				equalsPresa = false;
			if (equalsPresa)
				for (ICard c : subp) {
					//if (!presa.contains(c))
					if(!listCardContains(presa,c))
						equalsPresa = false;
				}
			if (equalsPresa)
				return true; // la presa è tra quelle possibili
		}
		return false;
	}
	
	private static boolean listCardContains(List<ICard> list, ICard card){
		for(ICard c : list){
			if(c.getCardStr().equals(card.getCardStr()))
				return true;
		}
		return false;
	}

	/**
	 * Ritorna le prese possibili relative alle carte passata
	 * 
	 * @param card
	 * @param tableCards
	 * @return
	 */
	public static List<List<ICard>> getPrese(ICard card, List<ICard> tableCards) {
		List<List<ICard>> retPrese = new ArrayList<List<ICard>>();

		// singola presa
		for (ICard c : tableCards)
			if (card.getNumber() == c.getNumber()) {
				ArrayList<ICard> spresa = new ArrayList<ICard>();
				spresa.add(c);
				spresa.add(card); // aggiungo carta giocata alla presa
				retPrese.add(spresa);
				return retPrese; // singola presa
			}
		// prese multiple
		List<List<ICard>> allSubSets = Lists.powerset(tableCards);
		for (List<ICard> set : allSubSets) {
			int sum = 0;
			for (ICard tablec : set)
				sum += tablec.getNumber();
			if (sum == card.getNumber()) {
				set.add(card); // aggiungo la carta giocata alla presa
				retPrese.add(set); // aggiungo il set alle prese
			}
		}
		return retPrese;
	}

	public static int finalScore(List<ICard> personalDeck, int numScope) {
		int score = 0;
		int numDenari = 0;
		int numSette = 0;
		for (ICard card : personalDeck) {
			if (card.getNumber() == 7 && card.getSeed() == Seed.DENARI) {
				// punto settebello
				score += 1;
			}
			if (card.getSeed() == Seed.DENARI) {
				numDenari += 1;
			}
			if (card.getNumber() == 7) {
				numSette += 1;
			}
		}
		if (personalDeck.size() >= 21) {
			// punto carte
			score += 1;
		}
		if (numDenari >= 5) {
			// punto denari
			score += 1;
		}
		if (numSette == 4) {
			// punto primiera
			score += 1;
		}
		return score + numScope;
	}

	public static boolean isSetteBello(ICard card) {
		if (card.getNumber() == 7 && card.getSeed() == Seed.DENARI)
			return true;
		return false;
	}

	/**
	 * @param card
	 * @return true se card e' il settebello
	 */
	public static int setteBelloScore(ICard card) {
		if (card.getNumber() == 7 && card.getSeed() == Seed.DENARI)
			return 1;
		return 0;
	}

	private static void log(String text) {
		System.out.println("Rules] " + text);
	}
}
