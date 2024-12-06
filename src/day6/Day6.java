package day6;

import utils.FilesUtil;

import java.util.HashMap;
import java.util.HashSet;

public class Day6 {
    char[][] back;
    char[][] grid;
    HashSet<String> obstructions = new HashSet<>();
    HashMap<String, String> positions = new HashMap<>();

    public void smallSol() {
        grid = getInput();
        Point player = locatePlayer();

        while(isInsideGrid(player)) {
            player = nextIteration(player);
        }

        int ans = countMoves();
        System.out.println(ans);
    }

    public void largeSol() {
        grid = getInput();
        back = new char[grid.length][grid[0].length];
        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                back[i][j] = grid[i][j];
            }
        }

        int count = 0;
        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                Point player = locatePlayer();
                obstructions = new HashSet<>();

                if(grid[i][j] == '.') {
                    grid[i][j] = '#';
                    grid[i][j] = '#';

                    while(isInsideGrid(player)) {
                        if(positions.getOrDefault(player.value(), "").contains(""+grid[player.x][player.y])) {
                            count++;
                            break;
                        }

                        String pos = positions.getOrDefault(player.value(), "");
                        positions.put(player.value(), pos + grid[player.x][player.y]);
                        player = nextIteration(player);
                    }
                    reset();
                }
            }
        }

        System.out.println(count);
    }

    int countMoves() {
        int ret = 0;
        for (char[] chars : grid) {
            for (int j = 0; j < grid[0].length; j++) {
                if (chars[j] == 'x') {
                    ret++;
                }
            }
        }
        return ret;
    }

    void reset() {
        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                grid[i][j] = back[i][j];
            }
        }

        positions.clear();
    }

    Point nextIteration(Point current) {
        char dir = grid[current.x][current.y];

        grid[current.x][current.y] = 'x';
        switch (dir) {
            case '^':
                if (!isInsideGrid(current.up())) {
                    return current.up();
                }

                if (isBlocked(current.up())) {
                    grid[current.x][current.y] = '>';
                    return current;
                }

                grid[current.up().x][current.up().y] = '^';
                return current.up();

            case '>':
                if (!isInsideGrid(current.right())) {
                    return current.right();
                }

                if (isBlocked(current.right())) {
                    grid[current.x][current.y] = 'v';
                    return current;
                }

                grid[current.right().x][current.right().y] = '>';
                return current.right();
            case 'v':
                if (!isInsideGrid(current.bottom())) {
                    return current.bottom();
                }

                if (isBlocked(current.bottom())) {
                    grid[current.x][current.y] = '<';
                    return current;
                }

                grid[current.bottom().x][current.bottom().y] = 'v';
                return current.bottom();
            case '<':
                if (!isInsideGrid(current.left())) {
                    return current.left();
                }

                if (isBlocked(current.left())) {
                    grid[current.x][current.y] = '^';
                    return current;
                }

                grid[current.left().x][current.left().y] = '<';
                return current.left();
            default:
                break;
        }
        return current;
    }

    boolean isBlocked(Point player) {
        return grid[player.x][player.y] == '#';
    }

    boolean isInsideGrid(Point player) {
        return player.x >= 0 && player.x < grid.length && player.y >= 0 && player.y < grid[0].length;
    }

    Point locatePlayer() {
        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                if(grid[i][j] == '^') {
                    return new Point(i, j);
                }
            }
        }
        return new Point(0, 0);
    }

    char[][] getInput() {
        String input = FilesUtil.getContentOf("src/day6/input");
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

        Point up() {
            return new Point(x - 1, y);
        }

        Point bottom() {
            return new Point(x + 1, y);
        }

        Point right() {
            return new Point(x, y + 1);
        }

        Point left() {
            return new Point(x, y - 1);
        }

        String value() {
            return x + ";" + y;
        }
    }
}