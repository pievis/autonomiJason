package it.unibo.scopone.impl.agents;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import org.apache.commons.math3.distribution.EnumeratedIntegerDistribution;

import utils.BasicMaths;
import utils.logging.ILogger;
import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.IPlayerAgent;
import it.unibo.scopone.structs.Action;
import it.unibo.scopone.structs.Rules;
import it.unibo.scopone.structs.Seed;

public class AgentPlayer extends BasicPlayer implements ILogger {

	/*
	 * String name; IPlayerAgent nextAgent; List<ICard> cardsOnHand; ITable
	 * table; // Riferimento al tavolo di gioco List<ICard> deck; // mazzetto
	 */

	LogLevel verbosity = LogLevel.GENERIC;

	public AgentPlayer(String name) {
		super(name);
	}

	public AgentPlayer(String name, IPlayerAgent nextAgent) {
		super(name, nextAgent);
	}

	/**
	 * Sceglie una carta in maniera non deterministica a seguito di ragionamenti
	 * fatti in base a degli obbiettivi che gli permettono di guadagnare punti.
	 * Ragionamenti: Scopa Sette bello Denari Carte Primiera Una carta con un
	 * maggior coefficiente di fiducia ha maggiore possibilità di essere
	 * giocata.
	 **/
	@Override
	void deliberate() {
		double[] trustArray = evaluateCardTrust();
		ICard card = getCartWithProbability(trustArray);
		List<List<ICard>> takings = Rules.getPrese(card, table.cardsOnTable());
		List<ICard> taking;
		if (takings.size() > 0) {
			taking = selectTaking(takings);
		} else {
			taking = new ArrayList<ICard>(); // Empty taking
		}
		intendedAction = new Action(this, card, taking);
	}
	
	List<ICard> selectTaking(List<List<ICard>> takings)
	{
		//TODO implementare scelta smart di selezione (possibilmente non deterministica)
		List<ICard> taking = takings.get(new Random().nextInt(takings.size()));
		return taking;
	}

	ICard getCartWithProbability(double[] trustArray) {
		ICard card = null;
		// Se le carte mancano di fiducia, allora ne seleziono semplicemente una
		// random
		double max = BasicMaths.arrayMax(trustArray);
		if (max <= 0.0) {
			int randomIndex = new Random().nextInt(cardsOnHand.size());
			card = cardsOnHand.get(randomIndex);
			log("Scelta la carta " + card.getCardStr() + " con fiducia: "
					+ trustArray[randomIndex], LogLevel.SPECIFIC);
			return card;
		}
		// Altrimenti la scelgo basandomi sulla fiducia
		// normalizzo il trust array con double da 0.0 a 1.0
		double[] probabilities = BasicMaths.normalizeArray(trustArray, 1.0);
		int[] singletons = new int[cardsOnHand.size()];
		for (int i = 0; i < cardsOnHand.size(); i++)
			singletons[i] = i;
		EnumeratedIntegerDistribution distribution = new EnumeratedIntegerDistribution(
				singletons, probabilities);
		int randomIndex = distribution.sample(); // prende la carta in relazione
													// alla probabilità non
													// uniforme
		card = cardsOnHand.get(randomIndex);
		log("Scelta la carta " + card.getCardStr() + " con fiducia: "
				+ trustArray[randomIndex] + " => " + probabilities[randomIndex],
				LogLevel.SPECIFIC);
		return card;
	}

	/**
	 * Valuta la fiducia nella scelta di una determinata carta.
	 * 
	 * @return
	 */
	private double[] evaluateCardTrust() {
		double[] trustArray = new double[cardsOnHand.size()];
		for (int i = 0; i < trustArray.length; i++) {
			// per ogni carta ragiono
			double[] partialTrust = new double[5];
			double trust = 0.0f;
			ICard card = cardsOnHand.get(i);
			partialTrust[0] = setteBello(card);
			partialTrust[1] = carte(card);
			partialTrust[2] = primiera(card);
			partialTrust[3] = denari(card);
			partialTrust[4] = scopa(card);
			trust = BasicMaths.arraySum(partialTrust);
			trustArray[i] = trust;
			printTrustForCard(card, partialTrust);
		}
		return trustArray;
	}

	// ////////////////////////////////////////////////////
	// Ragionamenti
	
	//Diamo una maggior fiducia alle azioni che ci permettono di 
	//compiere un punto.
	//1.0 = fiducia per un punto
	//0.1 = fiducia nel prendere una singola carta
	//0.3 = fiducia nel prendere una carta che avvicina a primiera/denari

	/**
	 * Ragiona su quanto questa carta sia utile per ottenere il punto relativo
	 * al sette bello.
	 * 
	 * @param card
	 *            = carta in esame
	 * @return fiducia per la carta in esame
	 */
	private double setteBello(ICard card) {
		// La carta che esamino è il sette bello
		if (Rules.isSetteBello(card)) {
			if (Rules.existPresa(card, table.cardsOnTable()))
				;
			return 1.0;
		}
		for (ICard tcard : table.cardsOnTable()) {
			// Il settebello è tra le carte in gioco (sul tavolo)
			if (Rules.isSetteBello(tcard)) {
				List<List<ICard>> prese = Rules.getPrese(card,
						table.cardsOnTable());
				for (List<ICard> presa : prese) {
					// Se almeno una presa contiene il sette bello allora ho più
					// fiducia
					if(presa.contains(tcard))
						return 1.0;
				}
			}
		}
		return 0.0;
	}

	/**
	 * Ragiona e ritorna una fiducia relativa al compiere 
	 * un punto facendo scopa.
	 * 
	 * @param card
	 *            = carta in esame
	 * @return fiducia
	 */
	private double scopa(ICard card) {
		List<List<ICard>> prese = Rules.getPrese(card, table.cardsOnTable());
		for(List<ICard> presa : prese)
			if(presa.size() == table.cardsOnTable().size())
				return 1.0; //Faccio una scopa;
		return 0.0;
	}

	private double primiera(ICard card) {
		if (card.getNumber() == 7) {
			if (Rules.existPresa(card, table.cardsOnTable())) {
				return 0.3;
			}
		}
		for (ICard tcard : table.cardsOnTable()) {
			// c'e' un 7 tra le carte in gioco sul tavolo
			if (tcard.getNumber() == 7) {
				List<List<ICard>> prese = Rules.getPrese(card,
						table.cardsOnTable());
				for (List<ICard> presa : prese) {
					if(presa.contains(tcard))
						return 0.3;
				}
			}
		}
		return 0.0;
	}

	private double carte(ICard card) {
		List<List<ICard>> prese = Rules.getPrese(card, table.cardsOnTable());
		if(prese.size() > 0){
			double fp = 0.0;
			for(List<ICard> presa : prese)
				for(int i = 0; i < presa.size(); i++)
					fp+=0.1;
			return fp;
		}
		return 0.0;
	}

	private double denari(ICard card) {
		//Una presa contiene anche la carta in esame
		List<List<ICard>> prese = Rules.getPrese(card, table.cardsOnTable());
		if(prese.size() > 0){
			double fp = 0.0;
			for(List<ICard> presa : prese)
				for(ICard c : presa)
					if(c.getSeed() == Seed.DENARI)
						fp+=0.3;
			return fp;
		}
		return 0.0;
	}

	// ////////////////////////////////////////////////////////////////////////////////////////////////
	private void printTrustForCard(ICard card, double[] trustArray) {
		String pstr = "trustArray[";
		double sum = 0.0;
		for (int i = 0; i < trustArray.length; i++) {
			pstr += String.format("%s", trustArray[i]) + " ";
			sum += trustArray[i];
		}
		pstr += "] = " + sum;
		log("Valore di trust per carta: " + card.getCardStr() + "\n\t" + pstr,
				LogLevel.SPECIFIC);
	}

	protected void log(String text, LogLevel logLevel) {
		if (verbosity == LogLevel.ALL || logLevel == LogLevel.ERROR)
			super.log(text);
		else {
			if (logLevel == verbosity)
				super.log(text);
		}
	}
}
