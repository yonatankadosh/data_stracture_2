import java.util.concurrent.atomic.AtomicBoolean;

public class Main {
    public static void main(String[] args) {
        // Initialize FibonacciHeap
        FibonacciHeap heap = new FibonacciHeap();
        PrintHeap printHelper = new PrintHeap(heap);

        // Start a thread to monitor and stop the program if it runs for too long
        AtomicBoolean stopProgram = new AtomicBoolean(false);
        Thread monitor = createMonitorThread(stopProgram, 3_000); // Timeout: 3 seconds
        monitor.start();

        try {
            // Test 1: Insert Nodes
            System.out.println("Test 1: Insert Nodes");

            FibonacciHeap.HeapNode to_decrease_10 = null;
            FibonacciHeap.HeapNode to_delete_12 = null;
            for (int i = 0; i < 17; i++) {
                if (i==11){
                    to_decrease_10 = heap.insert(i, "Node");
                }
                else if (i==12){
                    to_delete_12 = heap.insert(i, "Node");
                }
                else {
                    heap.insert(i, "Node");
                }
            }
            printHelper.printHeap();
            heap.deleteMin();
            printHelper.printHeap();
            System.out.println("\nTest 9: Total Links and Cuts");
            System.out.println("Total Links: " + heap.totalLinks());
            System.out.println("Total Cuts: " + heap.totalCuts());

            heap.delete(to_delete_12);
            printHelper.printHeap();
            System.out.println("\nTest 9: Total Links and Cuts");
            System.out.println("Total Links: " + heap.totalLinks());
            System.out.println("Total Cuts: " + heap.totalCuts());

/*            // Test 6: Insert and Consolidate
            System.out.println("\nTest 6: Insert and Consolidate");
            heap.deleteMin();
            printHelper.printHeap();
            System.out.println("num trees:"+ heap.HeapNumTrees);
            System.out.println("min:"+ heap.min.key);
            System.out.println("num nodes:"+ heap.HeapSize);
            System.out.println("min rank:"+ heap.min.rank);
            FibonacciHeap.HeapNode child = heap.min.child; 
            FibonacciHeap.HeapNode curr = child;
            do {
            	System.out.println("min child:"+ curr.key);
            	System.out.println("min child rank:"+ curr.rank);
            	curr = curr.next;
    		} while (curr != child);

            // Test 7: Delete Min with Children
            System.out.println("\nTest 7: Delete Min with Children");
            heap.deleteMin();
            printHelper.printHeap();
            System.out.println("min:"+ heap.min.key);
            System.out.println("Heap Size: " + heap.size());
            System.out.println("Number of Trees: " + heap.numTrees());
            
            FibonacciHeap.HeapNode firstroot = heap.first;
            FibonacciHeap.HeapNode curr2 = heap.first;
            do {
            	System.out.println("root:"+ curr2.key);
            	curr2 = curr2.next;
    		} while (curr2 != firstroot);
            
            System.out.println("last ="+ heap.first.prev.key);

           // Test 8: Meld Heaps
            System.out.println("\nTest 8: Meld Heaps");
            FibonacciHeap anotherHeap = new FibonacciHeap();
            anotherHeap.insert(50, "NodeX");
            anotherHeap.insert(60, "NodeY");
            System.out.println("Second Heap Before Meld:");
            PrintHeap printHelper2 = new PrintHeap(anotherHeap);
            
            System.out.println("\nMelding Heaps...");
            System.out.println("first");
            heap.meld(anotherHeap);
            
            printHelper.printHeap();
            System.out.println("New Minimum After Meld: " + heap.findMin().key);

            // Test 9: Total Links and Cuts
            System.out.println("\nTest 9: Total Links and Cuts");
            System.out.println("Total Links: " + heap.totalLinks());
            System.out.println("Total Cuts: " + heap.totalCuts());

            // Test 10: Heap Size and Number of Trees
            System.out.println("\nTest 10: Heap Size and Number of Trees");
            System.out.println("Heap Size: " + heap.size());
            System.out.println("Number of Trees: " + heap.numTrees());
*/      } catch (Exception e) {
            System.err.println("An exception occurred: " + e.getMessage());
            e.printStackTrace();
        } finally {
            // Stop the monitor thread
            stopProgram.set(true);
        }
    }

    /**
     * Creates a monitor thread to stop the program after a timeout.
     * 
     * @param stopFlag AtomicBoolean to signal program termination.
     * @param timeout  Timeout in milliseconds.
     * @return The monitor thread.
     */
    private static Thread createMonitorThread(AtomicBoolean stopFlag, long timeout) {
        return new Thread(() -> {
            try {
                Thread.sleep(timeout);
                if (!stopFlag.get()) {
                    System.err.println("Program timeout reached. Exiting.");
                    System.exit(1);
                }
            } catch (InterruptedException e) {
                // Thread interrupted; no action needed
            }
        });
    }
}