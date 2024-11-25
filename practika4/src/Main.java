import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class Main {
    public static void main(String[] args) {
        int rows = 3;
        int cols = 3;

        // runAsync: повідомляє про запуск
        CompletableFuture<Void> startTask = CompletableFuture.runAsync(() ->
                System.out.println("Початок роботи")
        );

        // supplyAsync: створює першу матрицю
        CompletableFuture<int[][]> matrixATask = CompletableFuture.supplyAsync(() -> {
            int[][] matrixA = generateMatrix(rows, cols);
            System.out.println("Перша матриця:");
            printMatrix(matrixA);
            return matrixA;
        });

        // thenApplyAsync: створює другу матрицю після завершення першої
        CompletableFuture<int[][]> matrixBTask = matrixATask.thenApplyAsync(matrixA -> {
            int[][] matrixB = generateMatrix(cols, rows);
            System.out.println("Друга матриця:");
            printMatrix(matrixB);
            return matrixB;
        });

        // thenCombineAsync: виконує множення матриць після створення обох
        CompletableFuture<int[][]> resultTask = matrixATask.thenCombineAsync(matrixBTask, (matrixA, matrixB) -> {
            System.out.println("Множення матриць...");
            return multiplyMatrices(matrixA, matrixB);
        });

        // thenAcceptAsync: виводить результат
        CompletableFuture<Void> printResultTask = resultTask.thenAcceptAsync(result -> {
            System.out.println("Результат множення матриць:");
            printMatrix(result);
        });

        // thenRunAsync: завершує процес
        printResultTask.thenRunAsync(() ->
                System.out.println("Задачі завершено.")
        ).join(); // Очікуємо завершення всіх задач
    }

    // Метод для генерації випадкової матриці
    private static int[][] generateMatrix(int rows, int cols) {
        int[][] matrix = new int[rows][cols];
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                matrix[i][j] = random.nextInt(1, 11);
            }
        }
        return matrix;
    }

    // Метод для множення двох матриць
    private static int[][] multiplyMatrices(int[][] matrixA, int[][] matrixB) {
        int rowsA = matrixA.length;
        int colsA = matrixA[0].length;
        int colsB = matrixB[0].length;
        int[][] result = new int[rowsA][colsB];

        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    result[i][j] += matrixA[i][k] * matrixB[k][j];
                }
            }
        }
        return result;
    }

    // Метод для виводу матриці
    private static void printMatrix(int[][] matrix) {
        for (int[] row : matrix) {
            for (int element : row) {
                System.out.print(element + "\t");
            }
            System.out.println();
        }
        System.out.println();
    }
}