package it.unibo.scopone.structs;

import java.util.List;

import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.interfaces.IPlayerAgent;

/**
 * Rappresenta l'azione che un giocatore compie in un turno puo' essere
 * utilizzata per implementare uno storico della mosse.
 * 
 * @author Pierluigi
 */
public class Action {

	public ICard playedCard;
	public List<ICard> taking;
	public IPlayerAgent player;

	public Action(IPlayerAgent player, ICard playedCard, List<ICard> taking) {
		this.player = player;
		this.playedCard = playedCard;
		this.taking = taking;
	}

	public String getRep() {
		String rep = "action(" + player.getName() + ", "
				+ playedCard.getCardStr() + ", (...)";
		return rep;
	}

}
