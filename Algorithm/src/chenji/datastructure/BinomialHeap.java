package chenji.datastructure;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class BinomialHeap {
	
	private class SortByDegree implements Comparator {
		 public int compare(Object o1, Object o2) {
			 BinomialNode s1 = (BinomialNode) o1;
			 BinomialNode s2 = (BinomialNode) o2;
			 if( s1.degree < s2.degree)
				 return 1;
			 return 0;
		}
	}
	
	private class BinomialNode{
		public float value;
		public BinomialNode  right;
		public BinomialNode  child;
		public int degree;
		public BinomialNode(float value){
			this.value = value;
			this.degree = 0;
			this.right = null;
			this.child = null;
		}
		
		public BinomialNode(float value,BinomialNode right,BinomialNode child){
			this.value = value;
			this.child = child;
			this.right = right;
		}
		
		public BinomialNode addChild(BinomialNode other){
			other.right = this.child;
			this.child = other;
			this.degree++;
			return this;
		}
	}
	
	private List<BinomialNode> tree;
	
	public BinomialHeap(){
		this.tree = new ArrayList<BinomialNode>();
	}
	
	private BinomialNode MergeTree(BinomialNode left,BinomialNode right){
		if(left.value < right.value){
			return left.addChild(right);
		}
		else
			return right.addChild(left);		
	}
	
	public BinomialHeap addTreeToHeap(BinomialNode node){
		Collections.sort(tree, new SortByDegree());
		Iterator it = tree.iterator();
		BinomialNode tmp,node1 = node;
		boolean flags = true;
		while(it.hasNext()){
			tmp = (BinomialNode) it.next();
			if(node1.degree == tmp.degree){
				tmp = MergeTree(tmp,node1);
				flags = false;
			}
			if(!flags){
				
			}
		}
		if(flags){
			tree.add(node);
		}		
		return this;		
	}
	
	public BinomialHeap MergeHeap(BinomialHeap heap){
		return this;
	}
	
}
