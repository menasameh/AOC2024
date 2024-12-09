package day9;

import utils.FilesUtil;

import java.math.BigInteger;
import java.util.List;
import java.util.Stack;


public class Day9 {

    String expanded;
    StringBuilder expandedBuilder;
    Stack<File> files;

    public void smallSol() {
        String input = getInput();
        expandDiskMap(input);

        int lo=0, hi=expanded.length()-1;

        while (lo<hi) {
           if(expanded.charAt(lo) != '.') {
               lo++;
               continue;
           }

           if(expanded.charAt(hi) != '.') {
               expanded = expanded.substring(0,lo)+expanded.charAt(hi)+expanded.substring(lo+1);
               lo++;
               hi--;
           } else {
                hi--;
           }
        }

        BigInteger sum=new BigInteger("0");
        for(long i=0;i<=hi;i++) {
            sum = sum.add(BigInteger.valueOf(i * (expanded.charAt((int) i) - '0')));
//            sum += i * (expanded.charAt((int) i) - '0');
        }
        System.out.println(lo);
        System.out.println(hi);
        System.out.println(sum);
    }

    public void largeSol() {
        String input = getInput();
        enhancedExpandDiskMap(input);

        BigInteger sum = new BigInteger("0");
        while(!files.isEmpty()) {
            File file = files.pop();

            int index = expandedBuilder.indexOf(file.spaceRequired());
            index = Math.min(index, file.index);

            if(index == -1) {
                expandedBuilder.replace(file.index, file.index + file.len, file.value());
            } else {
                expandedBuilder.replace(index, index + file.len, file.value());
                file.index = index;
            }
            sum = sum.add(BigInteger.valueOf(file.checksum()));
        }

        System.out.println(expandedBuilder.toString());
        System.out.println(sum);
    }

    void expandDiskMap(String diskMap) {
        int len = 0;
        for(int i=0;i<diskMap.length();i++) {
            len += diskMap.charAt(i) - '0';
        }

        char[] expanded = new char[len];
        int fileId = 0;
        int index = 0;

        for (int i = 0; i < diskMap.length(); i += 2) {
            int fileLength = Character.getNumericValue(diskMap.charAt(i));
            int freeSpaceLength = i+1 == diskMap.length() ? 0 : Character.getNumericValue(diskMap.charAt(i + 1));

            for(int j=0;j<fileLength;j++) {
                expanded[index+j] = (char) ('0' + fileId);
            }
            index += fileLength;
            for(int j=0;j<freeSpaceLength;j++) {
                expanded[index+j] = '.';
            }
            index += freeSpaceLength;
            fileId++;
        }

        this.expanded = new String(expanded);
    }

    void enhancedExpandDiskMap(String diskMap) {
        files = new Stack<>();
        expandedBuilder = new StringBuilder();
        int fileId = 0;
        int index = 0;

        for (int i = 0; i < diskMap.length(); i += 2) {
            int fileLength = Character.getNumericValue(diskMap.charAt(i));
            int freeSpaceLength = i+1 == diskMap.length() ? 0 : Character.getNumericValue(diskMap.charAt(i + 1));

            files.add(new File(index, fileLength, fileId));
            expandedBuilder.repeat(' ', fileLength);
            expandedBuilder.repeat('.', freeSpaceLength);
            index += fileLength;
            index += freeSpaceLength;
            fileId++;
        }
    }

    String getInput() {
        return FilesUtil.getContentOf("src/day9/input");
    }

    static class File {
        int index;
        int len;
        int id;

        File(int index, int len, int id) {
            this.index = index;
            this.len = len;
            this.id = id;
        }

        String spaceRequired() {
            StringBuilder sb = new StringBuilder();
            sb.repeat('.', len);
            return sb.toString();
        }

        String value() {
            StringBuilder sb = new StringBuilder();
            sb.repeat('a'+id, len);
            return sb.toString();
        }

        long checksum() {
            long sum = 0;
            for(long i=0;i<len;i++) {
                sum += (index+i) * id;
            }

            return sum;
        }

    }
}
