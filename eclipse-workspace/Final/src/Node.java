import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
/*
 one node in the prediction suffix tree.
 */
public class Node<T> extends ProbabilityGenerator<T>{
	
	ArrayList<T> tSeq;
	ArrayList<Node<T>> nodeChild = new ArrayList<Node<T>>();
	int nodeCount=1;
	boolean hasSeqAtEndOfDataset;/*Is the tokenSequence of this node at the end? – can be in
	constructor or create a set method to assign the value from the tree*/
	float this_r;/* – the r value of the node. I make this a class variable so it is easy to print out in the
	print() for testing*/
	Node<T> nextToken;/* – the token that comes after this node’s tokenSequence. Set in a parameter in the
	constructor.*/
	public Node(ArrayList<T> tokenSequence) { // Constructor
		this.tSeq = tokenSequence;
	}
	public Node() { //constructor
		tSeq = new ArrayList<T>(); 
	}
	
	public boolean addNode(Node<T> node) {
		boolean found = false;
		int i = 0;
		if(tSeq.equals(node.tSeq)) {// if tokenSequence node == add node
			found = true;
			nodeCount++;			
			if(!tSeq.equals(node.tSeq))
				trainViaProbGen(node);
		}else if(amISuffix(node)||(tSeq.size()==0)) {
				while(!found && i < nodeChild.size()) {
					found = nodeChild.get(i).addNode(node);//add nood to children
					i++;
				}
			if(!found) {
				nodeChild.add(node);
				node.trainViaProbGen(node);
				found = true;
			}
		}
		return found;
	}
	
	/*performs elimination based on an empirical
	probability threshold PMin. Returns whether to delete this node or not. The parent node performs the deletion.
	*/
	public boolean pMinElimination( int totalTokens, float pMin )
	{
		return false;
		
	}
	/*
	 *  – performs elimination based on the R-values. Returns
	whether to delete this node or not. The parent node performs the deletion
	 * */
	public boolean rElimination(float r, Node myRoot)
	{
		boolean shouldRemove = false;
		if(tSeq.size() > 1)
			shouldRemove = true;
		if(shouldRemove)
		{
			//Find the r of this node
			ArrayList<T> pRatio = (ArrayList<T>)Collections.max(myRoot.alphabetcount);//super.alphabetcount.size();
			int myRatio = pRatio.size()/super.alphabetcount.size();
			ArrayList<T> pRootRatio = (ArrayList<T>)Collections.max(myRoot.tSeq);//super.alphabetcount.size();
			int rootRatio = pRatio.size()/super.alphabetcount.size();
			this_r = myRatio / rootRatio;
			shouldRemove = this_r < r;			
			if(!shouldRemove)
				for (Iterator<T> i = myRoot.tSeq.iterator(); i.hasNext();)  
				{
					if(rElimination(this_r, myRoot))
					{
						tSeq.remove(i);
					}
				}
		}
		return shouldRemove;
		
	}
	/*
	 * – generate new tokens via the PST
	 * */
	public T generate(ArrayList initSeq)
	{
		//the new token to return
		T newToken = null;
		if(tSeq.equals(initSeq))		
			return super.generateToken();		
		else if(initSeq.contains(tSeq))
		{
			T tIter = generate(initSeq);
			if(tIter != null)
				return tIter;
		}
		super.generate();
		return newToken;
	}
	public ArrayList<T> generate(ArrayList initSeq, int length)
	{
		ArrayList<T> retSeq = null;
		for(int i=0; i< length; i++)
			retSeq.add(generate(initSeq));// for length times and return an ArrayList with the result
		return retSeq;
	}
	
	//***you should fill in these methods yourself, so they won’t appear below***
	public void trainViaProbGen(Node<T> node)
	{
		// – keeps track of probability distribution of the next tokens
		float probab = node.probability;
		if(probab > 1)
			node.probability = 1;
		if(probab < 0)
			node.probability = 0;
	}
	
	public float getCountsAtToken(T token)
	{
		float nIndex = 0;
		for(T nIter: tSeq)
		{
			nIndex ++;
			if(nIter.equals(token))
				return nIndex;
		}
		return nodeCount;
	}
	
	public boolean amISuffix(Node<T> node) {//use sublist fromindex to toinddex
		if((node.tSeq.size() - tSeq.size()) < 0)
			return false;		
		return tSeq.equals(node.tSeq.subList(node.tSeq.size() - this.tSeq.size(), node.tSeq.size()));
	}
	public boolean pMinElim(int total, double pmin) {
		boolean remove = false;
		float num = (nodeCount/(float)(total-(tSeq.size()-1)));//sequence have occur
		
		if(num <= pmin && tSeq.size() != 0) {
			remove = true;
		}else {
				for(int i = nodeChild.size()-1; i >= 0; i--) {
					if(nodeChild.get(i).pMinElim(total, pmin)) {
						nodeChild.remove(nodeChild.get(i));//do removing
					}
				}
				remove = false;
		}
		return remove; 
	}
	public void print() {//to print
		
		System.out.println(tSeq); 
		for(int i = 0; i < nodeChild.size(); i++) 
			nodeChild.get(i).print(1);
	}
	public void clear() {//clear 
		tSeq.clear();
		for(int i = 0; i < nodeChild.size(); i++)
			nodeChild.get(i).clear();
		
	}
	public void print(int numspaces) {		
				
		for(int i = 1; i <= numspaces; i++) {
			System.out.print(" ");
		}
		System.out.print("-->");
		System.out.println(tSeq);
		for(int j = 0; j < nodeChild.size(); j++) 
			nodeChild.get(j).print(numspaces + 1);
	}
	
	
}