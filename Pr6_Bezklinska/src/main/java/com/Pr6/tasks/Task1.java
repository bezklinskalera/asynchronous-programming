package com.Pr6.tasks;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
@Component
public class Task1 {

    // шлях до файлу
    private static final String FILE_PATH = "D:\\КПІ\\асинхронне програмування\\data_Pr6\\1.txt";
    private int counter = 0;

    /*
      конструктор класу, який очищує файл під час запуску.
     */
    public Task1() {
        try {
            Files.write(Paths.get(FILE_PATH), new byte[0], StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING);
            System.out.println("File cleared at startup.");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /*
     метод updateFile виконується кожні 10 секунд завдяки @Scheduled(fixedRate = 10000).
     метод оновлює файл значенням лічильника, додаючи його до кінця файлу.
     */
    @Scheduled(fixedRate = 10000)
    public void updateFile() {
        counter += 5;
        String data = "Counter: " + counter + "\n";

        try {
            Files.write(Paths.get(FILE_PATH), data.getBytes(), StandardOpenOption.CREATE, StandardOpenOption.APPEND);
            System.out.println("Updated file with: " + data.trim());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
