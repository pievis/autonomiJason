// Agent basic_player in project scopone

/* Initial beliefs and rules */
//Perceptions
//cardsOnHand([]). //lista di carte che ha in mano, inizialmente vuota
//cardsOnTable([]). //Non ha ancora percezione delle carte in gioco
//nextPlayer(Name). //conosce il gioco del prossimo giocatore
//~gameEnded. //esiste se il gioco è terminato
canStart :- .my_name(MyName) & (p1 == MyName).
hasCards :- cardsOnHand(Xl) & .length(Xl, X) & X \== 0.

/* Initial goals */

//Se sei il giocatore p1 allora devi iniziare la partita
!startGame.

/* Plans */

+!startGame : canStart <- !doMove; ?cardsOnHand(Xh); !printList(Xh).
+!printList([]) : true <- .print("fine").
+!printList([X|Xs]) : true <- .print("stampa",X); !printList(Xs).
+!startGame : not canStart <- .print("ready").

+!doMove : not gameEnded <- !execute.
					
+!endTurn : true <- .print("my turn ended");
					?nextPlayer(NextP);
					.send(NextP,achieve,doMove). //tells the next agent to do his move

+!execute : hasCards <- ?cardsOnHand(Xh);
						?cardsOnTable(Xt);
						!selectAction(Xh,Xt);
						!endTurn.
						
+!execute : not hasCards <- +gameEnded.		

//Non ci sono carte sul tavolo, allora seleziono a caso dal mazzo
+!selectAction(Xh,[]) : true <- .print("Carte in mano ", Xh);
								playerLib.selectCardRandom(Xh, Card, Taking);
								.my_name(Name);
								-+intendedAction(action(Name,Card,Taking)).
								
//Se carte sono sul tavolo allora devo ragionare
+!selectAction(Xh,Xt) : true <- .print("Carte in mano ", Xh);
								.print("Carte sul tavolo ", Xt);
								!evaluateCards(Xh, Xt);
								.my_name(Name);
								-+intendedAction(action(Name,Card,Taking)).
								
+!evaluateCards([],Xt) : true .
+!evaluateCards([Card|Xs],Xt) : true <- .print("Valuto la carta ", Card);
										playerLib.trustScopa(Card,Xt,Trust);
										+trust(Card,scopa,Trust).
			

+intendedAction(Action) : true <- .print("Azione selezionata: ", Action);
								//playAction(Action);
								.broadcast(tell,Action). //Notifica gli altri giocatori dell'azione compiuta

+gameEnded <- .my_name(Name);
				playerLib.getScore(Score);
				.print("Il mio punteggio finale: ", Score).

/* Plans failure handling*/
+!doMove
	: true
	<- .current_intention(I); // notice INTERNAL action to retrieve the execution "context"
		.print("Failed to achieve goal '!doMove'. Current intention is: ", I). // print debug info	
