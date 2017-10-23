package com.company;

import java.util.LinkedList;

public class Maze {
    public static String terrain;
    private int cols, rows;
    private Point start, end;

    public Point[][] getPoints() {
        return points;
    }

    private Point[][] points;

    public Point getStart() {
        return start;
    }

    public Point getEnd() {
        return end;
    }

    public Maze(int rows,int cols) {
        this.cols = cols;
        this.rows = rows;
        points = new Point[rows][cols];
        initGraph(points, getTerrain());
    }

    public void printGraph() {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {

                if (points[i][j].equals(end)) {
                    System.out.printf("❤ ");
                } else if (points[i][j].equals(start)) {
                    System.out.printf("✫ ");
                } else if (points[i][j].isObstacle()) {
                    System.out.printf("■ ");
                } else {
                    System.out.printf("□ ");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    public void printSolution(LinkedList<Point> solution) {
        if (solution == null) {
            System.out.println("no solution!");
            return;
        }
        for (int i = 0; i < points.length; i++) {
            for (int j = 0; j < points[i].length; j++) {
                if (points[i][j].equals(start)) {
                    System.out.printf("✫ ");
                } else if (points[i][j].equals(end)) {
                    System.out.printf("❤ ");
                } else if (solution.contains(points[i][j])) {
                    System.out.printf("✫ ");
                } else if (points[i][j].isObstacle()) {
                    System.out.printf("■ ");
                } else {
                    System.out.printf("□ ");
                }
            }
            System.out.println();
        }
    }

    private void initGraph(Point[][] points, char[][] obstacleRef) {
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                points[i][j] = new Point(
                        i, j,
                        obstacleRef[i][j] == '■'
                );
                if (obstacleRef[i][j] == '✫') {
                    start = points[i][j];
                }
                if (obstacleRef[i][j] == '❤') {
                    end = points[i][j];
                }
            }
        }
    }

    private char[][] getTerrain() {
        final char[][] terrainChars = new char[rows][cols];
        terrain = terrain.replaceAll("\\s", "");
        int k = 0;
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                terrainChars[i][j] = terrain.charAt(k++);
            }
        }
        return terrainChars;
    }

}
