package day8;

import day6.Day6;
import utils.FilesUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Day8 {
    char[][] grid;
    char[][] antiGrid;
    HashMap<Character, List<Point>> positions = new HashMap<>();

    public void smallSol() {
        grid = getInput();

        calculatePositions();
        calculateAntiNodes();

        int ans = 0;
        for (char[] chars : antiGrid) {
            for (int j = 0; j < antiGrid[0].length; j++) {
                if (chars[j] == '#') {
                    ans++;
                }
            }
        }
        System.out.println(ans);
    }

    public void largeSol() {
        grid = getInput();

        calculatePositions();
        calculateAntiNodesWithResonantHarmonics();

        int ans = 0;
        for (char[] chars : antiGrid) {
            for (int j = 0; j < antiGrid[0].length; j++) {
                if (chars[j] == '#') {
                    ans++;
                }
            }
        }
        System.out.println(ans);
    }

    void calculatePositions() {
        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                if(grid[i][j] != '.') {
                    List<Point> list = positions.getOrDefault(grid[i][j], new ArrayList<>());
                    list.add(new Point(i, j));
                    positions.put(grid[i][j], list);
                }
            }
        }
    }

    void calculateAntiNodes() {
        antiGrid = new char[grid.length][grid[0].length];

        for(int i=0;i<antiGrid.length;i++) {
            for(int j=0;j<antiGrid[0].length;j++) {
                antiGrid[i][j] = '.';
            }
        }

        for(List<Point> list : positions.values()) {
            for(int i=0;i< list.size();i++) {
                Point p = list.get(i);
                for(int j = 0;j<list.size();j++) {
                    if(i == j) continue;
                    Point anti = p.antiOf(list.get(j));

                    if(isValid(anti)) {
                        antiGrid[anti.x][anti.y] = '#';
                    }
                }
            }
        }
    }

    void calculateAntiNodesWithResonantHarmonics () {
        antiGrid = new char[grid.length][grid[0].length];

        for(int i=0;i<antiGrid.length;i++) {
            for(int j=0;j<antiGrid[0].length;j++) {
                if(grid[i][j] != '.'){
                    antiGrid[i][j] = '#';
                } else {
                    antiGrid[i][j] = '.';
                }
            }
        }

        for(List<Point> list : positions.values()) {
            for(int i=0;i< list.size();i++) {
                Point p = list.get(i);
                for(int j = 0;j<list.size();j++) {
                    if(i == j) continue;
                    Point anti = p.antiOf(list.get(j));
                    Point prev = p;

                    while(isValid(anti)) {
                        antiGrid[anti.x][anti.y] = '#';
                        Point temp = anti;

                        anti = anti.antiOf(prev);
                        prev = temp;
                    }
                }
            }
        }
    }

    boolean isValid(Point p) {
        return p.x >= 0 && p.x < grid.length && p.y >=0 && p.y < grid[0].length;
    }

    char[][] getInput() {
        String input = FilesUtil.getContentOf("src/day8/input");
        String[] lines = input.split("\n");
        char[][] chars = new char[lines.length][lines[0].length()];
        for(int i=0;i<lines.length;i++) {
            for(int j=0;j<lines[0].length();j++) {
                chars[i][j] = lines[i].charAt(j);
            }
        }
        return chars;
    }

    static class Point {
        int x;
        int y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        String value() {
            return x + ";" + y;
        }

        Point antiOf(Point p) {
            return switch (getQuad(p)) {
                case 0 -> new Point(x + Math.abs(x - p.x), y - Math.abs(y - p.y));
                case 1 -> new Point(x - Math.abs(x - p.x), y - Math.abs(y - p.y));
                case 2 -> new Point(x - Math.abs(x - p.x), y + Math.abs(y - p.y));
                case 3 -> new Point(x + Math.abs(x - p.x), y + Math.abs(y - p.y));
                default -> p;
            };
        }

        int getQuad(Point p) {
            if (p.x <= x && p.y >= y) {
                return 0; // top right
            }

            if (p.x >= x && p.y >= y) {
                return 1; // bottom right
            }

            if (p.x >= x && p.y <= y) {
                return 2; // bottom left
            }

            return 3; // top left
        }
    }
}
