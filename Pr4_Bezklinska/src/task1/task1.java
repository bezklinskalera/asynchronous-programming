package task1;

/*1. Напишіть програму, яка асинхронно генеруватиме двовимірний
масив розмірністю 3х3. Потім асинхронно виводитиме кожен
стовпчик цього масиву додаючи перед значеннями повідомлення
(наприклад, перший стовпець: 3, 21, 6). Далі продемонструйте роботу
методу thenRunAsync().
Результати та початковий масив потрібно виводити на екран
асинхронно.
Також після завершення кожної асинхронної задачі потрібно
виводити на екран час її виконання.*/


import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

public class task1 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // Асинхронне створення двовимірного масиву
        CompletableFuture<int[][]> generateArrayFuture = CompletableFuture.supplyAsync(() -> {
            logExecutionTime("Генерація масиву", startTime);
            int[][] array = new int[3][3];
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    array[i][j] = ThreadLocalRandom.current().nextInt(1, 100);
                }
            }
            return array;
        });

        // Асинхронний вивід кожного стовпця з повідомленням
        CompletableFuture<Void> displayColumnsFuture = generateArrayFuture.thenAcceptAsync(array -> {
            logExecutionTime("Вивід масиву", startTime);
            for (int col = 0; col < array[0].length; col++) {
                StringBuilder columnValues = new StringBuilder("Стовпець " + (col + 1) + ": ");
                for (int row = 0; row < array.length; row++) {
                    columnValues.append(array[row][col]).append(" ");
                }
                System.out.println(columnValues.toString().trim());
            }
        });

        // Асинхронне виконання фінального завдання
        displayColumnsFuture.thenRunAsync(() -> {
            logExecutionTime("Завершення всіх задач", startTime);
            System.out.println("Усі задачі виконано!");
        });

        // Затримка, щоб дочекатися завершення всіх асинхронних задач
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private static void logExecutionTime(String taskName, long startTime) {
        long elapsedTime = System.currentTimeMillis() - startTime;
        System.out.printf("%s виконано за %d мс\n", taskName, elapsedTime);
    }
}

