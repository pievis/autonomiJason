package it.unibo.scopone.structs;

import java.util.List;

import utils.PrologUtils;

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
	private String playerStr;

	public Action(IPlayerAgent player, ICard playedCard, List<ICard> taking) {
		this.player = player;
		playerStr = player.getName();
		this.playedCard = playedCard;
		this.taking = taking;
	}
	
	public Action(String player, ICard playedCard, List<ICard> taking) {
		playerStr = player;
		this.playedCard = playedCard;
		this.taking = taking;
	}

	public String getRep() {
		String rep = "action(" + playerStr + ", "
				+ playedCard.getCardStr() + ", "+PrologUtils.cardListToStrRapp(taking)+")";
		return rep;
	}
	
	

}
