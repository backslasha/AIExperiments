package com.company;

public class Main {
    public static void main(String[] args) {
        Maze.terrain =
                        "■ ■ ■ ■ ■ ■ ■ \n" +
                        "■ □ ■ □ ■ □ ❤ \n" +
                        "■ □ □ □ ■ □ ■ \n" +
                        "■ ■ ■ ■ □ ■ ■ \n" +
                        "■ □ □ □ □ ■ ■ \n" +
                        "✫ □ ■ □ □ □ ■ \n" +
                        "■ ■ ■ ■ ■ ■ ■ ";
        Maze maze = new Maze(7, 7);
        System.out.println("MAZE:");
        maze.printGraph();
        System.out.println("SOLUTION:");
        maze.printSolution(AStar.searchSolution(maze));

    }

}