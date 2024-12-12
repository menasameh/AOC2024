package day12;
import utils.FilesUtil;

import java.util.*;

public class Day12 {
    char[][] grid;
    List<Region> regions;
    Set<String> seen = new HashSet<>();

    int[] dx = {0, 0, 1, -1};
    int[] dy = {1, -1, 0, 0};

    public void smallSol() {
        grid = getInput();
        regions = new ArrayList<>();

        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                startDfs(i, j);
            }
        }

        System.out.println(regions.stream().map(Region::cost).mapToInt(Integer::valueOf).sum());
    }

    public void largeSol() {
        grid = getInput();
        regions = new ArrayList<>();

        for(int i=0;i<grid.length;i++) {
            for(int j=0;j<grid[0].length;j++) {
                startDfs2(i, j);
            }
        }

        System.out.println(regions.stream().map(Region::cost).mapToInt(Integer::valueOf).sum());
    }

    void startDfs(int i, int j) {
        int area = 0;
        int per = 0;
        Stack<Point> dfs = new Stack<>();
        dfs.add(new Point(i, j));

        while(!dfs.isEmpty()) {
            Point cur = dfs.pop();
            int val = grid[(int)cur.x][(int)cur.y];
            if(seen.contains(cur.value())) {
                continue;
            }
            area++;
            for(int k=0;k<4;k++) {
                Point neighbour = new Point(cur.x+dx[k], cur.y+dy[k]);
                if(isValid(neighbour)) {
                    if(grid[(int)neighbour.x][(int)neighbour.y] != val) {
                        per++;
                    }
                } else {
                    per++;
                }
            }
            seen.add(cur.value());

            for(int k=0;k<4;k++) {
                Point next = new Point(cur.x+dx[k], cur.y+dy[k]);
                if(isValid(next) && !seen.contains(next.value())) {
                    if(grid[(int)next.x][(int)next.y] == val) {
                        dfs.add(next);
                    }
                }
            }
        }
        if(area != 0 && per != 0) {
            regions.add(new Region(area, per));
        }
    }

    void startDfs2(int i, int j) {
        int area = 0;
        int per = 0;
        List<Point> fence = new ArrayList<>();
        Stack<Point> dfs = new Stack<>();
        dfs.add(new Point(i, j));

        while(!dfs.isEmpty()) {
            Point cur = dfs.pop();
            int val = grid[(int)cur.x][(int)cur.y];
            if(seen.contains(cur.value())) {
                continue;
            }
            area++;
            for(int k=0;k<4;k++) {
                Point neighbour = new Point(cur.x+dx[k], cur.y+dy[k]);
                if(isValid(neighbour)) {
                    if(grid[(int)neighbour.x][(int)neighbour.y] != val) {
                        Point fenceItem = cur.halfDistance(neighbour);
                        fenceItem.upside = cur.setFenceAccordingTo(neighbour);
                        fence.add(fenceItem);
                        per++;
                    }
                } else {
                    Point fenceItem = cur.halfDistance(neighbour);
                    fenceItem.upside = cur.setFenceAccordingTo(neighbour);
                    fence.add(fenceItem);
                    per++;
                }
            }
            seen.add(cur.value());

            for(int k=0;k<4;k++) {
                Point next = new Point(cur.x+dx[k], cur.y+dy[k]);
                if(isValid(next) && !seen.contains(next.value())) {
                    if(grid[(int) next.x][(int) next.y] == val) {
                        dfs.add(next);
                    }
                }
            }
        }

        List<Point> horizontal = fence.stream().filter(Point::isHorizontal).sorted((left, right) -> {
            if(left.x == right.x) {
                return (int) (left.y - right.y);
            }
            return (int) (left.x - right.x);
        }).toList();
        List<Point> vertical = fence.stream().filter(Point::isVertical).sorted((left, right) -> {
            if(left.y == right.y) {
                return (int) (left.x - right.x);
            }
            return (int) (left.y - right.y);
        }).toList();

        int horizontalCount = 0;
        if(horizontal.size() == 1) {
            horizontalCount = 1;
        } else {
            horizontalCount++;
            for(int k=1;k<horizontal.size();k++) {
                if(horizontal.get(k).x == horizontal.get(k-1).x && horizontal.get(k).upside == horizontal.get(k-1).upside && Math.abs((int)horizontal.get(k).y - (int)horizontal.get(k-1).y) == 1) {
                    continue;
                } else {
                    horizontalCount++;
                }
            }
        }

        int verticalCount = 0;
        if(vertical.size() == 1) {
            verticalCount = 1;
        } else {
            verticalCount++;
            for(int k=1;k<vertical.size();k++) {
                if(vertical.get(k).y == vertical.get(k-1).y && vertical.get(k).upside == vertical.get(k-1).upside&& Math.abs((int)vertical.get(k).x - (int)vertical.get(k-1).x) == 1) {
                    continue;
                } else {
                    verticalCount++;
                }
            }
        }

        if(area != 0 && per != 0) {
            regions.add(new Region(area, horizontalCount + verticalCount));
        }
    }

    boolean isValid(Point p) {
        return p.x >= 0 && p.x < grid.length && p.y >=0 && p.y < grid[0].length;
    }

    char[][] getInput() {
        String input = FilesUtil.getContentOf("src/day12/input");
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
        double x;
        double y;
        // used for fence, true means fence is pointing up or left
        boolean upside = true;

        Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        Point halfDistance(Point to) {
            return new Point((x+to.x)/2, (y+to.y)/2);
        }

        boolean setFenceAccordingTo(Point second) {
            if(x == second.x) {
                return y < second.y;
            } else if (y == second.y) {
                return x < second.x;
            }

            return false;
        }

        boolean isHorizontal() {
            return x - (int) x != 0;
        }

        boolean isVertical() {
            return y - (int) y != 0;
        }

        String value() {
            return x + ";" + y;
        }
    }

    static class Region {
        int area;
        int per;

        Region(int area, int per) {
            this.area = area;
            this.per = per;
        }

        int cost() {
            return area * per;
        }
    }
}
