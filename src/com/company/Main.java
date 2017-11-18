package com.company;

import javafx.util.Pair;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Vector;



public class Main {
    static void draw(Vector<Integer[]> lines, Graphics graphics) {
        graphics.setColor(Color.BLACK);
        for (Integer[] data : lines) {
            graphics.drawLine(data[0], data[1], data[2], data[3]);
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

    static Vector<Integer> nextRow(Vector<Integer> previous, int base) {
        Vector<Integer> nextRow = new Vector<>();
        int carry = 1;
        for (int i = previous.size() - 1; i >= 0; i--) {
            Integer u  = previous.get(i) + carry;
            carry = u / base;
            nextRow.insertElementAt(u % base, 0);
        }
        return nextRow;
    }

    public static void main(String[] args) {

	    Scanner scan = new Scanner(System.in);
        System.out.println("What is the order of the javieric series? ");
	    int order = scan.nextInt();
	    int base = 2;
	    int row_height = 10;
	    int col_width = 10;

	    Vector<Integer> last_row = new Vector<>();
	    Vector<Integer> current_row = new Vector<>();
	    for (int i = 0; i < order; i++) {
	        current_row.add(0);
        }

        Vector<Integer[]> draw_steps = new Vector<>(); // vector of arrays {x1, y1, x2, y2}. used to draw a line from (x1, y1) to (x2, y2)
        Vector<Integer> toCheck  = new Vector<>(); // things that aren't connected that may yet be
        Vector<Integer> newChecks;

        for (int j = 0; j < Math.pow(base, order); j++) {
            System.out.println("                     " + last_row);
	        System.out.println("Currently analyzing: " + current_row + " ("+j+")");

	        newChecks = new Vector<>();
	        int beginSeg = 0;
	        int upConnectsInThisSegment = 0; // how many times does this new row connect up to the last? used for calculating diagonals

	        for (int k = 0; k < order; k++) {
                int u = current_row.get(k);

                if (k != 0 && current_row.get(k - 1) != u) { // new segment
                    upConnectsInThisSegment = 0; // if we break the segment, we must reset the counters
                    beginSeg = k;
                }

                // connect across
                if (k + 1 < order && current_row.get(k+1) == u) {
                    // connect to the right
                    draw_steps.add(new Integer[]{k * col_width, j * row_height, (k + 1) * col_width, j * row_height});
                }

                // connect up, either straight or diagonal
                if (k < last_row.size() && last_row.get(k).equals(u)) {
                    // connect straight up
                    draw_steps.add(new Integer[]{k * col_width, j * row_height, k * col_width, (j - 1) * row_height});
                    upConnectsInThisSegment++;

                } else if (k - 1 < last_row.size() && k != 0 && last_row.get(k - 1).equals(u) && (upConnectsInThisSegment == 0 || toCheck.contains(k - 1))) {
                    // connect diagonally to the left
                    draw_steps.add(new Integer[]{k * col_width, j * row_height, (k - 1) * col_width, (j - 1) * row_height});
                    upConnectsInThisSegment++;
                } else if (k + 1 < last_row.size() && last_row.get(k + 1).equals(u) && (upConnectsInThisSegment == 0 || toCheck.contains(k + 1))) {
                    // connect diagonally to the right
                    draw_steps.add(new Integer[]{k * col_width, j * row_height, (k + 1) * col_width, (j - 1) * row_height});
                    upConnectsInThisSegment++;
                }

                if (upConnectsInThisSegment == 0) {
                    newChecks.add(beginSeg);
                    System.out.println(beginSeg);
                }

            }
	        last_row = current_row;
	        current_row = nextRow(current_row, base);

	        toCheck = newChecks;
            System.out.println("                     "+toCheck);
        }

        System.out.println("Analysis complete, proceeding to draw...");

        BufferedImage off_Image =
                new BufferedImage(col_width * order, (int) (row_height * Math.pow(base, order)),
                        BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2 = off_Image.createGraphics();

        draw(draw_steps, g2);

        System.out.println("Saving file...");
        save(off_Image, "javieric_"+order+"_"+base);
    }
}
