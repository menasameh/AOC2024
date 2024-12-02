package day1;

import utils.FilesUtil;
import java.util.*;

public class Day1 {
    public void smallSol() {
        Pair input = getInput();

        List<Integer> right = input.right;
        List<Integer> left = input.left;

        right = right.stream().sorted().toList();
        left = left.stream().sorted().toList();


        int ans = 0;
        for(int i=0;i<right.size();i++) {
            ans += Math.abs(right.get(i) - left.get(i));
        }
        System.out.println(ans);
    }

    public void largeSol() {
        Pair input = getInput();

        List<Integer> right = input.right;
        List<Integer> left = input.left;

        int ans = 0;
        for(int i = 0; i<right.size(); i++) {

            int finalI = i;
            ans += (int) (left.get(finalI) * right.stream().filter(item -> Objects.equals(item, left.get(finalI))).count());
        }
        System.out.println(ans);
    }


    Pair getInput() {
        String input = FilesUtil.getContentOf("src/day1/input");
        List<Integer> right = new ArrayList<>();
        List<Integer> left = new ArrayList<>();

        List<List<Integer>> list = Arrays.stream(input.split("\n"))
                .map(line -> Arrays.stream(line.split("\\W+"))
                        .map(Integer::valueOf)
                        .toList())
                .toList();

        list.forEach( line ->
        {
            left.add(line.get(0));
            right.add(line.get(1));
        });
        return new Pair(left, right);
    }
}

class Pair {
    List<Integer> right;
    List<Integer> left;

    Pair(List<Integer> right, List<Integer> left) {
        this.left = left;
        this.right = right;
    }

}