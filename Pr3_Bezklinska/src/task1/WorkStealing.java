package task1;

import java.util.concurrent.*;

// Клас WorkStealing успадковується від RecursiveTask
public class WorkStealing  extends RecursiveTask<int[][]> {

    private final int[][] matrixA;
    private final int[][] matrixB;
    private final int[][] result;
    private final int rowStart, rowEnd; // Межі рядків для обробки

    public WorkStealing(int[][] matrixA, int[][] matrixB, int[][] result, int rowStart, int rowEnd) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = result;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
    }

    // Метод, який виконує задачу або розділяє її на підзадачі
    @Override
    protected int[][] compute() {
        // Якщо кількість рядків у поточному підзадачі менша 10, обчислюємо результат
        if (rowEnd - rowStart <= 10) {
            for (int i = rowStart; i < rowEnd; i++) {
                for (int j = 0; j < matrixB[0].length; j++) {
                    result[i][j] = 0;
                    for (int k = 0; k < matrixA[0].length; k++) {
                        result[i][j] += matrixA[i][k] * matrixB[k][j];
                    }
                }
            }
        } else {
            // Якщо кількість рядків більша за поріг, розділяємо задачу на дві підзадачі
            int mid = (rowStart + rowEnd) / 2;
            WorkStealing task1 = new WorkStealing(matrixA, matrixB, result, rowStart, mid);
            WorkStealing task2 = new WorkStealing(matrixA, matrixB, result, mid, rowEnd);
            // Запускаємо обидві підзадачі паралельно
            invokeAll(task1, task2);
        }
        return result;
    }

    // Статичний метод для початку множення матриць
    public static int[][] multiplyMatrix(int[][] matrixA, int[][] matrixB) {
        // Кількість рядків у матриці A
        int rows = matrixA.length;
        int[][] result = new int[rows][matrixB[0].length];
        // Створюємо пул ForkJoin для роботи з підзадачами
        ForkJoinPool pool = new ForkJoinPool();
        // Запускаємо головну задачу, яка обробляє всі рядки
        pool.invoke(new WorkStealing(matrixA, matrixB, result, 0, rows));
        return result;
    }
}
