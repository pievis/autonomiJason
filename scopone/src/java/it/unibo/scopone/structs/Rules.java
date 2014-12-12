package it.unibo.scopone.structs;
import java.util.ArrayList;
import java.util.List;

import utils.Lists;
import it.unibo.scopone.interfaces.*;

public class Rules {
	//---
	//STRUCT:
	//	presa = contiene la carta lasciata sul tavolo
	//---
	
	/**
	 * se butto una carta e lo stesso valore e' sul tavolo, la devo prendere.
	 * altrimenti raccolgo con la somma
	 * @param card = carta giocata
	 * @param tableCards = carte sul tavolo
	 */
	public static boolean azionePossibile(ICard card, List<ICard> tableCards, List<ICard> presa)
	{
		//Butto una carta e non faccio prese
		if(presa.size() == 0)
		{
			boolean esistePresa = existPresa(card, tableCards);
			if(esistePresa){
				log("Non puoi usare la carta " + card.getCardStr() + ", una presa e' possibile.");
				return false;
			}
			return true;
		}
		else{
			//Butto una carta e faccio una presa
			boolean presaPossibile = presaPossibile(card, tableCards, presa);
			if(!presaPossibile){
				log("Non puoi usare la carta " + card.getCardStr() + " con la presa specificata");
				String takingStr = "Taking: [";
				if(presa.size() != 0)
					for(ICard c : presa)
						takingStr += c.getCardStr() + " ";
				log(takingStr + "]");
				return false;
			}
			else
				return true;
		}
	}
	
	
	/**
	 * esiste una presa
	 * @param card
	 * @param tableCards
	 * @return true se la mossa � possibile
	 */
	public static boolean existPresa(ICard card, List<ICard> tableCards){
		for(ICard c : tableCards)
			if(card.getNumber() == c.getNumber())
				return true;
		
		if(getPrese(card, tableCards).size() == 0)
			return false;
		else
			return true;
	}
	
	/**
	 * @param card
	 * @param tableCards
	 * @param presa
	 * @return true se � possibile fare la presa richiesta
	 */
	public static boolean presaPossibile(ICard card, List<ICard> tableCards, List<ICard> presa)
	{
		List<List<ICard>> possibiliPrese = getPrese(card, tableCards);
		for(List<ICard> subp : possibiliPrese){
			boolean equalsPresa = true;
			if(subp.size() != presa.size())
				equalsPresa = false;
			if(equalsPresa)
				for(ICard c : subp){
					if(!presa.contains(c))
						equalsPresa = false;
				}
			if(equalsPresa)
				return true; //la presa � tra quelle possibili
		}
		return false;
	}
	
	/**
	 * Ritorna le prese possibili relative alle carte passata
	 * @param card
	 * @param tableCards
	 * @return
	 */
	public static List<List<ICard>> getPrese(ICard card, List<ICard> tableCards){
		List<List<ICard>> retPrese = new ArrayList<List<ICard>>();
		
		//singola presa
		for(ICard c : tableCards)
			if(card.getNumber() == c.getNumber())
			{
				ArrayList<ICard> spresa = new ArrayList<ICard>();
				spresa.add(c);
				spresa.add(card); //aggiungo carta giocata alla presa
				retPrese.add(spresa);
				return retPrese; //singola presa
			}
		//prese multiple
		List<List<ICard>> allSubSets = Lists.powerset(tableCards);
		for(List<ICard> set : allSubSets)
		{
			int sum = 0;
			for(ICard tablec : set)
				sum+=tablec.getNumber();
			if(sum == card.getNumber()){
				set.add(card); //aggiungo la carta giocata alla presa
				retPrese.add(set); //aggiungo il set alle prese
				}
		}
		return retPrese;
	}
	
	/**
	 * @param card
	 * @return true se card e' il settebello
	 */
	public static boolean isSetteBello(ICard card){
		if(card.getNumber() == 7 && card.getSeed() == Seed.DENARI)
			return true;
		return false;
	}
	
	private static void log(String text)
	{
		System.out.println("Rules] " + text);
	}
}
