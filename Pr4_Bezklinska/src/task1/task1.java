package task1;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ThreadLocalRandom;

/*Напишіть програму, яка асинхронно генеруватиме двовимірний
масив розмірністю 3х3. Потім асинхронно виводитиме кожен
стовпчик цього масиву додаючи перед значеннями повідомлення
(наприклад, перший стовпець: 3, 21, 6). Далі продемонструйте роботу
методу thenRunAsync().
Результати та початковий масив потрібно виводити на екран
асинхронно.
Також після завершення кожної асинхронної задачі потрібно виводити
на екран час її виконання.*/

public class task1 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // створення двовимірного масиву

        /*supplyAsync використовується для виконання асинхронного завдання, яке повертає результат*/

        CompletableFuture<int[][]> generateArray = CompletableFuture.supplyAsync(() -> {
            long startTime1 = System.currentTimeMillis();
            int[][] array = new int[3][3];
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {

                    /*ThreadLocalRandom є класом, який дозволяє створювати випадкові числа в багатопотоковому середовищі
                    без небезпечної синхронізації.*/

                    /*ThreadLocalRandom.current() повертає поточний об'єкт випадкових чисел для поточного потоку виконання.*/

                    array[i][j] = ThreadLocalRandom.current().nextInt(1, 100);
                }
            }
            System.out.printf("Генерація масиву виконана за %d мс%n", System.currentTimeMillis() - startTime1);
            long startTime2 = System.currentTimeMillis();
            for (int i = 0; i < array.length; i++) {
                for (int j = 0; j < array[i].length; j++) {
                    System.out.print(array[i][j] + " ");
                }
                System.out.println();
            }
            System.out.printf("Вивід масиву виконано за %d мс%n", System.currentTimeMillis() - startTime2);
            return array;
        });

        //виконання виведення стовпців

        /* thenAcceptAsync використовується для виконання дій з результатом (Consumer).*/

        /* це метод, який приймає обробку (array) після завершення попереднього майбутнього (generateArray)*/

        CompletableFuture<Void> displayColumns = generateArray.thenAcceptAsync(array -> {
            long startTime3 = System.currentTimeMillis();
            for (int col = 0; col < array[0].length; col++) {
                /*Створюється StringBuilder, який використовуватиметься для створення рядка з чисел для кожного стовпця.*/
                StringBuilder columnValues = new StringBuilder("Стовпець " + (col + 1) + ": ");
                for (int row = 0; row < array.length; row++) {
                    columnValues.append(array[row][col]).append(" ");
                }
                System.out.println(columnValues.toString().trim());
            }
            System.out.printf("Вивід стовпців виконано за %d мс%n", System.currentTimeMillis() - startTime3);
        });

        // виконання фінального завдання

        /*thenRunAsync використовується для виконання дій без урахування результату (Runnable).*/

        CompletableFuture<Void> finalTaskFuture = displayColumns.thenRunAsync(() -> {
            long time = System.currentTimeMillis() - startTime;
            System.out.printf("Час виконання всіх задач: %d мс%n", time);
        });

        // очікування завершення всіх асинхронних операцій
        finalTaskFuture.join();
    }

}



