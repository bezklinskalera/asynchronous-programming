import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.concurrent.*;

//код є асинхронним багатопотоковим. У ньому кілька завдань (обробка кожного масиву) виконуються в кількох потоках.
// Для обробки масивів використовується пул потоків, де кожен масив ділиться на підчастини, оброблювані окремими підзадачами в різних потоках.

/*Напишіть Callable, який приймає число N і знаходить всі прості
числа до N. Створіть кілька потоків, кожен з яких обробляє свій
діапазон. Використовуйте Future, щоб зібрати результати обчислень з
усіх потоків.
Діапазон [0; 1000] – цілі числа, число N задає користувач.
Використати CopyOnWriteArrayList.*/

public class Pr2 {

    public static void main(String[] args) throws InterruptedException {
        // запитуємо користувача необхідні параметри
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть кількість масивів: ");
        int numArrays = scanner.nextInt();
        System.out.print("Введіть мінімальне значення діапазону: ");
        int minRange = scanner.nextInt();
        System.out.print("Введіть максимальне значення діапазону: ");
        int maxRange = scanner.nextInt();
        System.out.print("Введіть кількість елементів масиву (від 40 до 60): ");
        int arraySize = scanner.nextInt();
        System.out.print("N (числа в списку не повинні бути більше за N): ");
        int N = scanner.nextInt();
        scanner.close();

        //запис часу початку виконання програми
        long startTime = System.currentTimeMillis();

        // створюємо основний пул потоків для обробки кожного масиву
        ExecutorService mainExecutorService = Executors.newFixedThreadPool(numArrays);
        // створюємо потокобезпечний список для зберігання простих чисел зі всіх масивіів
        CopyOnWriteArrayList<Integer> allPrimes = new CopyOnWriteArrayList<>();

        // список  для зберігання завдань (Future), які обробляють кожний масив
        List<Future<List<Integer>>> arrayFutures = new ArrayList<>();

        // основний цикл для обробки кожного масиву
        for (int i = 0; i < numArrays; i++) {
            // генеруємо масив випадкових чисел у заданому діапазоні
            int[] array = new Random().ints(arraySize, minRange, maxRange + 1).toArray();

            // виводимо згенерований масив
            System.out.printf("Згенерований масив %d (потік %s): ", i + 1, Thread.currentThread().getName());
            for (int num : array) {
                System.out.print(num + " ");
            }
            System.out.println();

            // завдання (Callable) для обробки масиву.
            final int arrayIndex = i; // зберігаємо індекс масиву для виводу
            // Callable - це інтерфейс, що дозволяє виконувати завдання в окремому потоці і повертати результат.
            Callable<List<Integer>> arrayTask = () -> {
                // створюємо список для зберігання простих чисел у цьому масиві
                List<Integer> primes = new ArrayList<>();

                // створюємо пул потоків для обробки піддіапазонів в масивів
                ExecutorService partExecutor = Executors.newFixedThreadPool(3);
                // список для зберігання завдань (Future), які обробляють кожну частину масиву.
                List<Future<List<Integer>>> partFutures = new ArrayList<>();

                // розділення масиву на 3 частини
                //Math.ceil — це метод в Java, який округлює дробове число до найближчого більшого цілого.
                int partSize = (int) Math.ceil((double) array.length / 3);
                for (int j = 0; j < 3; j++) {
                    int start = j * partSize;
                    int end = Math.min(start + partSize, array.length);
                    if (start >= array.length) break; // перевірка на вихід за межі масиву
                    //Метод java.util.Arrays.copyOfRange(array, start, end) використовується для створення нового масиву, який є копією частини оригінального масиву.
                    int[] partArray = java.util.Arrays.copyOfRange(array, start, end);

                    // завдання (Callable) для обробки піддіапазону
                    final int partIndex = j; // Зберігаємо індекс частини для виводу

                    Callable<List<Integer>> partTask = () -> {
                        // створюємо список для зберігання простих чисел у цьому піддіапазоні
                        List<Integer> partPrimes = new ArrayList<>();
                        System.out.printf("Обробка частини %d масиву %d (потік %s): %s\n", partIndex + 1, arrayIndex + 1, Thread.currentThread().getName(), java.util.Arrays.toString(partArray)); // Виводимо дані піддіапазону
                        for (int num : partArray) {
                            if (checkSimple(num,N)) {
                                partPrimes.add(num);
                            }
                        }
                        return partPrimes;
                    };
                    // `submit` метод submit() є частиною інтерфейсу ExecutorService.
                    // Він приймає завдання, яке реалізується за допомогою інтерфейсу Callable, і передає це завдання в пул потоків для виконання.
                    //  додає отриманий об'єкт Future, що повертається методом submit(), у список partFutures
                    partFutures.add(partExecutor.submit(partTask));
                }

                // збирання результатів підпотоків
                for (Future<List<Integer>> future : partFutures) {
                    try {
                        if (future.isCancelled()) {
                            System.err.println("Частина завдання була скасована.");
                        } else {
                            //метод future.get() блокує потік до того часу, поки завдання, яке пов'язане з об'єктом Future, не буде завершене.
                            List<Integer> result = future.get();
                            primes.addAll(result);
                        }
                    } catch (CancellationException e) {
                        System.err.println("Завдання було скасовано.");
                    } catch (ExecutionException e) {
                        System.err.println("Виникла помилка при виконанні задачі: " + e.getCause());
                    }
                }
                partExecutor.shutdown();
                return primes;
            };
            // додаємо завдання для обробки масиву в пул потоків
            arrayFutures.add(mainExecutorService.submit(arrayTask));
        }

        // Future дозволяє отримати результат, перевірити стан (виконано чи ні), та скасувати завдання.
        // збирання результатів з усіх основних потоків
        for (Future<List<Integer>> future : arrayFutures) {
            try {
                if (future.isCancelled()) {
                    System.err.println("Завдання було скасовано.");
                } else {
                    List<Integer> result = future.get();
                    // додаємо всі знайдені прості числа до загального списку
                    allPrimes.addAll(result);
                }
            } catch (CancellationException e) {
                System.err.println("Завдання було скасовано.");
            } catch (ExecutionException e) {
                System.err.println("Виникла помилка при виконанні задачі: " + e.getCause());
            }
        }

        mainExecutorService.shutdown();
        long endTime = System.currentTimeMillis();

        System.out.println("Прості числа: " + allPrimes);
        System.out.println("Час роботи програми: " + (endTime - startTime) + " мс");
    }

    // Метод для перевірки, чи є число простим
    public static boolean checkSimple(int i, int N) {
        if (i > N) return false; // Число не повинно бути більшим за N
        if (i <= 1) return false; // Число має бути більше 1
        if (i <= 3) return true; // 2 і 3 - прості числа
        if (i % 2 == 0 || i % 3 == 0) return false; // Перевірка на кратність 2 і 3
        int n = 5;
        while (n * n <= i) { // Перевірка інших дільників
            if (i % n == 0 || i % (n + 2) == 0) return false;
            n += 6;
        }
        return true;
    }

}
