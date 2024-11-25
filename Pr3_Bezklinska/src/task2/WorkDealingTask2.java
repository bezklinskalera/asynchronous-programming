package task2;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/*WorkDealingTask:

Це підхід, що використовує пул потоків для виконання паралельних задач.
Для кожного файлу або підкаталогу створюється окрема задача, яка перевіряє, чи містить ім'я файлу ключове слово.
Під час виконання задачі для кожної директорії рекурсивно запускаються нові потоки для перевірки вмісту.*/

// Клас для пошуку файлів з використанням ExecutorService (підхід WorkDealing).
class WorkDealingTask {

    // Метод для підрахунку файлів у директорії, які містять задане слово.
    public static int countFiles(File directory, String keyword) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        // Лічильник для кількості знайдених файлів.
        int count = 0;
        // Отримуємо список файлів у директорії.
        File[] files = directory.listFiles();
        // Якщо список файлів порожній або директорія пуста, повертаємо 0.
        if (files == null) return 0;

        // Масив для зберігання майбутніх об'єктів (для асинхронних задач)
        Future<Integer>[] futures = new Future[files.length];
        // Лічильник для індексу в масиві futures.
        int index = 0;

        // Перебираємо всі файли в директорії.
        for (File file : files) {
            // Якщо це директорія, рекурсивно викликаємо countFiles для цієї директорії.
            if (file.isDirectory()) {
                futures[index++] = executor.submit(() -> countFiles(file, keyword));
            } else {
                // Якщо це файл, перевіряємо, чи містить його ім'я ключове слово.
                futures[index++] = executor.submit(() -> file.getName().contains(keyword) ? 1 : 0);
            }
        }

        // Чекаємо на завершення всіх задач і додаємо їх результати до лічильника.
        for (Future<Integer> future : futures) {
            if (future != null) count += future.get();
        }

        executor.shutdown();
        return count;
    }
}
