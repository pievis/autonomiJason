package utils;

import it.unibo.scopone.interfaces.ICard;
import jason.asSyntax.Literal;

import java.util.List;

public class PrologUtils {
	
	public static Literal cardListToLiteral(List<ICard> l){
		String str = cardListToStrRapp(l);
		return Literal.parseLiteral(str);
	}
	
	public static String cardListToStrRapp(List<ICard> l){
		String str = "[";
		int size = l.size();
		for(int i = 0; i < size-1; i++){
			str+=l.get(i).getCardStr() +",";
		}
		str+= l.get(size-1).getCardStr() + "]";
		return str;
	}
	
}
