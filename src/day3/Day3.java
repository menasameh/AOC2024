package day3;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day3 {

    public void smallSol() {
        String input = getInput();

        String regex = "mul\\((\\d{1,3}),(\\d{1,3})\\)";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        int sum = 0;

        while (matcher.find()) {
            int num1 = Integer.parseInt(matcher.group(1));
            int num2 = Integer.parseInt(matcher.group(2));

            sum += num1 * num2;
        }
        System.out.println(sum);
    }

    public void largeSol() {
        String input = getInput();

        String doPattern = "do\\(\\)";
        String dontPattern = "don't\\(\\)";
        String mulPattern = "mul\\((\\d{1,3}),(\\d{1,3})\\)";

        Pattern pattern = Pattern.compile(doPattern + "|" + dontPattern + "|" + mulPattern);
        Matcher matcher = pattern.matcher(input);

        int sum = 0;
        boolean mulEnabled = true;

        while (matcher.find()) {
            String match = matcher.group();

            if (match.equals("do()")) {
                mulEnabled = true;
            } else if (match.equals("don't()")) {
                mulEnabled = false;
            } else if (mulEnabled && match.startsWith("mul")) {
                int num1 = Integer.parseInt(matcher.group(1));
                int num2 = Integer.parseInt(matcher.group(2));
                sum += num1 * num2;
            }
        }
        System.out.println(sum);
    }

    String getInput() {
        return FilesUtil.getContentOf("src/day3/input");
    }
}
