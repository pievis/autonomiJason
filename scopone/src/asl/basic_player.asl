// Agent basic_player in project scopone

/* Initial beliefs and rules */
//Perceptions
//cardsOnHand([]). //lista di carte che ha in mano, inizialmente vuota
//cardsOnTable([]). //Non ha ancora percezione delle carte in gioco
//deck([]). //Il mazzetto
//nextPlayer(Name). //conosce il gioco del prossimo giocatore
//~gameEnded. //esiste se il gioco è terminato

scope_count(p1,0).
current_turn(1).
canStart :- .my_name(MyName) & (p1 == MyName).
hasCards :- cardsOnHand(Xl) & .length(Xl, X) & X \== 0.

/* Initial goals */

//Se sei il giocatore p1 allora devi iniziare la partita
!startGame.

/* Plans */

+!startGame : canStart <- !doMove.
+!startGame : not canStart <- .print("ready").

+!updateTurn : true <- ?current_turn(N);
						 NewTurn = N+1; 
						 -+current_turn(NewTurn).

+!doMove : not gameEnded <- !updateTurn; !execute.
					
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
								!selectByTrust.

//valuta la finducia nello scegliere una determinata carta
//ragiona in base ai diversi sotto obbiettivi del gioco (fare denari, scope, ecc)
+!evaluateCards([],Xt) : true .
+!evaluateCards([Card|Xs],Xt) : true <- .print("Valuto la carta ", Card);
										!!evaluateTrustScopa(Card,Xt);
										!!evaluateTrustDenari(Card,Xt);
										!!evaluateTrustCarte(Card,Xt);
										!!evaluateTrustSettebello(Card,Xt);
										!!evaluateTrustPrimiera(Card,Xt);
										!evaluateCards(Xs,Xt).
										
///Trust evaluation	/////////////////////////////////////////////							
+!evaluateTrustScopa(Card,Xt) : true <- playerLib.trustScopa(Card,Xt,Trust);
										?current_turn(N);
										+trust(N,Card,scopa,Trust).
										
+!evaluateTrustCarte(Card,Xt) : true <- playerLib.trustCarte(Card,Xt,Trust);
										?current_turn(N);
										+trust(N,Card,carte,Trust).

+!evaluateTrustSettebello(Card,Xt) : true <- playerLib.trustSettebello(Card,Xt,Trust);
										?current_turn(N);
										+trust(N,Card,settebello,Trust).

+!evaluateTrustPrimiera(Card,Xt) : true <- playerLib.trustPrimiera(Card,Xt,Trust);
										?current_turn(N);
										+trust(N,Card,primiera,Trust).
										
+!evaluateTrustDenari(Card,Xt) : true <- playerLib.trustDenari(Card,Xt,Trust);
										?current_turn(N);
										+trust(N,Card,denari,Trust).

///////////////////////////////////////////////////////////////////////

+!selectByTrust : true <- 	?current_turn(Turn);
							.findall(trust(Card,Obj,Val), (trust(Turn,Card,Obj,Val) & Val > 0.0), L); //tutti i valori di fiducia per il contesto corrente
							.print("trust list: ", L);
							playerLib.selectCardWithThinking(L, Card, Taking);
							.my_name(Name);
							-+intendedAction(action(Name,Card,Taking)).

+intendedAction(Action) : true <- .print("Azione selezionata: ", Action);
								playAction(Action); //External action
								.broadcast(tell,Action). //Notifica gli altri giocatori dell'azione compiuta

+gameEnded <- .my_name(Name);
				playerLib.getScore(Score);
				.print("Il mio punteggio finale: ", Score).

/* Plans failure handling*/
+!doMove
	: true
	<- .current_intention(I); // notice INTERNAL action to retrieve the execution "context"
		.print("Failed to achieve goal '!doMove'. Current intention is: ", I). // print debug info	
