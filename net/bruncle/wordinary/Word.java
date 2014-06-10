package net.bruncle.wordinary;
public class Word implements Comparable{

	private String inEnglish;
	private String inOtherLanguage;
	private String category;
	private String hint;
	private int correct = 0;
	private int id = -1;
	
	public Word(String inEnglish, String inOtherLanguage, String category, int correct){
		this.inEnglish = inEnglish;
		this.inOtherLanguage = inOtherLanguage;
		this.category = category;
		this.correct = correct;
	}
	
	public void setWord(String inEnglish, String inOtherLanguage, String category, int correct){
		this.inEnglish = inEnglish;
		this.inOtherLanguage = inOtherLanguage;
		this.category = category;
		this.correct = correct;
	}
	
	public String[] toStrings(){
		return new String[] {inEnglish,inOtherLanguage,category,""+correct,hint};
	}
	
	public String toString(){
		return inEnglish+"-->"+inOtherLanguage+"-->"+category+"-->"+correct;
	}
	
	public String getCategory(){
		return category;
	}
	
	public String getInEnglish(){
		return inEnglish;
	}
	
	public void setInEnglish(String inEnglish){
		this.inEnglish = inEnglish;
	}
	
	public String getInOtherLanguage(){
		return inOtherLanguage;
	}
	
	public void setInOtherLanguage(String inOtherLanguage){
		this.inOtherLanguage = inOtherLanguage;
	}
	
	public int getNumberOfTimesCorrect(){
		return correct;
	}
	
	public void setNumberOfTimesCorrect(int correct){
		this.correct = correct;
	}
	
	public void setID(int id){
		this.id = id;
	}
	
	public int getID(){
		return id;
	}
	
	public boolean equalsIgnoreCase(Word toCheck){
		return((toCheck.getInEnglish().equalsIgnoreCase(getInEnglish()) ||
			toCheck.getInOtherLanguage().equalsIgnoreCase(getInOtherLanguage())) ||
			toCheck.getID() == getID());
	}
	
	public boolean equals(Word toCheck){
		return (toCheck != null && toCheck.getID() == getID());
	}
	
	public String getMatchingString(String toCheck){
		if (toCheck.indexOf(getInEnglish()) >= 0){
			return getInOtherLanguage();
		}
		else if (toCheck.indexOf(getInOtherLanguage()) >= 0){
			return getInEnglish();
		}
		else
			return null;
	}
	
	public void setHint(String hint){
		this.hint = hint;
	}
	
	public String getHint(){
		return hint;
	}
	
	public Word clone(){
		Word clone = new Word(inEnglish, inOtherLanguage, category, correct);
		clone.setHint(hint);
		return clone;
	}
	
	public int compareTo(Object anotherWord) throws ClassCastException {
		if (!(anotherWord instanceof Word))
	    		throw new ClassCastException("Wrong type of object supplied to compareTo in Word.class");
		return toString().compareToIgnoreCase(anotherWord.toString());   
	}
}