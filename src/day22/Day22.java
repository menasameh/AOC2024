package day22;
import utils.FilesUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Day22 {
    long pruningConst = 16777216L;

    public void smallSol() {
        List<Long> list = getInput();

        long ans = 0;
        for(long i : list) {
            ans += calculateNthSecret(i, 2000);
        }

        System.out.println(ans);
    }

    public void largeSol() {
        List<Long> list = getInput();

        List<List<Integer>> priceForAll = new ArrayList<>();

        for(long i : list) {
            priceForAll.add(calculateNthSecretPerserving10(i, 2000));
        }

        System.out.println(solve(priceForAll));
    }

    long solve(List<List<Integer>> priceForAll) {
        long bananas = 0;

        HashMap<String, Long> cache = new HashMap<>();

        int[] diff = {0, 0, 0, 0};

        for(List<Integer> list : priceForAll) {
            for(int i=4;i<list.size();i++) {
                diff[3] = list.get(i) - list.get(i-1);
                diff[2] = list.get(i-1) - list.get(i-2);
                diff[1] = list.get(i-2) - list.get(i-3);
                diff[0] = list.get(i-3) - list.get(i-4);

                long currentBananas;
                if(cache.containsKey(getCacheKey(diff))) {
                    currentBananas = cache.get(getCacheKey(diff));
                } else {
                    currentBananas = getCurrentBananas(priceForAll, diff);
                    cache.put(getCacheKey(diff), currentBananas);
                }
                bananas = Math.max(bananas, currentBananas);
            }
        }

        return bananas;
    }

    String getCacheKey(int[] diff) {
        return diff[0] + ";" + diff[1] + ";" + diff[2] + ";" + diff[3];
    }

    private static long getCurrentBananas(List<List<Integer>> priceForAll, int[] diff) {
        long currentBananas = 0;
        for(List<Integer> items : priceForAll) {
            for (int j = 4; j < items.size(); j++) {
                if(
                    diff[3] == items.get(j) - items.get(j -1) &&
                    diff[2] == items.get(j -1) - items.get(j -2) &&
                    diff[1] == items.get(j -2) - items.get(j -3) &&
                    diff[0] == items.get(j -3) - items.get(j -4)
                ) {
                    currentBananas += items.get(j);
                    break;
                }
            }
        }
        return currentBananas;
    }


    List<Integer> calculateNthSecretPerserving10(long input, int n) {
        long secret = input;
        List<Integer> res = new ArrayList<>();
        res.add((int) (input % 10));
        for(int i=0;i<n;i++) {
            secret = calculateNextSecret(secret);
            res.add((int) (secret % 10));
        }
        return res;
    }

    long calculateNthSecret(long input, int n) {
        long secret = input;

        for(int i=0;i<n;i++) {
            secret = calculateNextSecret(secret);
        }
        return secret;
    }


    long mix(long secretNumber, long value) {
        return secretNumber ^ value;
    }

    long prune(long secretNumber) {
        return secretNumber % pruningConst;
    }

    long calculateNextSecret(long secretNumber) {
        secretNumber = mix(secretNumber, secretNumber * 64);
        secretNumber = prune(secretNumber);

        secretNumber = mix(secretNumber, secretNumber / 32);
        secretNumber = prune(secretNumber);

        secretNumber = mix(secretNumber, secretNumber * 2048);
        secretNumber = prune(secretNumber);

        return secretNumber;
    }


    List<Long> getInput() {
        String input = FilesUtil.getContentOf("src/day22/input");
        return Arrays.stream(input.split("\n"))
                .map(Long::parseLong)
                .toList();
    }
}
