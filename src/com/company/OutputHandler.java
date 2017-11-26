package com.company;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Vector;

public class OutputHandler {
    private BufferedImage image;
    private Graphics2D graphics;
    private int colWidth, rowHeight;
    private ProgressTracker ticker;

    OutputHandler(int columns, int rows, int colWidth, int rowHeight) {
        this.colWidth = colWidth;
        this.rowHeight = rowHeight;

        image = new BufferedImage(columns * colWidth, rows * rowHeight, BufferedImage.TYPE_INT_ARGB);
        graphics = image.createGraphics();
    }

    public void drawLines(Vector<Integer[]> lines) {
        ticker = new ProgressTracker(lines.size(), "Drawing lines: ");
        graphics.setColor(Color.BLACK);
        for (Integer[] data : lines) {
            graphics.drawLine(data[0] * colWidth, data[1] * rowHeight, data[2] * colWidth, data[3] * rowHeight);
        }
    }

    public void saveAs(String filename, String formatType) {
        try {
            File outputfile = new File(filename+"."+formatType);
            if (!outputfile.canRead()) {
                outputfile.setReadable(true);
            }
            if (!outputfile.canWrite()) {
                outputfile.setWritable(true);
            }
            System.out.println("Saving as " + filename + "." + formatType);
            ImageIO.write(image, formatType, outputfile);
        } catch (IOException ioe) {
            ioe.printStackTrace();
        }
    }

}
