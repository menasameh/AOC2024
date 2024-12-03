package day2;

import utils.FilesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Day2 {

    public void smallSol() {
        List<List<Integer>> list = getInput();

        long ans = list.stream()
                .map(this::isSafe)
                .filter(i-> i)
                .count();

        System.out.println(ans);
    }

    public boolean isSafe(List<Integer> levels) {
        boolean isIncreasing = true;
        boolean isDecreasing = true;

        for (int i = 1; i < levels.size(); i++) {
            int diff = levels.get(i) - levels.get(i - 1);
            if (Math.abs(diff) < 1 || Math.abs(diff) > 3) return false;

            if (diff > 0) isDecreasing = false;
            if (diff < 0) isIncreasing = false;
        }

        return isIncreasing || isDecreasing;
    }

    public void largeSol() {
        List<List<Integer>> list = getInput();

        long ans = list.stream()
                .map(this::isSafeWithAnError)
                .filter(i-> i)
                .count();

        System.out.println(ans);
    }

    public boolean isSafeWithAnError(List<Integer> levels) {
        if (isSafe(levels)) return true;

        for (int i = 0; i < levels.size(); i++) {
            List<Integer> modifiedLevels = removeLevel(levels, i);
            if (isSafe(modifiedLevels)) return true;
        }

        return false;
    }

    private static List<Integer> removeLevel(List<Integer> levels, int indexToRemove) {
        List<Integer> modifiedLevels = new ArrayList<>(levels);
        modifiedLevels.remove(indexToRemove);
        return modifiedLevels;
    }

    List<List<Integer>> getInput() {
        String input = FilesUtil.getContentOf("src/day2/input");
        return Arrays.stream(input.split("\n"))
                .map(list -> Arrays.stream(list.split(" "))
                        .map(Integer::valueOf)
                        .toList())
                .toList();
    }
}
