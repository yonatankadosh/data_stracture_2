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
	private void deleteMin(boolean doesOGmin) {
		
		boolean size_decreased = false;

		// מחיקה מערימה ריקה - מקרה קצה
		if (this.isEmpty())
			return; // nothing to do

		// Disconnect the min's children from their parent (their parent should be null now) + add to totalCuts
		set_childrens_parentField_null(this.min);

		// Create a new heap with the min's chidren
		FibonacciHeap new_heap = new FibonacciHeap();
		new_heap.min = this.min.child;
		new_heap.HeapNumTrees = this.min.rank;
		if (HeapNumTrees == 1){
			new_heap.HeapSize = this.HeapSize - 1;
			size_decreased = true;
		}
		else
			new_heap.HeapSize = 0; // doesn't really matter for meld_call

		//Edge Case - num of trees is 1
		if (this.HeapNumTrees == 1)
			this.min = null;
		else
			// skip the min pointer in the root's list
			skip_node_in_root_list(this, this.min, true);
		
		// meld heap with new heap
		this.meldCall(new_heap, false);
		

		if (doesOGmin){
			this.consolidate();
			this.updateMin();
		}
		if (!size_decreased)
			this.HeapSize--;


	}
	
	// פונקציות עזר למחיקה

	/*
	 * given a parent node, the function sets the children node's "parent" field to none
	 */
	public void set_childrens_parentField_null(HeapNode parent){
		int parent_rank = parent.rank;
		HeapNode curr_child = parent.child;
		for(int i = 0; i < parent_rank; i++){
			curr_child.parent = null;
			curr_child = curr_child.next;
		}
		
		this.HeapTotalCuts += parent_rank;
	}

	/**
	 * מחיקת שורש מרשימת השורשים
	 */
	public void skip_node_in_root_list(FibonacciHeap heap, HeapNode node, boolean handleMinArbitrary){
		HeapNode old_min = heap.findMin();
		boolean isMin = false;
		if (old_min == node){
			isMin = true;
		}

		HeapNode node_next = node.next;
		HeapNode node_prev = node.prev;
		node_prev.next = node_next;
		node_next.prev = node_prev;
		heap.HeapNumTrees--;

		if (handleMinArbitrary && isMin)
		{
			heap.min = node_next;
			heap.updateMin();
		}
	}



	/**
	 * חיבור שתי רשימות שורשים
	 */
	private void mergeRootLists(HeapNode node1, HeapNode node2) {
		if (node1 == null || node2 == null) {
			return; // One of the lists is empty
		}

		HeapNode temp = node1.next;
		node1.next = node2.next;
		node2.next.prev = node1;
		node2.next = temp;
		temp.prev = node2;

		// Update min if necessary
		if (node2.key < min.key) {
			min = node2;
		}
	}

	/**
	 * מחיקת צומת ע״י שינוי מצביעים
	 */
	private void removeFromRootList(HeapNode node) {
		if (node == node.next) {
			// Single node case
			first = null;
			min = null;
		} else {
			node.prev.next = node.next;
			node.next.prev = node.prev;
			if (first == node) {
				first = node.next; // Update first if necessary
			}
			if (min == node) {
				updateMin(); // Update min explicitly
			}
		}

		// Reset pointers to prevent accidental reuse
		node.next = node;
		node.prev = node;
		HeapNumTrees--;
	}

	/**
	 * חיבור עצים מדרגות שוות ע״פ המפתח
	 */
	private void linkNodes(HeapNode ndoe1, HeapNode node2) {
		HeapNode x = ndoe1;
		HeapNode y = node2;
		// want to create two nodes such that x.key <= y.key. if not true, swap them.
		// if not - switch them
		if (node2.key < ndoe1.key){
			x = node2;
			y = ndoe1;
		}

		// remove y from the root list
		HeapNode Orig_next_y = y.next;
		HeapNode orig_prev_y = y.prev;
		orig_prev_y.next = Orig_next_y;
		Orig_next_y.prev = orig_prev_y;

		// Edge case - their ranks are zero
		if (x.rank == 0){
			y.next = y;
			y.prev = y;
		}

		else{ // their ranks >= 1
			y.next = x.child;
			y.prev = x.child.prev;
			x.child.prev.next = y;
			x.child.prev = y;
		}

		x.child = y;
		y.parent = x;
		x.rank++;

		// update the number of links for the heap
		this.HeapTotalLinks++;

		// decrease number of trees in the heap
		this.HeapNumTrees--;
	}

	/**
	 * Consolidates the root list to ensure unique degrees for all trees.
	 */
	private void consolidate() {
		if (this.min == null)
			return;
		
		int n = this.HeapSize;
		double log_value;
		if (this.HeapSize <= 0){
		log_value = 1;
		}
		else {
		log_value = Math.log(n + 1) / Math.log(2);
		}
		HeapNode[] bucket_arr = new HeapNode[(int) Math.ceil(log_value)];
		HeapNode curr_root = this.min;
		int times_to_iterate = this.HeapNumTrees;

		for (int i = 0; i < times_to_iterate; i++){
			HeapNode next_root = curr_root.next;
			HeapNode curr_inner = curr_root;  // keep a pointer to the next root (for cases when the pointers shift)

			while (bucket_arr[curr_inner.rank] != null){
				HeapNode other = bucket_arr[curr_inner.rank];
				bucket_arr[curr_inner.rank] = null; // empty the bucket
				this.linkNodes(curr_inner, other);
				
				if (curr_inner.parent != null) // always get the root of the linked tree
					curr_inner = curr_inner.parent; 
			}
			bucket_arr[curr_inner.rank] = curr_inner;
			if (curr_inner.key < this.min.key)
				this.min = curr_inner;
			curr_root = next_root;
		}
	}
	

	/**
	 * עדכון המצביע למינימום
	 */
	private void updateMin() {
		
		if (first == null) {
			this.min = null; // No nodes in the heap, set min to null
			return;
		}

		HeapNode current = first;
		do {
			if (this.min == null || current.key < this.min.key) {
				this.min = current;
			}
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
		meldCall(heap2, true);
	}

	public void meldCall(FibonacciHeap heap2, Boolean sumLinksCuts){
		if(this.isEmpty() && heap2.isEmpty()){
			return;
		}

		if(heap2.isEmpty()){
			return;
		}

		if(this.isEmpty()){
			this.min = heap2.min;
			this.HeapTotalCuts = heap2.HeapTotalCuts;
			this.HeapTotalLinks = heap2.HeapTotalLinks;
			this.HeapSize = heap2.HeapSize;
			this.HeapNumTrees = heap2.HeapNumTrees;
			return;
		}

		HeapNode first1 = this.min; //pointer to first
		HeapNode first2 = heap2.min;
		HeapNode last1 = this.min.prev;
		HeapNode last2 = heap2.min.prev;

		//connect roots
		last1.next = first2;
		first2.prev = last1;

		last2.next = first1;
		first1.prev = last2;

		//set new minimum
		if(this.min.key >= heap2.min.key){
			this.min = heap2.min;
		}
		
		this.HeapNumTrees = this.HeapNumTrees + heap2.HeapNumTrees;

		//meld all other fields - if outer heap
		if(sumLinksCuts){
			this.HeapTotalCuts = this.HeapTotalCuts + heap2.HeapTotalCuts;
			this.HeapTotalLinks = this.HeapTotalLinks + heap2.HeapTotalLinks;
			this.HeapSize = this.HeapSize + heap2.HeapSize;
		}
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
		return this.HeapNumTrees;
	}

	public boolean isEmpty()
	{
		return this.min == null;
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
