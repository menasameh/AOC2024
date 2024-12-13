package day13;

import utils.FilesUtil;

import java.util.Arrays;
import java.util.List;

public class Day13 {
    public void smallSol() {
        List<Claw> claws = getInput();

        System.out.println(claws.stream().map(Claw::solve).mapToLong(Long::valueOf).sum());
    }

    public void largeSol() {
        List<Claw> claws = getInput();
        claws.forEach(Claw::update);
        System.out.println(claws.stream().map(Claw::solve).mapToLong(Long::valueOf).sum());
    }

    List<Claw> getInput() {
        String input = FilesUtil.getContentOf("src/day13/input");

        return Arrays.stream(input.split("\n\n"))
                .map(Claw::new)
                .toList();
    }

    static class Claw{
        Button a;
        Button b;
        Prize p;

        Claw(String in) {
            String[] lines = in.split("\n");

            a = new Button(lines[0]);
            b = new Button(lines[1]);
            p = new Prize(lines[2]);
        }

        void update() {
            p.x += 10000000000000L;
            p.y += 10000000000000L;
        }

        long solve() {
            // a.x * i + b.x * j = p.x
            // a.y * i + b.y * j = p.y

            // a.x * i = p.x - b.x * j
            // i = (p.x - b.x * j) / a.x

            // input i in the second eq
            // a.y * (p.x - b.x * j) / a.x + b.y * j = p.y

            // simplify
            // a.y * (p.x - b.x * j) + b.y * a.x * j = p.y * a.x
            // a.y * p.x - a.y * b.x * j + b.y * a.x * j = p.y * a.x
            // b.y * a.x * j - a.y * b.x * j = p.y * a.x - a.y * p.x
            // j (b.y * a.x - a.y * b.x) = p.y * a.x - a.y * p.x
            // j = (p.y * a.x - a.y * p.x) / (b.y * a.x - a.y * b.x)

            // input j in i's eq
            // i = (p.x - b.x * j) / a.x

            double j = (double) (p.y * a.x - a.y * p.x) / (b.y * a.x - a.y * b.x);
            double i = (p.x - b.x * j) / a.x;

            if(j - (long) j == 0 && i - (long) i == 0) {
                return (long) (i * 3 + j);
            }

            return 0;
        }
    }

    static class Button {
        int x;
        int y;

        Button(String in) {
            String[] parts = in.split("Button [AB]: X\\+|, Y\\+");
            x = Integer.parseInt(parts[1]);
            y = Integer.parseInt(parts[2]);
        }
    }

    static class Prize {
        long x;
        long y;

        Prize(String in) {
            String[] parts = in.split("Prize: X=|, Y=");
            x = Integer.parseInt(parts[1]);
            y = Integer.parseInt(parts[2]);
        }
    }
}
