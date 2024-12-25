package day25;

import utils.FilesUtil;

import java.util.ArrayList;
import java.util.List;

public class Day25 {
    public void smallSol() {
        Game game = getInput();

        long ans = 0;
        for(Schema key : game.keys) {
            for(Schema lock : game.locks) {
                if(key.fits(lock)) {
                    ans++;
                }
            }
        }
        System.out.println(ans);
    }
    
    Game getInput() {
        String input = FilesUtil.getContentOf("src/day25/input");
        return new Game(input.split("\n\n"));
    }

    static class Game {
        List<Schema> keys;
        List<Schema> locks;

        Game(String[] in) {
            keys = new ArrayList<>();
            locks = new ArrayList<>();
            for(String s : in) {
                Schema schema = new Schema(s);

                if(schema.type == 0) {
                    locks.add(schema);
                } else {
                    keys.add(schema);
                }
            }
        }
    }

    static class Schema {
        int[] nums;
        int type;

        Schema(String in) {
            String[] lines = in.split("\n");
            nums = new int[5];

            for(int i=0;i<lines[0].length();i++) {
                int count = 0;
                for (String line : lines) {
                    if (line.charAt(i) == '#') {
                        count++;
                    }
                }
                nums[i] = count - 1;
            }

            type = lines[0].equals("#####") ? 0 : 1;
        }

        boolean fits(Schema lock) {
            for(int i=0;i<5;i++) {
                if(nums[i] + lock.nums[i] > 5) {
                    return false;
                }
            }
            return true;
        }
    }
}
