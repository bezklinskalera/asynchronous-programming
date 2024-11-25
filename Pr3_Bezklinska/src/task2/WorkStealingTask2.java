package task2;

import java.io.File;
import java.util.concurrent.RecursiveTask;

/*WorkStealingTask:

Це підхід, що використовує ForkJoinPool і RecursiveTask для рекурсивної обробки файлів.
Якщо потік працює над великою кількістю файлів, завдання розбивається на підзадачі, які можуть бути виконані іншими потоками.
Завдання виконується рекурсивно для кожної підкаталогу, а результати об'єднуються після виконання підзадач.*/

class WorkStealingTask extends RecursiveTask<Integer> {
    private final File directory;
    private final String keyword;

    // Клас для пошуку файлів з використанням ForkJoinTask (підхід WorkStealing).
    public WorkStealingTask(File directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword;
    }

    @Override
    protected Integer compute() {
        // Лічильник знайдених файлів.
        int count = 0;
        // Отримуємо список файлів у директорії.
        File[] files = directory.listFiles();
        if (files == null) return 0;

        // Перебираємо всі файли в директорії.
        for (File file : files) {
            if (file.isDirectory()) {
                // Якщо це директорія, створюємо нове завдання для пошуку в цій директорії.
                WorkStealingTask task = new WorkStealingTask(file, keyword);
                // Запускаємо підзадачу для директорії.
                /*Метод fork() запускає задачу асинхронно.
                Метод join() використовується для очікування завершення завдання, запущеного через fork(), і отримання результату.*/
                task.fork();
                // Чекаємо на результат і додаємо до загального підсумку.
                count += task.join();
            } else if (file.getName().contains(keyword)) {
                // Якщо це файл і його ім'я містить ключове слово, збільшуємо лічильник.
                count++;
            }
        }
        return count;
    }
}