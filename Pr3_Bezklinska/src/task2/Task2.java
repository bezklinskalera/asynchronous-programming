package task2;

import java.io.File;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;

//D:\КПІ\асинхронне програмування\directory

public class Task2 {

    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть потрібну директорію: ");
        String directory = scanner.nextLine();
        System.out.print("Введіть пошукове слово: ");
        String searchWord = scanner.nextLine();
        scanner.close();

        // Створюємо об'єкт для роботи з файловою системою, за вказаним шляхом.
        File directoryPath = new File(directory);
        if (!directoryPath.isDirectory()) {
            System.out.println("Помилка: вказаний шлях не є директорією.");
            return;
        }

        // Вимірюємо час виконання для першого підходу (WorkDealing).
        long startWorkDealing = System.currentTimeMillis();
        int resultWorkDealing = WorkDealingTask.countFiles(directoryPath,searchWord);
        System.out.println("\nWorkDealing кількість знайдених файлів:" + resultWorkDealing);
        System.out.println("Execution time WorkDealing = " + (System.currentTimeMillis() - startWorkDealing));

        // Вимірюємо час виконання для другого підходу (WorkStealing).
        long startWorkStealing = System.currentTimeMillis();
        // Створюємо ForkJoinPool для паралельної обробки.
        ForkJoinPool pool = new ForkJoinPool();
        int resultWorkStealing = pool.invoke(new WorkStealingTask(directoryPath,searchWord));
        System.out.println("\nWork Stealing кількість знайдених файлів: " + resultWorkStealing);
        System.out.println("Execution time WorkStealing = " + (System.currentTimeMillis() - startWorkStealing));
    }
}
