public class Main {
    public static void main(String[] args) {
        // Initialize FibonacciHeap
        FibonacciHeap heap = new FibonacciHeap();
        PrintHeap printHelper = new PrintHeap(heap);

        // Test 1: Insertion
        System.out.println("Test 1: Insert Nodes");
        heap.insert(10, "Node1");
        heap.insert(20, "Node2");
        heap.insert(5, "Node3");
        heap.insert(15, "Node4");
        heap.insert(30, "Node5");
        printHelper.printHeap();

        // Test 2: Find Min
        System.out.println("\nTest 2: Find Minimum");
        System.out.println("Minimum: " + heap.findMin().key);

        // Test 3: Delete Min
        System.out.println("\nTest 3: Delete Minimum");
        heap.deleteMin();
        printHelper.printHeap();
        System.out.println("New Minimum: " + heap.findMin().key);

        // Test 4: Decrease Key
        System.out.println("\nTest 4: Decrease Key");
        FibonacciHeap.HeapNode node = heap.insert(25, "Node6"); // Insert new node
        heap.decreaseKey(node, 20); // Decrease key to 5
        printHelper.printHeap();
        System.out.println("New Minimum after DecreaseKey: " + heap.findMin().key);

        // Test 5: Meld Two Heaps
        System.out.println("\nTest 5: Meld Heaps");
        FibonacciHeap anotherHeap = new FibonacciHeap();
        anotherHeap.insert(40, "NodeA");
        anotherHeap.insert(50, "NodeB");
        anotherHeap.insert(2, "NodeC");
        System.out.println("Second Heap Before Meld:");
        PrintHeap printHelper2 = new PrintHeap(anotherHeap);
        printHelper2.printHeap();

        System.out.println("\nMelding Heaps...");
        heap.meld(anotherHeap);
        printHelper.printHeap();
        System.out.println("New Minimum After Meld: " + heap.findMin().key);

        // Test 6: Delete Arbitrary Node
        System.out.println("\nTest 6: Delete Arbitrary Node");
        heap.delete(node);
        printHelper.printHeap();
        System.out.println("New Minimum After Arbitrary Delete: " + heap.findMin().key);

        // Test 7: Total Links and Cuts
        System.out.println("\nTest 7: Total Links and Cuts");
        System.out.println("Total Links: " + heap.totalLinks());
        System.out.println("Total Cuts: " + heap.totalCuts());

        // Test 8: Heap Size and Number of Trees
        System.out.println("\nTest 8: Heap Size and Number of Trees");
        System.out.println("Heap Size: " + heap.size());
        System.out.println("Number of Trees: " + heap.numTrees());
    }
}