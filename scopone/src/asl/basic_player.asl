// Agent basic_player in project scopone

/* Initial beliefs and rules */
//Perceptions
//cardsOnHand([]). //lista di carte che ha in mano, inizialmente vuota
//cardsOnTable([]). //Non ha ancora percezione delle carte in gioco
//nextPlayer(Name). //conosce il gioco del prossimo giocatore
//~gameEnded. //esiste se il gioco è terminato
canStart :- .my_name(MyName) & (p1 == MyName).

/* Initial goals */

//Se sei il giocatore p1 allora devi iniziare la partita
!startGame.

/* Plans */

+!startGame : canStart <- !doMove.
+!startGame : not canStart <- .print("ready").


+!doMove : not gameEnded <- ?cardsOnHand(X); 
							!endTurn.
					
+!endTurn : true <- .print("my turn ended");
					?nextPlayer(NextP);
					.send(NextP,achieve,doMove). //tells the next agent to do his move
/* Plans failure handling*/

+!doMove
	: true
	<- .current_intention(I); // notice INTERNAL action to retrieve the execution "context"
		.print("Failed to achieve goal '!doMove'. Current intention is: ", I). // print debug info		
