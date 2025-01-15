/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode min;
	public int HeapSize;
	public HeapNode first;
	public int HeapNumTrees;
	public int HeapTotalLinks;
	public int HeapTotalCuts;
	
	
	/**
	 *
	 * Constructor to initialize an empty heap.
	 *
	 */
	public FibonacciHeap()
	{
		this.HeapSize = 0;
		this.first = null;
		this.min = null;
		this.HeapNumTrees = 0;
		this.HeapTotalLinks = 0;
		this.HeapTotalCuts = 0;
	}

	/**
	 * 
	 * pre: key > 0
	 *
	 * Insert (key,info) into the heap and return the newly generated HeapNode.
	 *
	 */
	public HeapNode insert(int key, String info) 
	{    
		HeapNode newNode = new HeapNode(key,info);
		insert(newNode);
	    // עדכון גודל ההיפ 
	    HeapSize++;
	    return newNode;
	}
	
	private HeapNode insert(HeapNode newNode) {
		
		// אם ההיפ ריק
	    if (min == null) {
	        min = newNode;
	        first = newNode; // הצומת הראשון בהיפ
	    } else {
	        // הוספת הצומת לרשימת השורשים
	        newNode.next = first;
	        newNode.prev = first.prev;
	        first.prev.next = newNode;
	        first.prev = newNode;
	        // עדכון הראשון (לשמור על first)
	        first = newNode;
	        // עדכון המינימום אם המפתח של הצומת החדש קטן מהמינימום
	        if (newNode.key < min.key) {
	            min = newNode;
	        }
	    }
		
	    HeapNumTrees++;
		return newNode;
	}

	/**
	 * 
	 * Return the minimal HeapNode, null if empty.
	 *
	 */
	public HeapNode findMin()
	{
		return this.min; 
	}

	/**
	 * 
	 * Delete the minimal item
	 *
	 */
	public void deleteMin()
	{
		deleteMin(true);

	}
	
	/**
	 * 
	 * Delete the minimal item
	 * if doesOGmin is true do Consolidating and minimum update, else don't
	 */
	private void deleteMin(boolean doesOGmin)
	{
		// מחיקה מערימה ריקה - מקרה קצה
		if (this.min == null) {
        return; 
		}
		if (this.min.child != null) {
        HeapNode child = this.min.child;
        do {
            child.parent = null; // ניתוק מהצומת הנמחקת
            child = child.next;
        } while (child != this.min.child);

        // הוספת הילדים לרשימת השורשים של הצומת הנמחקת
        mergeRootLists(this.min, this.min.child);

    }
		// מחיקת הצומת
		removeFromRootList(this.min);

		if (doesOGmin=true){
			this.consolidate();
			this.updateMin(); // צריך להוסיף את הפונקציה הזאת
		}

		this.HeapSize--;
            //  מאי: תשים לב להפריד בין שני מקרים - אם מתקבל קלט "אמת"  צריך לעשות מחיקה כמו שלמדנו בכיתה, אחרת, צריך למחוק את המינימום ולא לעשות את תהליך החיבור עצים רק להוסיף אותם לשורש
            // אם מוחקים צומת שהוא לא באמת המינימום , כלומר המקרה בו מתקבל "שקר" אין צורך לעדכן את המינימום בסיום הפעולה
	}
	
	// פונקציות עזר למחיקה
	/**
	 * חיבור שתי רשימות שורשים
	 */
	private void mergeRootLists(HeapNode node1, HeapNode node2) {
		if (node1 == null || node2 == null) {
			return; // מקרה קצה - אחד הרשימות ריקה
		}

		HeapNode temp = node1.next;
		node1.next = node2.next;
		node2.next.prev = node1;
		node2.next = temp;
		temp.prev = node2;
	}
	/**
	 * מחיקת צומת ע״י שינוי מצביעים
	 */
	private void removeFromRootList(HeapNode node) {
		if (node == node.next) {
			// מחיקת הצומת היחיד ברשימה
			first = null;
		} else {
			node.prev.next = node.next;
			node.next.prev = node.prev;
			if (first == node) {
				first = node.next; // עדכון הצומת הראשון אם נדרש
			}
 		}	
	}


	/**
	 * חיבור עצים מדרגות שוות ע״פ המפתח
	 */
	private void linkNodes(HeapNode child, HeapNode parent) {
		removeFromRootList(child);
		child.parent = parent;

		if (parent.child == null) {
			parent.child = child;
			child.next = child;
			child.prev = child;
		} else {
			child.next = parent.child.next;
			child.prev = parent.child;
			parent.child.next.prev = child;
			parent.child.next = child;
		}

		parent.rank++;
		child.mark = false;
		this.HeapTotalLinks++;
	}

	/**
	 * Consolidates the root list to ensure unique degrees for all trees.
	 */
	private void consolidate() {
		int arraySize = (int) Math.ceil(Math.log(HeapSize) / Math.log(2)) + 1;
		HeapNode[] rankTable = new HeapNode[arraySize];

		HeapNode current = first;
		int numRoots = 0;

		// Count the number of nodes in the root list
		do {
			numRoots++;
			current = current.next;
		} while (current != first);

		while (numRoots > 0) {
			int rank = current.rank;
			HeapNode next = current.next;

			// Merge trees with the same degree
			while (rankTable[rank] != null) {
				HeapNode other = rankTable[rank];

				if (current.key > other.key) {
					HeapNode temp = current;
					current = other;
					other = temp;
				}

				linkNodes(other, current);
				rankTable[rank] = null;
				rank++;
			}

			rankTable[rank] = current;
			current = next;
			numRoots--;
		}
	}

	/**
	 * עדכון המצביע למינימום
	 */
	private void updateMin(){
		HeapNode current = first;
		do {
        	if (this.min != null) {
            	if (this.min == null || current.key < min.key) {
                	this.min = current;
            	}}
        	current = current.next;
		} while (current != first);
	}
	



	/**
	 * 
	 * pre: 0<diff<x.key
	 * 
	 * Decrease the key of x by diff and fix the heap. 
	 * 
	 */
	public void decreaseKey(HeapNode x, int diff) 
	{    
		// הפחתת המפתח של הצומת
	    x.key = x.key - diff;

	    HeapNode parent = x.parent;

	    // אם לצומת יש הורה והמפתח החדש קטן מהמפתח של ההורה
	    if (parent != null && x.key < parent.key) {
	        // חיתוך הצומת מההורה
	        cut(x, parent);

	        // אם ההורה מסומן, מבצעים חיתוך מדורג עליו
	        if (parent.mark) {
	            cascading_cut(parent, parent.parent);
	        } else {
	            // אם זו הפעם הראשונה שההורה מאבד ילד, מסמנים אותו
	            parent.mark = true;
	        }
	    } else {
	    	// עדכון המינימום אם המפתח החדש של הצומת קטן מהמינימום הנוכחי
	    	if (x.key < min.key) {
	    		min = x;
	    	}
	    }
	}
	
	private void cut(HeapNode x, HeapNode y) {
		this.HeapTotalCuts++;
		x.parent = null;
		x.mark = false;
		y.rank = y.rank -1 ;
		// אם איו לצומת ההורה עוד ילדים
		if (x.next==x) {
			y.child = null;
		}
		// אם יש עוד ילדים
		else {
			y.child = x.next;
			x.prev.next = x.next;
			x.next.prev = x.prev;
		}
		
		// הוספת הצומת לרשימת השורשים
	    insert(x);
	}
	
	private void cascading_cut(HeapNode x, HeapNode y) {
		cut(x,y);
		if(y.parent!=null) {
			if (y.mark==false) {
				y.mark = true;
			} else {
				cascading_cut(y,y.parent);
			}
		}
	}
	

	/**
	 * 
	 * Delete the x from the heap.
	 *
	 */
	public void delete(HeapNode x) 
	{    
		if (x == null) {
	        return;
	    }
		
		// אם הצומת הוא המינימום
	    if(this.min == x ) {
	    	// הסרת המינימום
	    	deleteMin();
	    	
	    } else {
	    	
	    	HeapNode OGmin = this.min;
	    	
	    	// להפחית את המפתח של הצומת ל-Integer.MIN_VALUE כדי שיהפוך למינימום
	        decreaseKey(x, x.key - Integer.MIN_VALUE);
	        // מחיקת צומת שהיא לא מינימום אמיתי
	        deleteMin(false);
	        this.min = OGmin;
	    }
	
	    
	    
	    
	}
	

	
	/**
	 * 
	 * Return the total number of links.
	 * 
	 */
	public int totalLinks()
	{
		return this.HeapTotalLinks; 
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return this.HeapTotalCuts; 
	}


	/**
	 * 
	 * Meld the heap with heap2
	 *
	 */
	public void meld(FibonacciHeap heap2)
	{
		return; // should be replaced by student code   		
	}

	/**
	 * 
	 * Return the number of elements in the heap
	 *   
	 */
	public int size()
	{
		return this.HeapSize;
	}


	/**
	 * 
	 * Return the number of trees in the heap.
	 * 
	 */
	public int numTrees()
	{
		return this.HeapSize;
	}


	/**
	 * Class implementing a node in a Fibonacci Heap.
	 *  
	 */
	public static class HeapNode{
		public int key;
		public String info;
		public HeapNode child;
		public HeapNode next;
		public HeapNode prev;
		public HeapNode parent;
		public int rank;
		public boolean mark;
		
		public HeapNode(int key, String info) {
			this.key = key;
			this.info = info;
			this.next = this;
			this.prev = this;
			this.mark = false;
			this.rank = 0;
			this.child = null;
		}
	}
}
