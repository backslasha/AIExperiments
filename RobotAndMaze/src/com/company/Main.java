package com.company;

public class Main {

    public static void main(String[] args) {
        Maze.terrain =
                        "❤ □ ■ □ □ □  \n" +
                        "□ □ ■ □ ■ ■ \n" +
                        "□ ■ ■ □ □ □ \n" +
                        "■ □ ■ ■ □ ■ \n" +
                        "■ ■ □ ■ □ ■ \n" +
                        "■ ■ □ ■ □ □ \n" +
                        "■ ■ □ □ ■ □  \n" +
                        "❤ ■ □ □ ❤ ✫";
        Maze maze = new Maze(8, 6);
        maze.printGraph();
        maze.printSolution(AStar.searchSolution(maze));
    }
}