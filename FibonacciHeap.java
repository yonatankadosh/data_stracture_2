/**
 * FibonacciHeap
 *
 * An implementation of Fibonacci heap over positive integers.
 *
 */
public class FibonacciHeap
{
	public HeapNode min;
	private int HeapSize;
	private HeapNode first;
	private int HeapNumTrees;
	
	
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
	        if (key < min.key) {
	            min = newNode;
	        }
	    }

	    // עדכון גודל ההיפ ומספר העצים
	    HeapSize++;
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
		return this.min; // should be replaced by student code
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
	
	
	public void deleteMin(boolean doesOGmin)
	{
		return; //  מאי: תשים לב להפריד בין שני מקרים - אם מתקבל קלט "אמת"  צריך לעשות מחיקה כמו שלמדנו בכיתה, אחרת, צריך למחוק את המינימום ולא לעשות את תהליך החיבור עצים רק להוסיף אותם לשורש 
		// אם מוחקים צומת שהוא לא באמת המינימום , כלומר המקרה בו מתקבל "שקר" אין צורך לעדכן את המינימום בסיום הפעולה
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
		return; // should be replaced by student code
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
	    if(this.min.key==x.key) {
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
		return 0; // should be replaced by student code
	}


	/**
	 * 
	 * Return the total number of cuts.
	 * 
	 */
	public int totalCuts()
	{
		return 0; // should be replaced by student code
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
