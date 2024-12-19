package day19;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;


public class Day19 {
    Game game;
    HashMap<String, Long> dp;

    public void smallSol() {
        game = getInput();
        String patternRegex = "^((" + String.join(")|(", game.patterns) + "))+$";
        int ans = 0;
        for(String towel : game.towels) {
            if(towel.matches(patternRegex)) {
                ans++;
            }
        }
        System.out.println(ans);
    }

    public void largeSol() {
        game = getInput();
        dp = new HashMap<>();

        long ans = 0;
        for(String towel : game.towels) {
            ans += runBT(towel);
        }
        System.out.println(ans);
    }

    long runBT(String towel) {
        return bt(towel, "");
    }

    long bt(String s, String acc) {
        if(s.isEmpty()) {
            return 1;
        }

        if(dp.containsKey(s)) {
            return dp.get(s);
        }

        long ans = 0;
        for(String pattern : game.patterns) {
            if(s.startsWith(pattern)) {
                ans += bt(s.substring(pattern.length()), acc + ";" + pattern);
            }
        }
        dp.put(s, ans);
        return ans;
    }

    Game getInput() {
        String input = FilesUtil.getContentOf("src/day19/input");
        return new Game(Arrays.stream(input.split("\n\n")).toList());
    }

    static class Game {
        List<String> patterns;
        List<String> towels;

        public Game(List<String> lines) {
            this.patterns = Arrays.stream(lines.getFirst().split(", ")).toList();
            this.towels = Arrays.stream(lines.getLast().split("\n")).toList();
        }
    }
}
