package task1;

import java.util.Random;

public class WorkWithMatrix {
    // Функція для генерації матриці
    public static int[][] generateMatrix(int rows, int cols, int start, int end) {
        Random random = new Random();
        int[][] matrix = new int[rows][cols];
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(end - start + 1) + start;
            }
        }
        return matrix;
    }

    // Функція для виведення матриці
    public static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int value : row) {
                System.out.print(value + " ");
            }
            System.out.println();
        }
    }

    // Функція для перевірки можливості множення матриць
    public static boolean canMultiply(int[][] matrixA, int[][] matrixB) {
        return matrixA[0].length == matrixB.length;
    }
}
