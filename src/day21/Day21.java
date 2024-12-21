package day21;
import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;

public class Day21 {

    public void smallSol() {
        List<Pair<String, Long>> input = getInput();

        long sum = 0;
        for(Pair<String, Long> list : input) {
            long length = expand(list.left, 2);

            sum += length * list.right;
        }

        System.out.println(sum);
    }

    public void largeSol() {
        List<Pair<String, Long>> input = getInput();

        long sum = 0;
        for(Pair<String, Long> list : input) {
            long length = expand(list.left, 25);
            sum += length * list.right;
        }
        System.out.println(sum);
    }

    long expand(String list, int times) {
        while(times-->0) {
            list = expand(list);
        }
        return list.length();
    }

    String expand(String in) {
        String res = "";

        char prev = 'A';
        for (char ch : in.toCharArray()) {
            res += expand(prev, ch);
            prev = ch;
        }
        return res;
    }

    String expand(char prev, char current) {
        switch(prev) {
            case 'A':
                switch(current) {
                    case 'A':
                        return "A";
                    case '^':
                        return "<A";
                    case '>':
                        return "vA";
                    case '<':
                        return "<<vA";
                    case 'v':
                        return "<vA";
                }
            case '^':
                switch(current) {
                    case 'A':
                        return ">A";
                    case '^':
                        return "A";
                    case '>':
                        return "v>A";
                    case '<':
                        return "v<A";
                    case 'v':
                        return "vA";
                }
            case '>':
                switch(current) {
                    case 'A':
                        return "^A";
                    case '^':
                        return "<^A";
                    case '>':
                        return "A";
                    case '<':
                        return "<<A";
                    case 'v':
                        return "<A";
                }
            case '<':
                switch(current) {
                    case 'A':
                        return ">>^A";
                    case '^':
                        return ">^A";
                    case '>':
                        return ">>A";
                    case '<':
                        return "A";
                    case 'v':
                        return ">A";
                }
            case 'v':
                switch(current) {
                    case 'A':
                        return "^>A";
                    case '^':
                        return "^A";
                    case '>':
                        return ">A";
                    case '<':
                        return "<A";
                    case 'v':
                        return "A";
                }
        }
        return null;
    }

    List<Pair<String, Long>> getInput() {
        String input = FilesUtil.getContentOf("src/day21/input");
        return Arrays.stream(input.split("\n"))
                .map(this::getSequence)
                .toList();
    }

    Pair<String, Long> getSequence(String in) {
        return switch (in) {
            case "029A" -> new Pair<>("<A^A>^^AvvvA", 29L);
            case "980A" -> new Pair<>("^^^A<AvvvA>A", 980L);
            case "179A" -> new Pair<>("^<<A^^A>>AvvvA", 179L);
            case "456A" -> new Pair<>("^^<<A>A>AvvA", 456L);
            case "379A" -> new Pair<>("^A<<^^A>>AvvvA", 379L);

            case "805A" -> new Pair<>("<^^^AvvvA^^Avv>A", 805L);
            case "682A" -> new Pair<>("^^A<^AvvAv>A", 682L);
            case "671A" -> new Pair<>("^^A<<^AvvA>>vA", 671L);
            case "973A" -> new Pair<>("^^^A<<Avv>>AvA", 973L);
            case "319A" -> new Pair<>("^A<<A^^>>AvvvA", 319L);
            default ->  new Pair<>("", 0L);
        };
    }

    static class Pair<k, v> {
        v right;
        k left;

        Pair(k left, v right) {
            this.left = left;
            this.right = right;
        }
    }
}
