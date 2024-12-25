package task2;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.stream.IntStream;

/*Напишіть програму, в якій асинхронно виконайте усі необхідні дії.
Ввести послідовність дійсних чисел (a1, a2, a3, ... an
) та обчислити
min(a1, a3, a5, ... ) + max(a2, a4, a6, ... ). Початкову послідовність
генерувати рандомно, кількість елементів = 20.
Початкову послідовність та результат вивести на екран.
До кожного виводу додавати відповідне інформаційне повідомлення.
В кінці вивести час роботи усіх асинхронних операцій.*/

public class task2 {
    public static void main(String[] args) {
        long startTime = System.currentTimeMillis();

        // генеруємо послідовності дійсних чисел

        /*supplyAsync використовується для виконання асинхронного завдання, яке повертає результат*/

        CompletableFuture<double[]> generateArray = CompletableFuture.supplyAsync(() -> {
            System.err.println("Генерація...");
            Random random = new Random();
            double[] array = new double[20];
            for (int i = 0; i < 20; i++) {
                array[i] = 1 + (9 * random.nextDouble());
            }
            System.err.println("Послідовність згенеровано.");
            return array;
        });

        // обчислення виразу min(a1, a3, a5, ...) + max(a2, a4, a6, ...)

        /*thenApplyAsync використовується для асинхронного обчислення на основі результату попереднього завдання (generateArray).*/

        CompletableFuture<Double> calculateExpression = generateArray.thenApplyAsync(array -> {
            System.err.println("Обчислення...");
            System.err.println("Послідовность:");
            //Arrays.stream(array) створює стрім (потік) із елементів масиву array.
            //mapToObj — перетворює кожен елемент потоку на інший об’єкт
            System.out.println(Arrays.toString(Arrays.stream(array).mapToObj(value -> String.format("%.1f", value)).toArray()));

            //обчислення мінімального значення серед непарних елементів масиву
            /* IntStream.range(0, array.length) створює потік цілих чисел від 0 до array.length - 1* (ІНДЕКСИ ВІД 0 ДО 19)*/
            double minOddIndexes = IntStream.range(0, array.length)
                    .filter(i -> i % 2 != 0) // непарні індекси (a1, a3, ...)
                    //отримання значень з масиву, відповідних до відфільтрованих індексів
                    .mapToDouble(i -> array[i])
                    //знаходимо мінімальне серед них. Якщо потік порожній, повертається 0.0.
                    .min().orElse(0.0);

            //обчислення максимально значення серед парних елементів масиву
            double maxEvenIndexes = IntStream.range(0, array.length)
                    .filter(i -> i % 2 == 0) // парні індекси (a2, a4, ...)
                    .mapToDouble(i -> array[i])
                    .max().orElse(0.0);

            System.out.println(String.format("Мінімальне значення для непарних індексів: %.1f", minOddIndexes));
            System.out.println(String.format("Максимальне значення для парних індексів: %.1f", maxEvenIndexes));

            double result = minOddIndexes + maxEvenIndexes;
            return result;
        });

        // асинхронний вивід результату обчислення

        /*thenAcceptAsync використовується для асинхронного виведення результату обчислення.*/

        CompletableFuture<Void> resultFuture = calculateExpression.thenAcceptAsync(result -> {
            System.out.printf("Результат: %.1f%n", result);
        });

        /*CompletableFuture.allOf чекає завершення всіх асинхронних завдань.*/

        /*thenRunAsync запускає завдання після завершення попередніх.*/

        CompletableFuture<Void> finalTask = CompletableFuture.allOf(resultFuture)
                .thenRunAsync(() -> {
                    long time = System.currentTimeMillis() - startTime;
                    System.out.printf("Час виконання всіх задач: %d мс%n", time);
                });

        // очікування завершення всіх асинхронних операцій
        finalTask.join();
    }}