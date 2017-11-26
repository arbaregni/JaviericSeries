package com.company;

import java.util.Vector;

public class Node {

    public int j, k, u, connections;
    public Node origin;
    public Vector<Node> children;

    Node(int j, int k, int u) {
        this.j = j;
        this.k = k;
        this.u = u;
        connections = 0;
        children = new Vector<>();
        origin = this;
    }






    @Override
    public String toString() {
        return "(" + j + ", " + k + ") : " + u;
    }

    /**
     * this Node is superior to the other if:
     *  1. they have the same type
     *  2. its higher up
     *  3. more to the left
     * @param other the node to compare to
     * @return whether or not this point is superior
     */
    boolean isSuperior(Node other) {
        return other.u == u && other.j > j && other.k > k;
    }

    public void updateOrigins(Node other) {
        connections++;
        other.connections++;
        if (this.isSuperior(other)) {
            this.origin.addAsChild(other.origin);
        } else {
            other.origin.addAsChild(this.origin);
        }
    }

    public boolean isConnectedTo(Node other) {

        return other == this || other == this.origin || other.origin == this || other.origin == this.origin;
    }

    public void addAsChild(Node other) {
        if (other.origin != this) { // prevent concurrent modification
            for (Node child : other.origin.children) {
                child.origin = this;
                children.add(child);
            }
            other.children = new Vector<>();
            children.add(other);
            other.origin = this;
        }
    }
}
