package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Vector;



public class Main {
    static ProgressTracker ticker;

    static void drawLines(Vector<Integer[]> lines, Graphics graphics, int colWidth, int rowHeight) {
        graphics.setColor(Color.BLACK);
        for (Integer[] data : lines) {
            graphics.drawLine(data[0] * colWidth, data[1] * rowHeight, data[2] * colWidth, data[3] * rowHeight);
        }
    }

    static void save(BufferedImage bufferedImage, String filename) {
        try {
            File outputfile = new File(filename+".png");
            if (!outputfile.canRead()) {
                outputfile.setReadable(true);
            }
            if (!outputfile.canWrite()) {
                outputfile.setWritable(true);
            }

            ImageIO.write(bufferedImage, "png", outputfile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
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

    static Vector<Vector<Node>> constructNumerical(int order, int base) {
        Vector<Vector<Node>> rows = new Vector<>();
        Vector<Node> current_row = new Vector<>();
        for (int i = 0; i < order; i++) {
            current_row.add(new Node(0, i,0));
        }
        rows.add(current_row);
        for (int i = 1; i < Math.pow(base, order); i++) {
            rows.add(nextRow(rows.lastElement(), base, i));
        }
        return rows;
    }

    static Vector<Integer[]> constructDrawSteps(Vector<Vector<Node>> rows) {
        HashMap<Node, Node> origins = new HashMap<>(); // map each node to its root node
        Vector<Node> rootNodes = new Vector<>(); // keep track of each root node
        Vector<Integer[]> drawSteps = new Vector<>(); // vector of arrays {x1, y1, x2, y2}. used to draw a line from (x1, y1) to (x2, y2)

        Node.rows = rows;
        Node.origins = origins;
        Node.rootNodes = rootNodes;


        // establish non-diagonals:
        for (int j = 0; j < rows.size(); j++) {
            Vector<Node> line = rows.get(j);
            for (int k = 0; k < line.size(); k++) {
                ticker.tick();

                Node node = line.get(k);
                int u = node.u;
                origins.put(node, node); // place holder value. It will survive if it is superior to all its connections


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


        // do the diagonals
        for (int j = 0; j < rows.size(); j++) {
            for (int k = 0; k < rows.get(j).size(); k++) {
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

        return drawSteps;
    }

    public static void main(String[] args) {

	    Scanner scan = new Scanner(System.in);
        System.out.println("What is the order of the javieric series? ");
	    int order = scan.nextInt();
	    System.out.println("What is the base?");
	    int base = scan.nextInt();
	    int row_height = 10;
	    int col_width = 10;
	    int pic_width = col_width * order;
	    int pic_height = row_height * (int) Math.pow(base, order);

	    Vector<Vector<Node>> rows = constructNumerical(order, base);

	    ticker = new ProgressTracker(2 * (int) Math.pow(base, order) * order, "Analyzing file: ");

        Vector<Integer[]> draw_steps = constructDrawSteps(rows);

        System.out.println("Analysis complete, proceeding to draw...");

        BufferedImage off_Image =
                new BufferedImage(pic_width, pic_height,
                        BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = off_Image.createGraphics();

        drawLines(draw_steps, g2, col_width, row_height);

        String filename = "javieric_"+order+"_"+base+"_"+pic_width+"x"+pic_height;
        System.out.println("Saving file as "+filename);

        save(off_Image, filename);

        System.out.println("all done");
    }
}
