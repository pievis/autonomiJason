package it.unibo.scopone.gui;

import it.unibo.scopone.interfaces.ICard;

import java.util.List;

import javax.swing.JFrame;

public class SelectCardFrame extends JFrame {
	List<ICard> cards;
	
	public SelectCardFrame(List<ICard> cards){
		this.cards = cards;
		setTitle("Select one card");
		setSize(300,200);
		for(cards )
	}
}
