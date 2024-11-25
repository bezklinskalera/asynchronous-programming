package task1;

import java.util.concurrent.*;

public class WorkDealing {

    // Метод для множення матриць за допомогою пулу потоків
    //кожен потік обробляє один рядок матриці A
    public static int[][] multiplyMatrix(int[][] matrixA, int[][] matrixB) throws ExecutionException, InterruptedException {

        // Ініціалізуємо пул потоків
        ExecutorService executorService = Executors.newCachedThreadPool();
        // Кількість рядків у першій матриці
        int rows = matrixA.length;
        // Кількість стовпців у другій матриці
        int cols = matrixB[0].length;
        int[][] result = new int[rows][cols];

        /* використовується для створення масиву об'єктів Future<?> розміром rows.
        Створюється масив об'єктів Future<?> для зберігання результатів завдань (по одному для кожного рядка матриці A).*/
        Future<?>[] futures = new Future<?>[rows];
        //Цикл для створення задач, які обробляють кожен рядок матриці A
        for (int i = 0; i < rows; i++) {
            final int row = i;
            // Передаємо задачу в пул потоків для виконання
            futures[i] = executorService.submit(() -> {
                for (int j = 0; j < cols; j++) {
                    result[row][j] = 0;
                    // Обчислюємо значення елемента result[row][j] як суму добутків елементів
                    // рядка row матриці A та стовпця j матриці B
                    for (int k = 0; k < matrixA[0].length; k++) {
                        result[row][j] += matrixA[row][k] * matrixB[k][j];
                    }
                }
            });
        }

        // Очікуємо завершення всіх задач
        for (Future<?> future : futures) {
            // Блокує виконання до завершення кожної задачі
            future.get();
        }

        executorService.shutdown();
        return result;
    }





}
