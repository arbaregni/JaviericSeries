package com.company;

import java.util.Vector;

public class NodeGrid {
    private int order, base, size;
    private Vector<Vector<Node>> rows;
    private Vector<Integer[]> drawSteps;
    private ProgressTracker ticker;

    NodeGrid(int order, int base, int size) {
        this.order = order;
        this.base = base;
        this.size = size;
        drawSteps = new Vector<>();
        rows = new Vector<>();

        ticker = new ProgressTracker(size, "Constructing grid: ");
        constructNodeGrid();

        ticker = new ProgressTracker(2 * size * order, "Constructing connections: ");
        constructOrthagonals();
        constructDiagonals();
    }

    static Vector<Node> nextRow(Vector<Node> previous, int base, int j) {
        Vector<Node> nextRow = new Vector<>();
        int carry = 1;
        for (int i = previous.size() - 1; i >= 0; i--) {
            Integer u  = previous.get(i).u + carry;
            carry = u / base;
            nextRow.insertElementAt(new Node(j, i,u % base), 0);
        }
        return nextRow;
    }

    private Vector<Vector<Node>> constructNodeGrid() {
        Vector<Node> current_row = new Vector<>();
        for (int i = 0; i < order; i++) {
            current_row.add(new Node(0, i,0));
        }
        rows.add(current_row);
        for (int i = 1; i < size; i++) {
            ticker.tick();
            rows.add(nextRow(rows.lastElement(), base, i));
        }
        return rows;
    }

    private void constructOrthagonals() {
        for (int j = 0; j < size; j++) {
            Vector<Node> line = rows.get(j);
            for (int k = 0; k < order; k++) {
                ticker.tick();

                Node node = line.get(k);
                int u = node.u;

                if (k != 0 && line.get(k - 1).u == u) {
                    drawSteps.add(new Integer[]{k, j, k - 1, j});
                    node.updateOrigins(line.get(k - 1));
                }
                if (j != 0 && rows.get(j - 1).get(k).u == u) {
                    drawSteps.add(new Integer[]{k, j, k, j - 1});
                    node.updateOrigins(rows.get(j - 1).get(k));
                }
            }
        }
    }

    private void constructDiagonals() {
        for (int j = 0; j < size; j++) {
            for (int k = 0; k < order; k++) {
                ticker.tick();

                Node node = rows.get(j).get(k);
                if (k != 0 && j != 0) {
                    Node upperLeft = rows.get(j - 1).get(k - 1);
                    if (node.u == upperLeft.u && !node.isConnectedTo(upperLeft)) {
                        drawSteps.add(new Integer[]{k, j, k - 1, j - 1});
                        node.updateOrigins(upperLeft);
                    }
                }
                if (j != 0 && k + 1 < rows.get(j).size()) {
                    Node upperRight = rows.get(j - 1).get(k + 1);
                    if (node.u == upperRight.u && !node.isConnectedTo(upperRight)) {
                        drawSteps.add(new Integer[]{k, j, k + 1, j - 1});
                        node.updateOrigins(upperRight);
                    }
                }
                if (node.connections == 0) {
                    drawSteps.add(new Integer[]{k, j, k, j});
                }
            }
        }
    }

    public Vector<Integer[]> getDrawSteps() {
        return drawSteps;
    }

}
