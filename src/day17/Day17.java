package day17;

import utils.FilesUtil;

import java.util.*;
import java.util.stream.Collectors;

public class Day17 {
    public void smallSol() {
        VirtualMachine vm = getInput();

        List<Integer> out = vm.executeProgram();

        System.out.println(out.stream().map(String::valueOf).collect(Collectors.joining(",")));
    }

    public void largeSol() {
        VirtualMachine vm = getInput();
        long a = bfs(vm.program);
        System.out.println(a);
    }

    long bfs(List<Integer> program) {
        Queue<State> queue = new LinkedList<>();
        queue.add(new State(0, 0));

        while (!queue.isEmpty()) {
            State current = queue.poll();
            int target = program.get((int) (program.size() - current.iteration - 1));

            for (int i = 0; i < 8; i++) { // every 3 bits in register A value contribute to a value in the output
                long newRegisterA = (current.registerA << 3) | i;
                VirtualMachine vm = new VirtualMachine(program, newRegisterA, 0L, 0L);
                List<Integer> output = vm.executeProgram();
                if (output.getFirst() == target) {
                    if (current.iteration == program.size() - 1) {
                        return newRegisterA;
                    } else {
                        queue.add(new State(newRegisterA, current.iteration + 1));
                    }
                }
            }
        }
        return -1;
    }

    VirtualMachine getInput() {
        String input = FilesUtil.getContentOf("src/day17/input");
        String[] parts =  input.split("\n\n");

        String[] registers = parts[0].split("(: )|\n");

        List<Integer> nums = Arrays.stream(parts[1].split("[ ,]"))
                .filter(item -> item.length() < 3)
                .map(Integer::parseInt)
                .toList();

        return new VirtualMachine(nums, Long.parseLong(registers[1]), Long.parseLong(registers[3]), Long.parseLong(registers[5]));
    }

    static class VirtualMachine {
        private List<Integer> program;
        private Long registerA;
        private Long registerB;
        private Long registerC;
        private int instructionPointer;

        public VirtualMachine(List<Integer> program, Long registerA, Long registerB, Long registerC) {
            this.program = program;
            this.registerA = registerA;
            this.registerB = registerB;
            this.registerC = registerC;
            this.instructionPointer = 0;
        }

        public List<Integer> executeProgram() {
            List<Integer> output = new ArrayList<>();

            while (instructionPointer < program.size()) {
                int opcode = program.get(instructionPointer);
                int operand = (instructionPointer + 1 < program.size()) ? program.get(instructionPointer + 1) : 0;

                switch (opcode) {
                    case 0: // adv
                        registerA = registerA / (int) Math.pow(2, getComboValue(operand));
                        break;
                    case 1: // bxl
                        registerB ^= operand;
                        break;
                    case 2: // bst
                        registerB = getComboValue(operand) % 8;
                        break;
                    case 3: // jnz
                        if (registerA != 0) {
                            instructionPointer = operand;
                            continue;
                        }
                        break;
                    case 4: // bxc
                        registerB ^= registerC;
                        break;
                    case 5: // out
                        output.add((int) (getComboValue(operand) % 8));
                        break;
                    case 6: // bdv
                        registerB = registerA / (int) Math.pow(2, getComboValue(operand));
                        break;
                    case 7: // cdv
                        registerC = registerA / (int) Math.pow(2, getComboValue(operand));
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid opcode: " + opcode);
                }

                instructionPointer += 2;
            }
            return output;
        }

        private long getComboValue(int operand) {
            return switch (operand) {
                case 0, 1, 2, 3 -> operand;
                case 4 -> registerA;
                case 5 -> registerB;
                case 6 -> registerC;
                default -> throw new IllegalArgumentException("Invalid " + operand);
            };
        }
    }

    static class State {
        long registerA;
        long iteration;

        State(long registerA, long iteration) {
            this.registerA = registerA;
            this.iteration = iteration;
        }
    }

}
