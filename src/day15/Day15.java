package day15;

import utils.FilesUtil;

public class Day15 {
    public void smallSol() {
        Game game = getInput();
        game.move();

        System.out.println(game.sum());
    }

    public void largeSol() {
        Game game = getInput();
        game.expand();
        game.move();

        System.out.println(game.sum());
    }

    Game getInput() {
        String input = FilesUtil.getContentOf("src/day15/input");
        String[] parts = input.split("\n\n");
        return new Game(parts[0], parts[1]);
    }

    static class Game {
        char[][] grid;
        String moves;

        int playerX;
        int playerY;
        Point robot;

        Game(String gridData, String moves) {
            String[] gridLines = gridData.split("\n");
            this.grid = new char[gridLines.length][gridLines[0].length()];

            for(int i=0;i<gridLines.length;i++) {
                for(int j=0;j<gridLines[i].length();j++) {
                    grid[i][j] = gridLines[i].charAt(j);

                    if(gridLines[i].charAt(j) == '@') {
                        playerX = i;
                        playerY = j;
                        robot = new Point(i, j);
                    }
                }
            }

            this.moves = moves;
        }

        void move() {
            for(int k=0;k<moves.length();k++) {
                char dir = moves.charAt(k);
                if(canMove(robot, dir)) {
                    move(robot, dir);
                    robot = robot.next(dir);
                }
            }
        }

        void moveGrid(char dir) {
            int displacement = canMove(dir);
            if(displacement == -1) {
                return ;
            }

            switch (dir) {
                case '<':
                for(int i=displacement;i<grid[0].length;i++) {
                    grid[playerX][i] = grid[playerX][i+1];
                    if(grid[playerX][i] == '@') {
                        playerY = i;
                        grid[playerX][i+1] = '.';
                        return;
                    }
                }
                case '>':
                    for(int i=displacement;i>=0;i--) {
                        grid[playerX][i] = grid[playerX][i-1];
                        if(grid[playerX][i] == '@') {
                            playerY = i;
                            grid[playerX][i-1] =  '.';
                            return;
                        }
                    }
                case '^':
                    for(int i=displacement;i<grid.length;i++) {
                        grid[i][playerY] = grid[i+1][playerY];
                        if(grid[i][playerY] == '@') {
                            playerX = i;
                            grid[i+1][playerY] =  '.';
                            return;
                        }
                    }
                case 'v':
                    for(int i=displacement;i>=0;i--) {
                        grid[i][playerY] = grid[i-1][playerY];
                        if(grid[i][playerY] == '@') {
                            playerX = i;

                            grid[i-1][playerY] =  '.';
                            return;
                        }
                    }
            }
        }

        int canMove(char dir) {
            switch (dir) {
                case '<':
                    for(int i=playerY-1;i>=0;i--) {
                        if(grid[playerX][i] == '#') {
                            return -1;
                        }
                        if(grid[playerX][i] == '.') {
                            return i;
                        }
                    }
                    break;
                case '>':
                    for(int i=playerY+1;i<grid[0].length;i++) {
                        if(grid[playerX][i] == '#') {
                            return -1;
                        }
                        if(grid[playerX][i] == '.') {
                            return i;
                        }
                    }
                    break;
                case '^':
                    for(int i=playerX-1;i>=0;i--) {
                        if(grid[i][playerY] == '#') {
                            return -1;
                        }
                        if(grid[i][playerY] == '.') {
                            return i;
                        }
                    }
                    break;
                case 'v':
                    for(int i=playerX+1;i<grid.length;i++) {
                        if(grid[i][playerY] == '#') {
                            return -1;
                        }
                        if(grid[i][playerY] == '.') {
                            return i;
                        }
                    }
                    break;
            }

            return -1;
        }

        void move(Point p, char dir) {
            Point next = p.next(dir);

            switch(grid[next.x][next.y]) {
                case '.':
                    grid[next.x][next.y] = grid[p.x][p.y];
                    grid[p.x][p.y] = '.';
                    return;
                case '#':
                    return;
                case 'O':
                    move(next, dir);
                    grid[next.x][next.y] = grid[p.x][p.y];
                    grid[p.x][p.y] = '.';
                    return;
                case ']':
                    move(new Point(next.x, next.y - 1), dir);
                    move(next, dir);
                    grid[next.x][next.y] = grid[p.x][p.y];
                    grid[p.x][p.y] = '.';
                    return;
                case '[':
                    move(new Point(next.x, next.y + 1), dir);
                    move(next, dir);
                    grid[next.x][next.y] = grid[p.x][p.y];
                    grid[p.x][p.y] = '.';
            }
        }

        boolean canMove(Point p, char dir) {
            Point next = p.next(dir);

            switch(grid[next.x][next.y]) {
                case '.': return true;
                case '#': return false;
                case 'O': return canMove(next, dir);
                case ']':
                    if(dir == '<' || dir == '>') {
                        return canMove(next, dir);
                    } else {
                        return canMove(new Point(next.x, next.y - 1), dir) && canMove(next, dir);
                    }
                case '[':
                    if(dir == '<' || dir == '>') {
                        return canMove(next, dir);
                    } else {
                        return canMove(new Point(next.x, next.y + 1), dir) && canMove(next, dir);
                    }
            }
            return false;
        }

        int sum() {
            int ret =0 ;
            for(int i=0;i< grid.length;i++) {
                for(int j=0;j<grid[i].length;j++) {
                    if(grid[i][j] == 'O' || grid[i][j] == '[') {
                        ret += 100 * i + j;
                    }
                }
            }
            return ret;
        }

        void expand() {
            char[][] expanded = new char[grid.length][grid[0].length*2];

            for(int i=0;i< grid.length;i++) {
                for(int j=0;j<grid[i].length;j++) {
                    switch (grid[i][j]) {
                        case '#':
                            expanded[i][j*2] = '#';
                            expanded[i][j*2 + 1] = '#';
                            break;
                        case 'O':
                            expanded[i][j*2] = '[';
                            expanded[i][j*2 + 1] = ']';
                            break;
                        case '.':
                            expanded[i][j*2] = '.';
                            expanded[i][j*2 + 1] = '.';
                            break;
                        case '@':
                            expanded[i][j*2] = '@';
                            expanded[i][j*2 + 1] = '.';
                            robot = new Point(i, j*2);
                    }
                }
            }
            this.grid = expanded;
        }

        void print() {
            for (char[] chars : grid) {
                for (char aChar : chars) {
                    System.out.print(aChar);
                }
                System.out.println();
            }

            System.out.println();
            System.out.println();
        }
    }

    static class Point {
        int x,y;

        Point(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Point next(char dir) {
            return switch (dir) {
                case '<' -> new Point(x, y - 1);
                case '>' -> new Point(x, y + 1);
                case '^' -> new Point(x - 1, y);
                case 'v' -> new Point(x + 1, y);
                default -> this;
            };
        }
    }
}
