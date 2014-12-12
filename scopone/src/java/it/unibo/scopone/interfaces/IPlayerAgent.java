package it.unibo.scopone.interfaces;

import java.util.List;

public interface IPlayerAgent {
	/**
	 * @return nome del giocatore
	 */
	public String getName();

	/**
	 * Gli sono passate le carte con cui il giocatore inizia la partita. Ha
	 * effetto solo durante l'init
	 * 
	 * @param cards
	 */
	public void setCardsOnHand(List<ICard> cards);

	/**
	 * Gli e' comunicato l'inizio del proprio turno
	 */
	public void notifyStartTurn();

	/**
	 * Processo deliberativo che porta l'agente a prendere una decisione in base
	 * al contesto
	 */
	//private void deliberate();

	/**
	 * Gioca la carta scelta
	 * 
	 * @param card = carta da giocare
	 * @param taking = presa che si intende effettuare, lista vuota se non si
	 *            prende nulla dal tavolo.
	 */
	//private void playCard(ICard card, List<ICard> taking);

	/**
	 * Comunica al successivo giocatore la fine del proprio turno.
	 */
	//private void endTurn();
}
