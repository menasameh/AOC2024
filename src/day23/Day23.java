package day23;

import utils.FilesUtil;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Day23 {
    Map<String, Set<String>> adjacencyList;

    public void smallSol() {
        List<Edge> list = getInput();
        setupAdjacencyList(list);
        List<List<String>> ans = findTriplets();
        System.out.println(ans.stream().filter(items -> items.stream().anyMatch(item -> item.startsWith("t"))).count());
    }

    public void largeSol() {
        List<Edge> list = getInput();
        setupAdjacencyList(list);

        System.out.println(getPassword());
    }

    String getPassword() {

        List<String> passwords = new ArrayList<>();

        for (String node : adjacencyList.keySet()) {
            List<String> neighbors = adjacencyList.get(node).stream().toList();
            // No 14 nodes are connected, so stating from 12 neighbours till we reach 3
            // we are sure that there is no 14 nodes chain, and for sure there is a 3 nodes chain

            boolean hasFoundSol = false;
            for(int i=12;i>2;i--) {
                List<List<String>> combinationsNeighbors = getAllGroups(neighbors, i);
                for(List<String> setOfNodes : combinationsNeighbors) {
                    if(isFullyConnected(setOfNodes)) {
                        passwords.add(generatePassword(setOfNodes, node));
                        hasFoundSol = true;
                        break;
                    }
                }
                if(hasFoundSol) {
                    break;
                }
            }
        }

        return passwords.stream().sorted(Comparator.comparingInt(String::length)).toList().getLast();
    }

    String generatePassword(List<String> setOfNodes, String node) {
        List<String> nodes = new ArrayList<>(setOfNodes);
        nodes.add(node);

        return nodes.stream().sorted().collect(Collectors.joining(","));
    }

    List<List<String>> getAllGroups(List<String> elements, int groupSize) {
        List<List<String>> result = new ArrayList<>();
        getAllGroupsHelper(elements, groupSize, 0, new ArrayList<>(), result);
        return result;
    }

    void getAllGroupsHelper(List<String> elements, int groupSize, int start, List<String> current, List<List<String>> result) {
        if (current.size() == groupSize) {
            result.add(new ArrayList<>(current));
            return;
        }

        for (int i = start; i < elements.size(); i++) {
            current.add(elements.get(i));
            getAllGroupsHelper(elements, groupSize, i + 1, current, result);
            current.remove(current.size() - 1);
        }
    }


    void setupAdjacencyList(List<Edge> edges) {
        adjacencyList = new HashMap<>();
        for (Edge edge : edges) {
            adjacencyList.computeIfAbsent(edge.from, k -> new HashSet<>()).add(edge.to);
            adjacencyList.computeIfAbsent(edge.to, k -> new HashSet<>()).add(edge.from);
        }
    }

    boolean isFullyConnected(List<String> nodes) {
        for (String node : nodes) {
            Set<String> neighbors = adjacencyList.getOrDefault(node, new HashSet<>());
            for (String otherNode : nodes) {
                if (!node.equals(otherNode) && !neighbors.contains(otherNode)) {
                    return false;
                }
            }
        }
        return true;
    }

    List<List<String>> findTriplets() {
        Set<List<String>> triplets = new HashSet<>();
        for (String node : adjacencyList.keySet()) {
            Set<String> neighbors = adjacencyList.get(node);
            List<String> neighborsList = new ArrayList<>(neighbors);
            for (int i = 0; i < neighborsList.size(); i++) {
                for (int j = i + 1; j < neighborsList.size(); j++) {
                    List<String> triplet = List.of(node, neighborsList.get(i), neighborsList.get(j));
                    if(isFullyConnected(triplet)) {
                        triplets.add(triplet.stream().sorted().toList());
                    }
                }
            }
        }
        return triplets.stream().toList();
    }

    List<Edge> getInput() {
        String input = FilesUtil.getContentOf("src/day23/input");
        return Arrays.stream(input.split("\n"))
                .map(Edge::new)
                .toList();
    }


    static class Edge {
        String from;
        String to;

        Edge(String from, String to) {
            this.from = from;
            this.to = to;
        }

        Edge(String in) {
            String[] parts = in.split("-");
            this.from = parts[0];
            this.to = parts[1];
        }
    }
}
