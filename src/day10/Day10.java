package day10;

import utils.FilesUtil;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class Day10 {
    int[][] grid;

    int[] dx = {0, 0, 1, -1};
    int[] dy = {1, -1, 0, 0};

    public void smallSol() {
        grid = getInput();
        int ans = findTrailsScores();
        System.out.println(ans);
    }

    public void largeSol() {
        grid = getInput();
        int ans = findTrailsRatings();
        System.out.println(ans);
    }

    int findTrailsScores() {
        int ans = 0;
        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                if(grid[i][j] == 0) {
                    ans += findScoreOf(i, j);
                }
            }
        }
        return ans;
    }

    int findTrailsRatings() {
        int ans = 0;
        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                if(grid[i][j] == 0) {
                    ans += findRatingOf(i, j);
                }
            }
        }
        return ans;
    }

    int findScoreOf(int i, int j) {
        int score = 0;
        Stack<Point> dfs = new Stack<>();
        Set<String> seen = new HashSet<>();

        dfs.add(new Point(i, j));

        while(!dfs.isEmpty()) {
            Point cur = dfs.pop();
            int val = grid[cur.x][cur.y];

            if(val == 9) {
                score++;
            }
            seen.add(cur.value());

            for(int k=0;k<4;k++) {
                Point next = new Point(cur.x+dx[k], cur.y+dy[k]);
                if(isValid(next.x, next.y) && !seen.contains(next.value())) {
                    if(grid[next.x][next.y] == val + 1) {
                        dfs.add(next);
                    }
                }
            }
        }
        return score;
    }

    int findRatingOf(int i, int j) {
        int score = 0;
        Stack<Point> dfs = new Stack<>();
        Set<String> seen = new HashSet<>();

        dfs.add(new Point(i, j));

        while(!dfs.isEmpty()) {
            Point cur = dfs.pop();
            int val = grid[cur.x][cur.y];

            seen.add(cur.value());
            if(val == 9) {
                score++;
                seen.clear();
                continue;
            }

            for(int k=0;k<4;k++) {
                Point next = new Point(cur.x+dx[k], cur.y+dy[k]);
                if(isValid(next.x, next.y) && !seen.contains(next.value())) {
                    if(grid[next.x][next.y] == val + 1) {
                        dfs.add(next);
                    }
                }
            }
        }
        return score;
    }

    boolean isValid(int i, int j) {
        return i >= 0 && i < grid.length && j >= 0 && j < grid[0].length;
    }

    int[][] getInput() {
        String input = FilesUtil.getContentOf("src/day10/input");
        String[] lines = input.split("\n");
        int[][] grid = new int[lines.length][lines[0].length()];
        for(int i=0;i<lines.length;i++) {
            for(int j=0;j<lines[0].length();j++) {
                grid[i][j] = lines[i].charAt(j) - '0';
            }
        }
        return grid;
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
    }
}
