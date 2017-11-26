package com.company;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

	    Scanner scan = new Scanner(System.in);
        System.out.println("What is the order of the javieric series? ");
	    final int order = scan.nextInt();
	    System.out.println("What is the base?");
	    final int base = scan.nextInt();
	    final int size = (int) Math.pow(base, order);

	    NodeGrid grid = new NodeGrid(order, base, size);
        OutputHandler handler = new OutputHandler(order, size, 10, 10);
        handler.drawLines(grid.getDrawSteps());
        handler.saveAs("javieric_"+order+"_"+base, "png");

    }
}
