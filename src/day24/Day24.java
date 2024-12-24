package day24;

import utils.FilesUtil;

import java.util.*;

public class Day24 {
    HashMap<String, Boolean> ivalues;
    List<Op> ioperations;

    HashMap<String, Boolean> values;
    List<Op> operations;

    public void smallSol() {
        getInput();
        values = new HashMap<>(ivalues);
        operations = new ArrayList<>(ioperations);
        run();
        System.out.println(getResult("z"));
    }

    public void largeSol() {
        getInput();
        values = new HashMap<>(ivalues);
        operations = new ArrayList<>(ioperations);

        swap("vcv", "z13");
        swap("vwp", "z19");
        swap("mps", "z25");
        swap("vjv", "cqm");


        // Code for visualizing with mermaid
        for(Op operation : operations) {
            String op = operation.operand1 + " " + type(operation) + " " + operation.operand2;
            System.out.println(operation.operand1 + " --> " + "|" + op + "| " + operation.output);
            System.out.println(operation.operand2 + " --> " + "|" + op + "| " + operation.output);
        }

        run();

        System.out.println(getResult("z") == getResult("x") + getResult("y"));
    }

    String type(Op op) {
        return switch (op.type) {
            case 0 -> "AND";
            case 1 -> "OR";
            case 2 -> "XOR";
            default -> "noop";
        };
    }

    void swap(String out1, String out2) {
        Op op1 = operations.stream().filter(item -> item.output.equals(out1)).findFirst().get();
        Op op2 = operations.stream().filter(item -> item.output.equals(out2)).findFirst().get();

        operations.remove(op1);
        operations.remove(op2);

        op1.output = out2;
        op2.output = out1;
        operations.addAll(List.of(op1, op2));
    }

    long getResult(String s) {
        List<Boolean> zValues = values.entrySet().stream()
                .filter(item -> item.getKey().startsWith(s))
                .sorted(Map.Entry.comparingByKey())
                .map(Map.Entry::getValue)
                .toList();

        return toDecimal(zValues);
    }

    long toDecimal(List<Boolean> booleanList) {
        long decimal = 0;
        for (int i = 0; i < booleanList.size(); i++) {
            if (booleanList.get(i)) {
                decimal += (1L << i);
            }
        }
        return decimal;
    }

    void run() {
        PriorityQueue<Op> pq = new PriorityQueue<>((right, left) -> {
            int diff = calculatePriority(left) - calculatePriority(right);
            if(diff == 0) {
                return right.type - left.type;
            }
            return diff;
        });
        pq.addAll(operations);
        while(!pq.isEmpty()) {
            Op cur = pq.poll();

            if(calculatePriority(cur) == 0) {
                pq.add(cur);
                rebuildValues(pq);
                continue;
            }

            if(calculatePriority(cur) == 1) {
                if(cur.type == 0) {
                    if(values.containsKey(cur.operand1)) {
                        if(!values.get(cur.operand1)) {
                            values.put(cur.output, false);
                            rebuildValues(pq);
                        } else {
                            pq.add(new Op(cur.operand2, cur.operand2, cur.output, 0));
                            rebuildValues(pq);
                        }
                        continue;
                    }

                    if(values.containsKey(cur.operand2)) {
                        if(!values.get(cur.operand2)) {
                            values.put(cur.output, false);
                            rebuildValues(pq);
                        } else {
                            pq.add(new Op(cur.operand1, cur.operand1, cur.output, 0));
                            rebuildValues(pq);
                        }
                        continue;
                    }
                }

                if(cur.type == 1) {
                    if(values.containsKey(cur.operand1)) {
                        if(values.get(cur.operand1)) {
                            values.put(cur.output, true);
                            rebuildValues(pq);
                        } else {
                            pq.add(new Op(cur.operand2, cur.operand2, cur.output, 0));
                            rebuildValues(pq);
                        }
                        continue;
                    }

                    if(values.containsKey(cur.operand2)) {
                        if(values.get(cur.operand2)) {
                            values.put(cur.output, true);
                            rebuildValues(pq);
                        } else {
                            pq.add(new Op(cur.operand1, cur.operand1, cur.output, 0));
                            rebuildValues(pq);
                        }
                        continue;
                    }
                }

                if(cur.type == 2) {
                    pq.add(cur);
                    rebuildValues(pq);
                }
                continue;
            }


            boolean par1 = values.get(cur.operand1);
            boolean par2 = values.get(cur.operand2);

            boolean outputValue = switch (cur.type) {
                case 0 -> par1 && par2;
                case 1 -> par1 || par2;
                case 2 -> par1 ^ par2;
                default -> false;
            };

            values.put(cur.output, outputValue);
            rebuildValues(pq);
        }
    }

    void rebuildValues(PriorityQueue<Op> pq) {
        List<Op> elements = new ArrayList<>(pq);
        pq.clear();
        pq.addAll(elements);
    }

    int calculatePriority(Op op) {
        return (values.containsKey(op.operand1) ? 1 : 0) + (values.containsKey(op.operand2) ? 1 : 0);
    }

    void getInput() {
        String input = FilesUtil.getContentOf("src/day24/input");
        ivalues = new HashMap<>();
        String[] parts = input.split("\n\n");

        Arrays.stream(parts[0].split("\n"))
                .forEach(value -> {
                    String[] valueParts = value.split(": ");
                    ivalues.put(valueParts[0], valueParts[1].equals("1"));
                });

        ioperations = Arrays.stream(parts[1].split("\n"))
                .map(Op::new)
                .toList();
    }


    static class Op {
        String operand1;
        String operand2;
        String output;
        int type;

        Op(String in) {
            String[] parts = in.split(" | -> ");
            operand1 = parts[0];
            operand2 = parts[2];
            output = parts[4];

            type = switch (parts[1]) {
                case "AND" -> 0;
                case "OR" -> 1;
                case "XOR" -> 2;
                default -> 3;
            };
        }

        public Op(String operand1, String operand2, String output, int type) {
            this.operand1 = operand1;
            this.operand2 = operand2;
            this.output = output;
            this.type = type;
        }
    }
}
