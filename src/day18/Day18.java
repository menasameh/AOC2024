package day18;

import day17.Day17;
import utils.FilesUtil;

import java.util.*;

public class Day18 {

//    char[][] grid = new char[7][7];
    char[][] grid = new char[71][71];
    int limit = 1024;

    int[] dx = {0, 0, 1, -1};
    int[] dy = {1, -1, 0, 0};

    public void smallSol() {
        List<Point> points = getInput();

        fillGrid(points.stream().limit(limit).toList());

        int sol = bfs(new Point(0, 0), new Point(70, 70));

        System.out.println(sol);
    }

    public void largeSol() {
        List<Point> points = getInput();
        emptyGrid();

        for(Point p : points) {
            fillGrid(p);
            int sol = bfs(new Point(0, 0), new Point(70, 70));

            if(sol == -1) {
                System.out.println(p.value());
                break;
            }
        }
    }

    void fillGrid(List<Point> points) {
        for(int i=0;i<grid[0].length;i++) {
            for(int j=0;j<grid.length;j++) {
                grid[j][i] = '.';
            }
        }

        for(Point p : points) {
            grid[p.y][p.x] = '#';
        }
    }

    void emptyGrid() {
        for(int i=0;i<grid[0].length;i++) {
            for(int j=0;j<grid.length;j++) {
                grid[j][i] = '.';
            }
        }
    }

    void fillGrid(Point p) {
        grid[p.y][p.x] = '#';
    }

    void printGrid() {
        for (char[] chars : grid) {
            for (char ch : chars) {
                System.out.print(ch);
            }
            System.out.println();
        }
    }

    int bfs(Point start, Point end) {
        Queue<State> queue = new LinkedList<>();
        HashSet<String> seen = new HashSet<>();
        queue.add(new State(start, 0));

        while (!queue.isEmpty()) {
            State current = queue.poll();

            if(current.p.x == end.x && current.p.y == end.y) {
                return current.cost;
            }

            if(seen.contains(current.p.value())) {
                continue;
            }
            seen.add(current.p.value());

            for(int i=0;i<4;i++) {
                Point next = new Point(current.p.x + dx[i], current.p.y + dy[i]);

                if(isValid(next)) {
                    queue.add(new State(next, current.cost + 1));
                }
            }
        }
        return -1;
    }

    boolean isValid(Point p) {
        return p.x >= 0 && p.x < grid[0].length && p.y >=0 && p.y < grid.length && grid[p.y][p.x] == '.';
    }

    List<Point> getInput() {
        String input = FilesUtil.getContentOf("src/day18/input");
        return Arrays.stream(input.split("\n"))
                .map(Point::new)
                .toList();
    }

    static class State {
        Point p;
        int cost;

        public State(Point p, int cost) {
            this.p = p;
            this.cost = cost;
        }
    }

    static class Point {
        int x,y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point(String in) {
            String[] parts = in.split(",");
            this.x = Integer.parseInt(parts[0]);
            this.y = Integer.parseInt(parts[1]);
        }

//        @Override
//        public int hashCode() {
//            return Objects.hash(x, y);
//        }
        String value() {
            return x+";"+y;
        }
    }
}
