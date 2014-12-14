// Agent basic_player in project scopone

/* Initial beliefs and rules */
//Perceptions
//cardsOnHand([]). //lista di carte che ha in mano, inizialmente vuota
//cardsOnTable([]). //Non ha ancora percezione delle carte in gioco
//deck([]). //Il mazzetto
//nextPlayer(Name). //conosce il gioco del prossimo giocatore
//~gameEnded. //esiste se il gioco è terminato

//scope_count(p1,0).
//scope_count(p2,0).
//scope_count(p3,0).
//scope_count(p4,0).

//trust factors
//Definiscono il profilo del giocatore, ovvero i pesi per i relativi
//ragionamenti. In questo senso il giocatore ha un carattere
//definito nel suo "patrimonio genetico".
trust_factor(carte,0.5).
trust_factor(scopa,0.8).
trust_factor(settebello,1.0).
trust_factor(primiera,2.6).
trust_factor(denari,3.8).

current_turn(1).
canStart :- .my_name(MyName) & (p1 == MyName).
//hasCards(cardsOnHand(Xl)) :- cardsOnHand(Xl) & Xl \== [].
hasCards.

/* Initial goals */

//Se sei il giocatore p1 allora devi iniziare la partita
!startGame.

/* Plans */

+!startGame : canStart <- !tellScopeCount; !doMove.
+!startGame : not canStart <- !tellScopeCount;.print("ready").

//partono tutti con lo stesso valore di scope
+!tellScopeCount : true <- 	.my_name(Name); 
							+scope_count(Name,0);
							.broadcast(tell,scope_count(Name,0)).

+!updateTurn : true <- ?current_turn(N);
						 NewTurn = N+1; 
						 -+current_turn(NewTurn).

+!doMove : not gameEnded <- !updateTurn; 
							!execute;
							!seeIfDone.
							
+!seeIfDone : true <- ?cardsOnHand(Xl);
					 	Xl == []; //if I've no cards
					 	-hasCards. //update my belief

+!checkScopa : true <- ?cardsOnTable(Xt);
						!updateScopa(Xt).
						
+!updateScopa(Xt) : Xt == [] <- .my_name(Name);
								?scope_count(Name,N);
								Nscope = N+1;
								.print("Scopa! scope=",Nscope);
								.broadcast(untell,scope_count(Name,N));
								.broadcast(tell,scope_count(Name,Nscope));
								-+scope_count(Name,Nscope).
					
+!endTurn : true <- .print("my turn ended");
					?nextPlayer(NextP);
					.send(NextP,achieve,doMove). //tells the next agent to do his move

+!execute : hasCards <- ?cardsOnHand(Xh);
						?cardsOnTable(Xt);
						!selectAction(Xh,Xt);
						!checkScopa; //controlla se hai fatto scopa
						!endTurn.
						
+!execute : not hasCards <- +gameEnded; !tellEveryoneGameEnded.

+!tellEveryoneGameEnded : gameEnded <- .broadcast(tell,gameEnded).	

//Non ci sono carte sul tavolo, allora seleziono a caso dal mazzo
+!selectAction(Xh,[]) : true <- .print("Carte in mano ", Xh);
								!selectRandom(Xh).
								
+!selectRandom(Xh) : hasCards <-playerLib.selectCardRandom(Xh, Card, Taking);
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
+!evaluateCards([Card|Xs],Xt) : true <- //.print("Valuto la carta ", Card);
										!!evaluateTrustScopa(Card,Xt);
										!!evaluateTrustDenari(Card,Xt);
										!!evaluateTrustCarte(Card,Xt);
										!!evaluateTrustSettebello(Card,Xt);
										!!evaluateTrustPrimiera(Card,Xt);
										!evaluateCards(Xs,Xt).
										
///Trust evaluation	/////////////////////////////////////////////							
+!evaluateTrustScopa(Card,Xt) : true <- playerLib.trustScopa(Card,Xt,Trust);
										?current_turn(N);
										?trust_factor(scopa,X);
										+trust(N,Card,scopa,Trust*X).
										
+!evaluateTrustCarte(Card,Xt) : true <- playerLib.trustCarte(Card,Xt,Trust);
										?current_turn(N);
										?trust_factor(carte,X);
										+trust(N,Card,carte,Trust*X).

+!evaluateTrustSettebello(Card,Xt) : true <- playerLib.trustSettebello(Card,Xt,Trust);
										?current_turn(N);
										?trust_factor(settebello,X);
										+trust(N,Card,settebello,Trust*X).

+!evaluateTrustPrimiera(Card,Xt) : true <- playerLib.trustPrimiera(Card,Xt,Trust);
										?current_turn(N);
										?trust_factor(primiera,X);
										+trust(N,Card,primiera,Trust*X).
										
+!evaluateTrustDenari(Card,Xt) : true <- playerLib.trustDenari(Card,Xt,Trust);
										?current_turn(N);
										?trust_factor(denari,X);
										+trust(N,Card,denari,Trust*X).

///////////////////////////////////////////////////////////////////////

+!selectByTrust : true <- 	?current_turn(Turn);
							.findall(trust(Card,Obj,Val), (trust(Turn,Card,Obj,Val) & Val > 0.0), L); //tutti i valori di fiducia per il contesto corrente
							.print("trust list: ", L);
							!actByTrust(L).
							

+!actByTrust([]) : true <- ?cardsOnHand(Xh); //non ho fiducia in nessuna carta, allora scelgo random
							!selectRandom(Xh).
							
+!actByTrust(L) : true <- playerLib.selectCardWithThinking(L, Card, Taking);
							.my_name(Name);
							-+intendedAction(action(Name,Card,Taking)).

+intendedAction(Action) : true <- .print("Azione selezionata: ", Action);
								playAction(Action); //External action
								.broadcast(tell,Action). //Notifica gli altri giocatori dell'azione compiuta
//Il gioco è terminato
+gameEnded : not hasCards <- !getScore.

+!getScore : true <- .my_name(Name);
				?deck(Deck);
				?scope_count(Name,Nscope);
				playerLib.getScore(Deck,Nscope,Score);
				+score(Name,Score);
				.print("Il mio punteggio finale: ", Score);
				!tellEveryoneScore(score(Name,Score)).
				
				
+!tellEveryoneScore(ScoreTerm) : true <- .broadcast(tell,ScoreTerm);
										!tellWinner.


+!tellWinner : true <- .count(score(Ag,_),N);
						N >= 4; //posso dire il vincitore solo se so il punteggio di tutti
						.findall(score(Name,Val), score(Name,Val), L);
						playerLib.getWinner(L,Winner);
						.print("Risultato: ", Winner).

//+score(Ag,Score)[source(S)] : S == Ag <- .my_name(Name); 
//											?score(Name,X);
//											X > Score;
//											+winner(Name,Ag).
//
//+!checkImWinner : true <- .my_name(Name);
//						.count(winner(Name,_),N);
//						N >= 3;
//						.print("Ho vinto!").
						

/////////
+!doMove
	: true
	<- .current_intention(I); // notice INTERNAL action to retrieve the execution "context"
		.print("Failed to achieve goal '!doMove'. Current intention is: ", I). // print debug info	
		
+!selectRandom(Xh) : not hasCards <- .print("Non posso scegliere se non ho carte").

+!tellEveryoneGameEnded : not gameEnded <- .print("Non voglio confondere gli altri giocatori").

+gameEnded : hasCards <- .print("Io ho ancora delle carte").

//+scope_count(Ag,N)[source(S)] : S == Ag <- -+scope_count(Ag,N).

/* Plans failure handling*/
-!getScore : true <- .current_intention(I); // notice INTERNAL action to retrieve the execution "context"
		.print("Failed to achieve goal '!getScore'. Current intention is: ", I). // print debug info
		
-!seeIfDone : true. //è previsto che fallisca
		
-!updateScopa(_) : true . //nessuna scopa in questa mano

-!score(Ag,_) : true <- .print(Ag," ha fatto più punti di me."); .my_name(Name); +loser(Name,Ag).

-!checkImWinner : true .

-!tellWinner : true . //previsto che fallisca

-score(_,_)[source(_)] : true .