class Node<T>{
    boolean valid;
    boolean recent;
    boolean dirty;
    T data;
    int[] page = new int[256];
    private Node next;

    /**
     * Constructor to initialize the node
     * @param data
     */
    Node(T data, int[] pagedata) {
        valid = true;
        recent = true;
        dirty = false;

        this.data = data;
        page = pagedata;
    }

    /**
     * Function to return the data of the node
     * @return
     */
    public T getData()
    {
        return this.data;
    }

    /**
     * Function to get the next node in the list
     * @return
     */
    public Node getNext() {
        return this.next;
    }

    /**
     * Function to set the next node in the list
     * @param next
     */
    public void setNext(Node next) {
        this.next = next;
    }

    /**
     * Function to set the recent bit of the node.
     * @param i
     */
    public void setRBit(boolean i) {
        this.recent = i;
    }
}
