package day14;

import utils.FilesUtil;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class Day14 {
    public void smallSol() {
        List<Robot> robots = getInput();

        for(Robot r : robots) {
            for(int i=0;i<100;i++) {
                r.advance();
            }
        }

        List<Integer> quads = robots.stream().map(Robot::getQuad).filter(item -> item != -1).toList();

        int[] quadCounts = new int[4];
        for(int quad : quads) {
            quadCounts[quad]++;
        }

        long ans = 1;
        for(int i=0;i<4;i++) {
            ans *= quadCounts[i];
        }

        System.out.println(ans);
    }

    public void largeSol() {
        List<Robot> robots = getInput();

        for (int i = 0; i < 10000; i++) {
            for(Robot r : robots) {
                r.advance();
            }
            printMap(robots, i+1);
        }
    }

    void printMap(List<Robot> robots, int iter) {
        BufferedImage bfImage = new BufferedImage(102, 104, BufferedImage.TYPE_INT_RGB);
        for(int j=0;j<104;j++) {
            for(int i=0;i<102;i++) {
                int finalI = i;
                int finalJ = j;

                long count = (int) robots.stream()
                        .filter(robot -> robot.pos.x == finalI && robot.pos.y == finalJ)
                        .count();

                if(count > 0) {
                    Color myRGB = new Color(255, 255, 255);
                    int rgb = myRGB.getRGB();
                    bfImage.setRGB(i, j, rgb);
                }
            }
        }
        FilesUtil.writeImage("src/day14/"+iter+".png", bfImage);
    }

    List<Robot> getInput() {
        String input = FilesUtil.getContentOf("src/day14/input");

        return Arrays.stream(input.split("\n"))
                .map(Robot::new)
                .toList();
    }

    static class Robot {
        Point pos;
        Point vel;

        int width = 101;
        int height = 103;

        Robot(String in) {
            String[] parts = in.split(" ");
            pos = new Point(parts[0]);
            vel = new Point(parts[1]);
        }

        void advance() {
            int newX = (pos.x + width + vel.x) % width;
            int newY = (pos.y + height + vel.y) % height;
            pos = new Point(newX, newY);
        }

        int getQuad() {
            if(pos.x == width / 2 || pos.y == height/2) {
                return -1;
            }

            if(pos.x < width/2) {
                if(pos.y < height/2) {
                    return 0;
                } else {
                    return 1;
                }
            } else {
                if(pos.y < height/2) {
                    return 2;
                } else {
                    return 3;
                }
            }
        }
    }

    static class Point {
        int x;
        int y;

        Point(String in) {
            String[] parts = in.split("[vp]=|,");
            x = Integer.parseInt(parts[1]);
            y = Integer.parseInt(parts[2]);
        }

        public Point(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
}
