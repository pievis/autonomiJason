// Agent basic_player in project scopone

/* Initial beliefs and rules */
cardsOnHand([]). //lista di carte che ha in mano, inizialmente vuota

/* Initial goals */

//Non fare niente, vieni comunicato

/* Plans */

+!turnStart : true <- 
					?cardsOnHand(X); 
					deliberate(X).

