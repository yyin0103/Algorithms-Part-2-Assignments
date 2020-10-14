/* *****************************************************************************
 *  Topic:    Seam Carving
 *            Seam-carving is a content-aware image resizing technique where the
 *            image is reduced in size by one pixel of height (or width) at a time.
 *            A vertical seam in an image is a path of pixels connected from the top
 *            to the bottom with one pixel in each row; a horizontal seam is a path
 *            of pixels connected from the left to the right with one pixel in each
 *            column.
 *  @auther:  Ying Chu
 **************************************************************************** */
import edu.princeton.cs.algs4.Picture;
import java.awt.Color;

public class SeamCarver {
    private Picture pic;
    private int[][] picColor;
    private double[][] energy;
    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("given picture is null");
        }
        this.pic = picture;
        this.picColor = new int[width()][height()];
        energy = new double[width()][height()];
        width = picture.width();
        height = picture.height();

        // set the color of each pixel
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                picColor[i][j] = pic.getRGB(i, j);
            }
        }

        // calculate the energy of each pixel
        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                energy[i][j] = countEnergy(i, j);
            }
        }
    }

    // current picture
    public Picture picture () {
        Picture picture = new Picture(width(), height());
        for (int x = 0; x < width(); x++) {
            for (int y = 0; y < height(); y++) {
                picture.setRGB(x, y, picColor[x][y]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width () {
        return this.width;
    }

    // height of current picture
    public int height () {
        return this.height;
    }

    // energy of pixel at column x and row y
    // the higher the energy, the less likely that the pixel will be included as part of a seam
    // implement "dual-gradient energy function"
    public double energy(int x, int y){
        if (x < 0 || x > width() - 1 || y < 0 || y > height() - 1) {
            throw new IllegalArgumentException("x or y out of range");
        }
        return energy[x][y];
    }

    private double countEnergy(int x, int y){

        // count energy of the pixel
        if (x == 0 || x == width() - 1 || y == 0 || y == height() - 1) {
            return 1000; // border
        }

        // calculate x difference
        Color leftC = pic.get(x - 1, y);
        Color rightC = pic.get(x + 1, y);
        double xR = rightC.getRed() - leftC.getRed();
        double xG = rightC.getGreen() - leftC.getGreen();
        double xB = rightC.getBlue() - leftC.getBlue();
        double xDiff = xR * xR + xG * xG + xB * xB;

        // calculate y difference
        Color aboveC = pic.get(x, y - 1);
        Color belowC = pic.get(x, y + 1);
        double yR = belowC.getRed() - aboveC.getRed();
        double yG = belowC.getGreen() - aboveC.getGreen();
        double yB = belowC.getBlue() - aboveC.getBlue();
        double yDiff = yR * yR + yG * yG + yB * yB;

        return Math.sqrt(xDiff + yDiff);
    }



    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] ematrix = new double[height()][width()];
        for (int i = 0; i < height(); i++)
            for (int j = 0; j < width(); j++) {
                ematrix[i][j] = energy(j, i);
            }
        return findSeam(ematrix);
    }

    public int[] findVerticalSeam() {
        double[][] ematrix = new double[width()][height()];
        for (int i = 0; i < width(); i++)
            for (int j = 0; j < height(); j++) {
                ematrix[i][j] = energy(i, j);
            }
        return findSeam(ematrix);
    }

    // sequence of indices for vertical seam
    private int[] findSeam(double[][] energy) {

        double[][] energySum = new double[width][height]; // used to identify minimum energy sum
        int[][] backTracker = new int[width][height]; // layer above
        double minEnergy;

        // loop over the energy matrix and find the lowest energy path
        for (int y = 0; y < h; y++) { // row
            for (int x = 0; x < w; x++) { // column
                if (y == 0) {
                    energySum[x][y] = energy[x][y];
                    backTracker[x][y] = x;
                }
                else {
                    if (x == 0) {
                        minEnergy = Math.min(energySum[x][y - 1], energySum[x + 1][y - 1]);
                        if (minEnergy == energySum[x][y - 1]) {
                            backTracker[x][y] = x;
                        }
                        else {
                            backTracker[x][y] = x + 1;
                        }
                    } else if (x == (w - 1)) {
                        minEnergy = Math.min(energySum[x - 1][y - 1], energySum[x][y - 1]);
                        if (minEnergy == energySum[x - 1][y - 1]) {
                            backTracker[x][y] = x - 1;
                        }
                        else { // x
                            backTracker[x][y] = x;
                        }
                    } else {
                        minEnergy = Math.min(Math.min(energySum[x - 1][y - 1], energySum[x][y - 1]),
                                             energySum[x + 1][y - 1]);
                        if (minEnergy == energySum[x - 1][y - 1]) {
                            backTracker[x][y] = x - 1;
                        }
                        else if (minEnergy == energySum[x][y - 1]) {
                            backTracker[x][y] = x;
                        }
                        else if (minEnergy == energySum[x + 1][y - 1]) {
                            backTracker[x][y] = x + 1;
                        }
                    }
                    energySum[x][y] = energy[x][y] + minEnergy;
                }
            }
        }

        // find the minimum energy sum in last row
        double minSum = energySum[0][h - 1];
        int minIndex = 0;
        for (int x = 1; x < energySum.length; x++) {
            if (minSum > energySum[x][h - 1]) {
                minIndex = x;
                minSum = energySum[x][h - 1];
            }
        }

        // back-track
        int[] seam = new int[height];
        seam[height - 1] = minIndex;
        for (int y = height - 2; y >= 0; y--) {
            seam[y] = backTracker[seam[y + 1]][y + 1];
        }

        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || seam.length != width()) {
            throw new IllegalArgumentException("seam not valid");
        }

        if (pic.height() <= 1) {
            throw new IllegalArgumentException("height of the picture is less than or equal to 1");
        }


        Picture newPic = new Picture(width(), height() - 1);
        Color color;
        int row;
        for (int x = 0; x < width(); x++) {
            if (seam[x] < 0 || seam[x] > height() - 1 || (x > 0 && Math.abs(seam[x] - seam[x - 1]) > 1)) {
                throw new IllegalArgumentException("seam not valid");
            }
            for (int y = 0; y < height(); y++) {
                if (y == seam[x]) continue;
                color = pic.get(x, y);
                row = y;
                if (y > seam[x]) {
                    row--;
                }
                newPic.set(x, row, color);
            }
        }
        pic = newPic;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam ( int[] seam){
        // corner cases
        if (seam == null || seam.length != height()) {
            throw new IllegalArgumentException("seam not valid");
        }

        if (pic.width() <= 1) {
            throw new IllegalArgumentException("width of the picture is less than or equal to 1");
        }

        Picture newPic = new Picture(width() - 1, height());
        Color color;
        int col;
        for (int y = 0; y < height(); y++) {
            if (seam[y] < 0 || seam[y] > width() - 1 || (y > 0 && Math.abs(seam[y] - seam[y - 1]) > 1)) {
                throw new IllegalArgumentException("seam not valid");
            }
            for (int x = 0; x < width(); x++) {
                if (x == seam[y]) continue;
                color = pic.get(x, y);
                col = x;
                if (x > seam[y]) {
                    col--;
                }
                newPic.set(col, y, color);
            }
        }
        pic = newPic;
    }
}