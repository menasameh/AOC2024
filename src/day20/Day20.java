package day20;

import utils.FilesUtil;

import java.util.*;

public class Day20 {

    public void smallSol() {
        Game game = getInput();
        long count = game.solve(2);
        System.out.println(count);
    }

    public void largeSol() {
        Game game = getInput();
        long count = game.solve(20);
        System.out.println(count);
    }

    Game getInput() {
        String input = FilesUtil.getContentOf("src/day20/input");
        return new Game(input);
    }

    static class Game {
        char[][] grid;
        Point start;
        Point end;

        int[] dx = {0, 0, 1, -1};
        int[] dy = {1, -1, 0, 0};

        Game(String gridData) {
            String[] gridLines = gridData.split("\n");
            this.grid = new char[gridLines.length][gridLines[0].length()];

            for (int i = 0; i < gridLines.length; i++) {
                for (int j = 0; j < gridLines[i].length(); j++) {
                    grid[i][j] = gridLines[i].charAt(j);

                    if (gridLines[i].charAt(j) == 'S') {
                        start = new Point(i, j);
                    }

                    if (gridLines[i].charAt(j) == 'E') {
                        end = new Point(i, j);
                    }
                }
            }
        }

        HashMap<String, Integer> bfs(Point start, Point end) {
            Queue<State> queue = new LinkedList<>();
            HashSet<String> seen = new HashSet<>();
            queue.add(new State(start, 0, null, null, null));
            HashMap<String, Integer> distance = new HashMap<>();

            while (!queue.isEmpty()) {
                State current = queue.poll();

                if (seen.contains(current.p.value())) {
                    continue;
                }
                distance.put(current.p.value(), current.time);
                seen.add(current.p.value());

                for (int i = 0; i < 4; i++) {
                    Point next = new Point(current.p.x + dx[i], current.p.y + dy[i]);
                    if (isValid(next)) {
                        queue.add(new State(next, current.time + 1, null, null, null));
                    }
                }
            }
            return distance;
        }

        int solve(int manDistance) {
            HashMap<String, Integer> forwardBFS = bfs(start, end);
            HashMap<String, Integer> backwardBFS = bfs(end, start);
            int shortestPath = forwardBFS.get(end.value());
            int cheats = 0;

            for (int i = 0; i < grid.length; i++) {
                for (int j = 0; j < grid[0].length; j++) {
                    if (grid[i][j] == '#') {
                        continue;
                    }

                    Point cheatS = new Point(i, j);
                    for (int k = -manDistance; k <= manDistance; k++) {
                        int rest = manDistance - Math.abs(k);
                        for (int l = -rest; l <= rest; l++) {
                            Point cheatE = new Point(i + k, j + l);
                            if (isValid(cheatE) && (grid[cheatE.x][cheatE.y] == '.' || grid[cheatE.x][cheatE.y] == 'E')) {
                                int length = forwardBFS.get(cheatS.value()) + Math.abs(k) + Math.abs(l) + backwardBFS.get(cheatE.value());

                                if (shortestPath - length >= 100) {
                                    cheats++;
                                }
                            }
                        }

                    }
                }
            }
            return cheats;
        }

        boolean isValid(Point point) {
            return point.x >= 0 && point.x < grid.length && point.y >= 0 && point.y < grid[0].length && grid[point.x][point.y] != '#';
        }

        boolean isValidDirection(Point current, Point next) {
            return switch (grid[next.x][next.y]) {
                case '.', 'E' -> true;
                case '>' -> current.x == next.x && next.y == current.y + 1;
                case '<' -> current.x == next.x && next.y == current.y - 1;
                case '^' -> current.x == next.x - 1 && next.y == current.y;
                case 'v' -> current.x == next.x + 1 && next.y == current.y;
                default -> false;
            };
        }
    }

    static class Point {
        int x, y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        String value() {
            return x + ";" + y;
        }
    }

    static class State {
        Point p;
        Point cheatStart, cheatEnd;
        int time;
        HashSet<String> seen;

        public State(Point p, int time, Point cheatStart, Point cheatEnd, HashSet<String> seen) {
            this.p = p;
            this.time = time;
            this.cheatStart = cheatStart;
            this.cheatEnd = cheatEnd;
            this.seen = seen;
        }

        String cheat() {
            if (cheatStart == null || cheatEnd == null) {
                return null;
            }
            return cheatStart.value() + "-" + cheatEnd.value();
        }

        String value() {
            return p.value() + "." + cheat();
        }

        @Override
        public boolean equals(Object o) {
            if (o == null || getClass() != o.getClass()) return false;
            State state = (State) o;
            return time == state.time && Objects.equals(p, state.p) && Objects.equals(cheatStart, state.cheatStart) && Objects.equals(cheatEnd, state.cheatEnd);
        }

        @Override
        public int hashCode() {
            return Objects.hash(p, cheatStart, cheatEnd, time);
        }
    }
}
