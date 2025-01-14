public class PrintHeap {
    private FibonacciHeap heap;

    public PrintHeap(FibonacciHeap heap) {
        this.heap = heap;
    }

    public void printHeap() {
        if (heap.min == null) {
            System.out.println("The heap is empty.");
            return;
        }
        System.out.println("Fibonacci Heap:");

        FibonacciHeap.HeapNode start = heap.first;
        FibonacciHeap.HeapNode current = heap.first;
        int treeNumber = 1;

        do {
            System.out.println("Tree " + treeNumber + ":");
            printTree(current, "", true);
            current = current.next;
            treeNumber++;
        } while (current != start);
    }

    private void printTree(FibonacciHeap.HeapNode node, String prefix, boolean isLast) {
        if (node == null) return;

        // Print the current node as (key, "value")
        System.out.print(prefix);
        System.out.print(isLast ? "└── " : "├── ");
        System.out.println("(" + node.key + ", \"" + node.info + "\")");

        // Prepare prefix for the next level
        prefix += isLast ? "    " : "│   ";

        // Recursively print children
        if (node.child != null) {
            FibonacciHeap.HeapNode child = node.child;
            do {
                printTree(child, prefix, child.next == node.child);
                child = child.next;
            } while (child != node.child);
        }
    }
}
