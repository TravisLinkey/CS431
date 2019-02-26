class linkedList<T> {
    Node head;
    int listSize;
    int listCount;

    linkedList(int listSize) {
        this.listSize = listSize;
        this.listCount = 0;
    }

    public void addNew(T data, int[] pagedata) {
        Node newNode = new Node(data,pagedata);

        if(this.listCount < 1)
        {
            head = newNode;
            return;
        }

        Node currentNode = head;

        while (currentNode.getNext() != null) {
            currentNode = currentNode.getNext();
        }

        currentNode.setNext(newNode);
    }
    public Node getNode(int count) {
        Node currentNode = head;
        int i = 0;

        while (i++ < count)
            currentNode = currentNode.getNext();

        return currentNode;
    }
    public void addNode(Node newNode,int index) {
        Node currentNode = this.head;
        Node nodeBefore = currentNode;
        Node nodeAfter;

        // get the node before it, set head ptr to null
        for (int j = 0; j < index; j++) {
            nodeBefore = currentNode;
            currentNode = currentNode.getNext();
        }

        nodeAfter = currentNode.getNext();

        System.out.println("Removing: " + currentNode.getData());
        // remove old current node
        currentNode.setNext(null);

        // set nodeBefore to newnode
        nodeBefore.setNext(newNode);

        // set newNode to nodeAfter
        newNode.setNext(nodeAfter);
    }
    public boolean isFull() {
        if(listCount > listSize)
            return true;
        else
            return false;
    }

    public Node getNodeWithAddress(String address) {
        Node currentNode = head;
        int i = 0;

        while(i++ < listCount)
        {
            if(currentNode.getData().equals(address.substring(0,2)))
            {
                return currentNode;
            }
            currentNode = currentNode.getNext();
        }

        return null;
        }

    public int getNodeIndex(String address) {
        for (int i = 0; i < listCount; i++) {
            if(this.getNode(i).getData().equals(address))
                return i;
        }

            return 0;
    }
}
