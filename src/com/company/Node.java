package com.company;

import java.util.HashMap;
import java.util.Vector;

public class Node {
    public static Vector<Vector<Node>> rows;
    public static HashMap<Node, Node> origins;
    public static Vector<Node> rootNodes;

    public int j, k, u, connections;

    Node(int j, int k, int u) {
        this.j = j;
        this.k = k;
        this.u = u;
        connections = 0;
    }

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
        Node old, updated;
        if (this.isSuperior(other)) {
            old = origins.get(other);
            updated = origins.get(this);
        } else {
            old = origins.get(this);
            updated = origins.get(other);
        }
        for (Node key : origins.keySet()) {
            if (origins.get(key) == old) {
                origins.replace(key, updated);
            }
        }
    }

    public boolean isConnectedTo(Node other) {
        if (origins.containsKey(other)) {
            if (origins.containsKey(this)) {
                // both are not root
                return origins.get(other) == origins.get(this);
            } else {
                // other is not root, but we are
                return origins.get(this) == other;
            }
        } else {
            if (origins.containsKey(this)) {
                // we are root, but other is not
                return origins.get(other) == this;
            } else {
                // neither are root
                return other == this;
            }
        }
    }
}
