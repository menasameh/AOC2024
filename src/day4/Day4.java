package day4;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;

public class Day4 {
    // Horizontal, Vertical, diagonal clockwise diff in x, y
    // For small
    int[] dx1 = {1, -1  ,0, 0   ,1, 1, -1, -1};
    int[] dy1 = {0, 0   ,1, -1  ,-1, 1, 1, -1};

    int[] dx2 = {2, -2  ,0, 0   ,2, 2, -2, -2};
    int[] dy2 = {0, 0   ,2, -2  ,-2, 2, 2, -2};

    int[] dx3 = {3, -3  ,0, 0   ,3, 3, -3, -3};
    int[] dy3 = {0, 0   ,3, -3  ,-3, 3, 3, -3};

    // For Large, top-left, top-right, bot-right, bot-left
    int[] dx = {-1, 1, 1, -1};
    int[] dy = {-1, -1, 1, 1};

    char[][] grid;

    public void smallSol() {
        List<String> list = getInput();
        int height = list.size();
        int width = list.getFirst().length();

        grid = new char[height][width];

        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                grid[i][j] = list.get(i).charAt(j);
            }
        }

        int ans = 0;

        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                ans += adjWords(i, j);
            }
        }

        System.out.println(ans);
    }

    int adjWords(int i, int j) {
        if(grid[i][j] != 'X') {
            return 0;
        }

        char ch1;
        char ch2;
        char ch3;

        int count = 0;

        for(int x=0;x<8;x++) {
            if(checkIndex(i + dx1[x], j + dy1[x])) {
                ch1 = grid[i + dx1[x]][j + dy1[x]];
            } else {
                continue;
            }

            if(checkIndex(i + dx2[x], j + dy2[x])) {
                ch2 = grid[i + dx2[x]][j + dy2[x]];
            } else {
                continue;
            }

            if(checkIndex(i + dx3[x], j + dy3[x])) {
                ch3 = grid[i + dx3[x]][j + dy3[x]];
            } else {
                continue;
            }

            if(ch1 == 'M' && ch2 == 'A' && ch3 == 'S') {
                count++;
            }

        }
        return count;
    }

    public void largeSol() {
        List<String> list = getInput();
        int height = list.size();
        int width = list.getFirst().length();

        grid = new char[height][width];

        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                grid[i][j] = list.get(i).charAt(j);
            }
        }

        int ans = 0;

        for(int i=0;i<height;i++) {
            for(int j=0;j<width;j++) {
                ans += xMasWords(i, j);
            }
        }

        System.out.println(ans);
    }

    int xMasWords(int i, int j) {
        if(grid[i][j] != 'A') {
            return 0;
        }

        char ch1;
        char ch2;
        char ch3;
        char ch4;

        if(checkIndex(i + dx[0], j + dy[0])) {
            ch1 = grid[i + dx[0]][j + dy[0]];
        } else {
            return 0;
        }

        if(checkIndex(i + dx[1], j + dy[1])) {
            ch2 = grid[i + dx[1]][j + dy[1]];
        } else {
            return 0;
        }

        if(checkIndex(i + dx[2], j + dy[2])) {
            ch3 = grid[i + dx[2]][j + dy[2]];
        } else {
            return 0;
        }

        if(checkIndex(i + dx[3], j + dy[3])) {
            ch4 = grid[i + dx[3]][j + dy[3]];
        } else {
            return 0;
        }

        if(ch1 == 'M' && ch2 == 'M' && ch3 == 'S' && ch4 == 'S') {
            return 1;
        }

        if(ch1 == 'M' && ch2 == 'S' && ch3 == 'S' && ch4 == 'M') {
            return 1;
        }

        if(ch1 == 'S' && ch2 == 'M' && ch3 == 'M' && ch4 == 'S') {
            return 1;
        }

        if(ch1 == 'S' && ch2 == 'S' && ch3 == 'M' && ch4 == 'M') {
            return 1;
        }

        return 0;
    }

    boolean checkIndex(int i, int j) {
        return i >= 0 && i < grid.length && j >= 0 && j < grid[0].length;
    }

    List<String> getInput() {
        String input = FilesUtil.getContentOf("src/day4/input");
        return Arrays.stream(input.split("\n"))
                .toList();
    }
}
