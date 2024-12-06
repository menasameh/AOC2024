package day5;

import utils.FilesUtil;

import java.util.*;

public class Day5 {

    List<List<Integer>> rules;
    Set<Integer> nodes;
    List<Integer> topologicalOrder;

    public void smallSol() {
        Game game = getInput();

        buildRulesEdgeList(game);

        int ans = game.updates.stream()
                .filter(this::isValidList)
                .map(update -> update.get(update.size()/2))
                .mapToInt(Integer::valueOf)
                .sum();

        System.out.println(ans);
    }

    public void largeSol() {
        Game game = getInput();

        buildRulesEdgeList(game);

        List<List<Integer>> filtered = game.updates.stream()
                .filter( list -> !isValidList(list)).toList();

        int ans = filtered.stream()
                .map(list -> correctOrder(game, list))
                .map(update -> update.get(update.size()/2))
                .mapToInt(Integer::valueOf)
                .sum();

        System.out.println(ans);
    }

    void buildRulesEdgeList(Game game) {
        rules = new ArrayList<>(105);
        nodes = new HashSet<>();
        for(int i=0;i<105;i++) {
            rules.add(new ArrayList<>());
        }

        for (List<Integer> rule : game.rules) {
            rules.get(rule.getFirst()).add(rule.getLast());
            nodes.add(rule.getFirst());
            nodes.add(rule.getLast());
        }
    }

    void buildRulesEdgeList(Game game, List<Integer> list) {
        rules = new ArrayList<>(105);
        nodes = new HashSet<>();
        for(int i=0;i<105;i++) {
            rules.add(new ArrayList<>());
        }

        for (List<Integer> rule : game.rules) {
            if(list.contains(rule.getFirst()) && list.contains(rule.getLast())) {
                rules.get(rule.getFirst()).add(rule.getLast());
                nodes.add(rule.getFirst());
                nodes.add(rule.getLast());
            }
        }
    }

    boolean isValidList(List<Integer> list) {
        Map<Integer, Integer> position = new HashMap<>();
        for (int i = 0; i < list.size(); i++) {
            position.put(list.get(i), i);
        }

        for (int u = 0; u < rules.size(); u++) {
            for (int v : rules.get(u)) {
                if (position.containsKey(u) &&
                        position.containsKey(v) &&
                        position.get(u) > position.get(v)) {
                    return false;
                }
            }
        }

        return true;
    }

//    void createOrderedRules() {
//        OrderdRules = new ArrayList<>(Arrays.asList(
//                82, 59, 63, 74, 43, 65, 11, 91, 33, 21, 12, 32, 37, 27, 92, 38, 51,
//                96, 39, 44, 15, 57, 61, 16, 58, 55, 26, 77, 99, 42, 64, 71, 46, 72,
//                52, 34, 79, 47, 88, 31, 45, 18, 81, 48, 54, 56, 93, 76, 84
//        ));
//    }

    // This is incorrect the graph is not DAG, it has cycles.
    List<Integer> createTopologicalSort() {
        int[] inDegree = new int[105];
        for (List<Integer> edges : rules) {
            for (int neighbor : edges) {
                inDegree[neighbor]++;
            }
        }

        Queue<Integer> queue = new LinkedList<>();
        for (int i = 0; i < 105; i++) {
            if (inDegree[i] == 0) queue.offer(i);
        }

        List<Integer> topologicalOrder = new ArrayList<>();
        while (!queue.isEmpty()) {
            int node = queue.poll();
            topologicalOrder.add(node);
            for (int neighbor : rules.get(node)) {
                if (--inDegree[neighbor] == 0) queue.offer(neighbor);
            }
        }

        this.topologicalOrder = topologicalOrder.stream()
                .filter(item -> nodes.contains(item))
                .toList();

        return this.topologicalOrder;
    }

    List<Integer> correctOrder(Game game, List<Integer> list) {
        buildRulesEdgeList(game, list);
        return createTopologicalSort();
    }

    Game getInput() {
        String input = FilesUtil.getContentOf("src/day5/input");
        String[] parts = input.split("\n\n");
        return new Game(parts[0], parts[1]);
    }

    static class Game {
        List<List<Integer>> rules;
        List<List<Integer>> updates;

        Game(String rules, String updates) {
            this.rules = Arrays.stream(rules.split("\n"))
                    .map(line -> Arrays.stream(line.split("\\|"))
                            .map(Integer::valueOf)
                            .toList())
                    .toList();

            this.updates = Arrays.stream(updates.split("\n"))
                    .map(line -> Arrays.stream(line.split(","))
                            .map(Integer::valueOf)
                            .toList())
                    .toList();
        }
    }
}

