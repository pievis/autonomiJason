package utils;

import it.unibo.scopone.impl.Card;
import it.unibo.scopone.interfaces.ICard;
import it.unibo.scopone.structs.Action;
import it.unibo.scopone.structs.Seed;
import jason.asSyntax.ListTerm;
import jason.asSyntax.Literal;
import jason.asSyntax.Term;

import java.util.ArrayList;
import java.util.List;

public class PrologUtils {

	public static Literal cardListToLiteral(List<ICard> l) {
		String str = cardListToStrRapp(l);
		return Literal.parseLiteral(str);
	}

	public static String cardListToStrRapp(List<ICard> l) {
		if (l.size() == 0)
			return "[]";
		String str = "[";
		int size = l.size();
		for (int i = 0; i < size - 1; i++) {
			str += l.get(i).getCardStr() + ",";
		}
		str += l.get(size - 1).getCardStr() + "]";
		return str;
	}

	public static Term generateActionTerm(Action action) {
		String str = action.getRep();
		return Literal.parse(str);
	}

	// card(10,bastoni)
	public static ICard parseCard(String cardStr) {
		String numStr = cardStr.split("\\(")[1].split(",")[0].replace(" ", "");
		int num = Integer.parseInt(numStr);
		String seedStr = cardStr.split(",")[1].split("\\)")[0].replace(" ", "");
		Seed seed = Seed.valueOf(seedStr.toUpperCase());
		return new Card(num, seed);
	}
	
	public static List<ICard> parseCardList(ListTerm cardListStr) {
		List<ICard> list = new ArrayList<ICard>();
		for(Term t : cardListStr){
			list.add(parseCard(t+""));
		}
		return list;
	}

	// /
	public static void main(String[] args) {
		String cardStr = "card(10,bastoni)";
		System.out.println(parseCard(cardStr).getCardStr());
	}
}
