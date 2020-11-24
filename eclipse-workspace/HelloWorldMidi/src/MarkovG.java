
import java.util.ArrayList;
public class MarkovG<T> extends ProbabilityGenerator<T>{
	int lastIndex = -1;
	int tokenIndex = 0;
	// set new variable 
	ArrayList<ArrayList<Integer>> transitionTableCount = new ArrayList<ArrayList<Integer>>(); 
	ArrayList<ArrayList<Float>> transitionTable = new ArrayList<ArrayList<Float>>();
	/*
		for each token in the input array 
		{
			tokenIndex = the index of the token in the alphabet
			if the current token is not found in the alphabet
			{
				  1. tokenIndex = size of alphabet
                                  2. add a new row to the transition table (expand vertically)
                                  That is, create a new array that is the size of the alphabet 
                                  Then add to your transition table (the array of arrays)
                                  3. add a new column (expand horizontally)
                                  That is, add a 0 on to all of the arrays in the transition table.
                                  That is, for each array in the transition table add 0.
		
	 */
	//train class 
	public void train() {
		for(int i = 0; i < newTokens.size(); i++) {
			if(alphabet.contains(newTokens.get(i)) == false) {
				tokenIndex = alphabet.size();
				alphabet.add(newTokens.get(i));
				expandTable();
				if(alphabet.size() > 1)
					transitionTableCount.get(alphabet.indexOf(newTokens.get(lastIndex))).set(tokenIndex, 1);
			} else { //if token is in 				
				int in = transitionTableCount.get( alphabet.indexOf(newTokens.get(lastIndex))).get(alphabet.indexOf(newTokens.get(i)));
				in++;
				transitionTableCount.get(alphabet.indexOf(newTokens.get(lastIndex))).set(alphabet.indexOf(newTokens.get(i)),in);
			}
			lastIndex = i; 
		}
	}
	private void expandTable() {// expand table 
		ArrayList<Integer> table = new ArrayList<Integer>();
		ArrayList<Float> tablef = new ArrayList<Float>();
		for(int i = 0; i < transitionTableCount.size(); i++) {
			table.add(0);
			tablef.add((float) 0);
		}
		transitionTableCount.add(table);
		transitionTable.add(tablef);
		for(int i = 0; i < transitionTableCount.size(); i++) {
			transitionTableCount.get(i).add(0);
			transitionTable.get(i).add((float) 0);
		}
	}
	public void normalize() {
		for(int i = 0; i < transitionTableCount.size(); i++) {
			int sum = 0;
			for(int j = 0; j < transitionTableCount.get(i).size(); j++)
				sum += transitionTableCount.get(i).get(j);
			for(int j = 0; j < transitionTableCount.get(i).size(); j++) {
				float k = transitionTableCount.get(i).get(j);
				if(sum == 0)
					k = 0;
				else
					k /= sum;
				transitionTable.get(i).set(j,k);
			}
		}
		
	}
	public void print(){ 
		for(int i = 0; i < transitionTable.size(); i++) {
			System.out.print("|");
			for(int j = 0; j < transitionTable.get(i).size(); j++) {
				System.out.print(transitionTable.get(i).get(j) + ", ");
			}
			System.out.println("|");
		}
	}
	//generate function
	void generate(T initToken) {
		//clear array 
		genarray.clear();
		for(int i = 0; i < newTokens.size(); i++) {
			tokenIndex = alphabet.indexOf(initToken);
			T generateToken = generateToken(tokenIndex);
			genarray.add(generateToken);
			initToken = generateToken;
		}
	}
	public T generateToken(int i) {//generate tokens 
		float randIndex = (float)Math.random();
		boolean found = false;
		int index = 0;
		float total = 0;
	
		while(!found && index < alphabet.size()) {
			total += transitionTable.get(i).get(index);
			found = randIndex <= total;
			index++;
		}
		if(found) return (T) alphabet.get(index-1);
		else return (T) alphabet.get(alphabet.size()-1);
	}
}
