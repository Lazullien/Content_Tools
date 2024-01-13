import java.awt.image.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;

public class HitboxTextureToHitbox {

    public static void main(String[] args) {
        int i = 0;
        for (var frame : getFrames(32, 32, 0,
                getImage("C:\\Users\\jitji\\Desktop\\assets\\AteloDev\\testForRectangleSearch.png"))) {
            System.out.println("frame " + i + ":");
            i++;
            for (var rect : getRectangles(imageToMap(frame))) {
                System.out.println("x:" + rect.x + ", " + "y:" + rect.y + ", " + "width:" + rect.width + ", "
                        + "height:" + rect.height);
            }
        }
    }

    public static BufferedImage getImage(String file) {
        try {
            return ImageIO.read(new File(file));
        } catch (IOException e) {
            System.out.println("FILE NOT FOUND");
            return null;
        }
    }

    public static ArrayList<BufferedImage> getFrames(int width, int height, int animationID, BufferedImage image) {
        // splits bufferedimage
        ArrayList<BufferedImage> arr = new ArrayList<>();
        for (int i = 0; i < image.getWidth() / width; i++)
            arr.add(image.getSubimage(i * width, animationID * height, width, height));
        return arr;
    }

    public static int[][] imageToMap(BufferedImage image) {
        int width = image.getWidth(), height = image.getHeight();

        int[][] arr = new int[width][height];

        for (int i = 0; i < width; i++)
            for (int j = 0; j < height; j++)
                arr[i][j] = image.getRGB(i, j);

        return arr;
    }

    public static ArrayList<Rectangle> getRectangles(int[][] map) {
        // search the top and left for pixels with color, if there is none we start a
        // since we're searching each column by column, we can get difference in y
        // (height) of a rect directly
        // we could also start another for instance going horizontally searching for the
        // width
        ArrayList<Rectangle> rects = new ArrayList<>();

        for (int i = 0; i < map.length; i++) {
            boolean inRectangleYSearch = false;
            int currentRectangleStartY = -1;
            int currentRectangleWidth = -1;
            for (int j = 0; j < map[i].length; j++) {
                if (map[i][j] != 0) {
                    if ((i == 0 || map[i - 1][j] == 0) && (j == 0 || map[i][j - 1] == 0)) {
                        // count this as a starting point
                        currentRectangleStartY = j;
                        inRectangleYSearch = true;
                        int startX = i;
                        // start an x axis search
                        for (int k = i; k < map.length; k++)
                            if ((k == map.length || map[k + 1][j] == 0)) {
                                currentRectangleWidth = k - startX;
                                break;
                            }
                    }
                    if (inRectangleYSearch && (j == map[i].length || map[i][j + 1] == 0)) {
                        int height = j - currentRectangleStartY;
                        // save rectangle here
                        rects.add(new Rectangle(i, currentRectangleStartY, currentRectangleWidth, height));
                        currentRectangleStartY = -1;
                        currentRectangleWidth = -1;
                        inRectangleYSearch = false;
                    }
                }
            }
        }

        return rects;
    }

    public static class Rectangle {
        float x, y, width, height;

        public Rectangle(float x, float y, float width, float height) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
        }
    }

}