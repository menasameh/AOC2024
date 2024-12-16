package day16;

import day15.Day15;
import utils.FilesUtil;

import java.util.*;

public class Day16 {
    public void smallSol() {
        Game game = getInput();

        int sol = game.runDijkstraForOnePath();
        System.out.println(sol);
    }

    public void largeSol() {
        Game game = getInput();
        int count = game.countTilesInShortestPaths();

        System.out.println(count);
    }

    Game getInput() {
        String input = FilesUtil.getContentOf("src/day16/input");
        return new Game(input);
    }

    static class Game {
        char[][] grid;
        Point start;
        Point end;

        Set<String> paths;

        Game(String gridData) {
            String[] gridLines = gridData.split("\n");
            this.grid = new char[gridLines.length][gridLines[0].length()];

            for (int i = 0; i < gridLines.length; i++) {
                for (int j = 0; j < gridLines[i].length(); j++) {
                    grid[i][j] = gridLines[i].charAt(j);

                    if (gridLines[i].charAt(j) == 'S') {
                        start = new Point(i, j, 1, 0, "");
                    }

                    if (gridLines[i].charAt(j) == 'E') {
                        end = new Point(i, j, 0, 0, "");
                    }
                }
            }
        }

        int runDijkstraForOnePath() {
            Map<Point, Integer> distances = new HashMap<>();
            PriorityQueue<Pair<Integer, Point>> pq = new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));
            Set<Point> visited = new HashSet<>();
            paths = new HashSet<>();

            pq.add(new Pair<>(0, start));
            distances.put(start, 0);

            while (!pq.isEmpty()) {
                Pair<Integer, Point> current = pq.poll();
                Point currentPoint = current.getValue();
                int currentDistance = current.getKey();

                if (visited.contains(currentPoint)) continue;
                visited.add(currentPoint);

                if (currentPoint.x == end.x && currentPoint.y == end.y) {
                    paths.add(currentPoint.path);
                    return currentDistance;
                }

                for (Point neighbor : getNeighbors(currentPoint)) {
                    int newCost = currentDistance + neighbor.cost;
                    if (!distances.containsKey(neighbor) || newCost < distances.get(neighbor)) {
                        distances.put(neighbor, newCost);
                        pq.add(new Pair<>(newCost, neighbor));
                    }
                }
            }
            return Integer.MAX_VALUE; // No path found
        }

        int countTilesInShortestPaths() {
            Map<Point, Integer> distancesFromStart = runDijkstraForAll(start);
            Map<Point, Integer> distancesFromEnd = runDijkstraForAll(end);
            int minScore = distancesFromStart.getOrDefault(end, Integer.MAX_VALUE);
            int tileCount = 0;

            Map<Integer, Integer> frequencyMap = new HashMap<>();
            for (Map.Entry<Point, Integer> entry : distancesFromStart.entrySet()) {
                Point point = entry.getKey();
                int distanceFromStart = entry.getValue();

                int distanceFromEnd = distancesFromEnd.getOrDefault(point, Integer.MAX_VALUE);
                int totalDistance = distanceFromStart + distanceFromEnd;
                frequencyMap.put(totalDistance, frequencyMap.getOrDefault(totalDistance, 0) + 1);

                if (totalDistance == minScore) {
                    tileCount++;
                }
            }

            return tileCount;
        }

        Map<Point, Integer> runDijkstraForAll(Point source) {
            Map<Point, Integer> distances = new HashMap<>();
            PriorityQueue<Pair<Integer, Point>> pq = new PriorityQueue<>(Comparator.comparingInt(Pair::getKey));
            Set<Point> visited = new HashSet<>();

            distances.put(source, 0);
            pq.add(new Pair<>(0, source));

            while (!pq.isEmpty()) {
                Pair<Integer, Point> current = pq.poll();
                Point currentPoint = current.getValue();
                int currentDistance = current.getKey();

                if (visited.contains(currentPoint)) continue;
                visited.add(currentPoint);

                for (Point neighbor : getNeighbors(currentPoint)) {
                    int newCost = currentDistance + neighbor.cost;
                    if (!distances.containsKey(neighbor) || newCost < distances.get(neighbor)) {
                        distances.put(neighbor, newCost);
                        pq.add(new Pair<>(newCost, neighbor));
                    }
                }
            }

            return distances;
        }

        List<Point> getNeighbors(Point current) {
            List<Point> neighbors = new ArrayList<>();
            Point forward = current.moveForward();
            if (isValid(forward)) neighbors.add(forward);

            Point turnRight = current.turnRight();
            neighbors.add(turnRight);

            Point turnLeft = current.turnLeft();
            neighbors.add(turnLeft);

            return neighbors;
        }

        boolean isValid(Point point) {
            return point.x >= 0 && point.x < grid.length && point.y >= 0 && point.y < grid[0].length && grid[point.x][point.y] != '#';
        }
    }

    static class Point {
        // dir: up, right, down, left -> 0, 1, 2, 3
        int x,y,dir;
        int cost;
        String path;

        Point(int x, int y, int dir, int cost, String path) {
            this.x = x;
            this.y = y;
            this.dir = dir;
            this.cost = cost;
            this.path = path;
        }

        Point turnRight() {
            return new Point(x, y, (dir+1) % 4, 1000, path + ">");
        }
        Point turnLeft() {
            return new Point(x, y, (dir-1+4) % 4, 1000, path + "<");
        }
        Point moveForward() {
            return switch (dir) {
                case 0 -> new Point(x - 1, y, dir, 1, path + "^");
                case 1 -> new Point(x, y + 1, dir, 1, path + "^");
                case 2 -> new Point(x + 1, y, dir, 1, path + "^");
                case 3 -> new Point(x, y - 1, dir, 1, path + "^");
                default -> new Point(x, y, dir, 1, path + "^");
            };
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point point = (Point) o;
            return x == point.x && y == point.y && dir == point.dir;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, dir);
        }

    }

    static class Pair<K, V> {
        private final K key;
        private final V value;

        Pair(K key, V value) {
            this.key = key;
            this.value = value;
        }

        public K getKey() {
            return key;
        }

        public V getValue() {
            return value;
        }
    }
}
