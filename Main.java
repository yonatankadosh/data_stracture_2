public class Main {
    public static void main(String[] args) {
        // יצירת אובייקט של FibonacciHeap
        FibonacciHeap heap = new FibonacciHeap();
        
        // הוספת ערכים לערימה
        heap.insert(10, "Node1");
        heap.insert(20, "Node2");
        heap.insert(5, "Node3");
        
        // יצירת אובייקט של PrintHeap
        PrintHeap printHelper = new PrintHeap(heap);
        
        // הדפסת הערימה
        printHelper.printHeap();
    }
}
