package day7;

import day5.Day5;
import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;

public class Day7 {
    public void smallSol() {
        List<Line> lines = getInput();


        long ans = lines.stream()
                .filter(this::isValid)
                .map(line -> line.result)
                .mapToLong(Long::valueOf)
                .sum();

        System.out.println(ans);
    }

    public void largeSol() {
        List<Line> lines = getInput();


        long ans = lines.stream()
                .filter(this::isValid2)
                .map(line -> line.result)
                .mapToLong(Long::valueOf)
                .sum();

        System.out.println(ans);
    }

    boolean isValid(Line line) {
        return dp(line.nums, 0, 0, line.result);
    }

    boolean dp(long[] nums, int index, long curr, long result) {
        if(index == nums.length) {
            if(curr == result) {
                return true;
            }
            return false;
        }

        if(dp(nums, index+1, curr + nums[index], result)) {
            return true;
        }

        if(index != 0 && dp(nums, index+1, curr * nums[index], result)) {
            return true;
        }

        return false;
    }

    boolean isValid2(Line line) {
        return dp2(line.nums, 0, 0, line.result);
    }

    boolean dp2(long[] nums, int index, long curr, long result) {
        if(index == nums.length) {
            if(curr == result) {
                return true;
            }
            return false;
        }

        if(dp2(nums, index+1, curr + nums[index], result)) {
            return true;
        }

        if(index != 0 && dp2(nums, index+1, curr * nums[index], result)) {
            return true;
        }

        if(dp2(nums, index+1, concat(curr, nums[index]), result)) {
            return true;
        }

        return false;
    }

    long concat(long x, long y) {
        String total = x + "" + y;
        return Long.parseLong(total);
    }

    List<Line> getInput() {
        String input = FilesUtil.getContentOf("src/day7/input");
        return Arrays.stream(input.split("\n"))
                .map(Line::new)
                .toList();
    }


    static class Line {
        long result;
        long[] nums;

        Line(String in) {
            String[] parts = in.split(": ");

            result = Long.parseLong(parts[0]);
            nums = Arrays.stream(parts[1].split(" ")).mapToLong(Long::valueOf).toArray();
        }
    }
}
