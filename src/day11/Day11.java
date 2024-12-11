package day11;

import utils.FilesUtil;

import java.util.*;

public class Day11 {
    int n = 25;
    List<Long> list;
    Map<Long, Long>[] dp;

    public void smallSol() {
        list = getInput();
        System.out.println(runDP());
    }

    public void longSol() {
        n = 75;
        list = getInput();
        System.out.println(runDP());
    }

    // To get star 1 fast enough :D
//    List<Long> simulateN(List<Long> list, int n) {
//        for(int i=0;i<n;i++) {
//            list = simulateStep(list);
//        }
//        return list;
//    }
//
//    List<Long> simulateStep(List<Long> list) {
//        List<Long> next = new ArrayList<>();
//        for(int i=0;i<list.size();i++) {
//            if(list.get(i) == 0) {
//                next.add(1L);
//            } else if (("" + list.get(i)).length() % 2 == 0) {
//                String num =  "" + list.get(i);
//                next.add(Long.parseLong(num.substring(0, num.length()/2)));
//                next.add(Long.parseLong(num.substring(num.length()/2)));
//            } else {
//                next.add(list.get(i) * 2024);
//            }
//        }
//        return next;
//    }

    long runDP() {
        dp = new HashMap[n + 1];
        for (int i = 0; i <= n; i++) {
            dp[i] = new HashMap<>();
        }
        for (int i = 0; i < list.size(); i++) {
            dp[0].put(list.get(i), 1L);
        }

        for (int time = 1; time <= n; time++) {
            for (Map.Entry<Long, Long> entry : dp[time - 1].entrySet()) {
                long num = entry.getKey();
                long count = entry.getValue();

                if (num == 0) {
                    dp[time].put(1L, dp[time].getOrDefault(1, 0L) + count);
                }
                else if (("" + num).length() % 2 == 0) {
                    String numString =  "" +num;
                    long num1 = Long.parseLong(numString.substring(0, numString.length()/2));
                    long num2 = Long.parseLong(numString.substring(numString.length()/2));
                    dp[time].put(num1, dp[time].getOrDefault(num1, 0L) + count);
                    dp[time].put(num2, dp[time].getOrDefault(num2, 0L) + count);
                }
                else {
                    long newNum = num * 2024;
                    dp[time].put(newNum, dp[time].getOrDefault(newNum, 0L) + count);
                }
            }
        }

        return dp[n].values().stream().mapToLong(Long::valueOf).sum();
    }

    List<Long> getInput() {
        String input = FilesUtil.getContentOf("src/day11/input");
        return Arrays.stream(input.split(" "))
                .map(Long::valueOf)
                .toList();
    }
}
