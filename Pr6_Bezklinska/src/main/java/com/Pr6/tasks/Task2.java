package com.Pr6.tasks;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

@Component
public class Task2 implements CommandLineRunner {

    @Override
    public void run(String... args) {
        // scheduler, який виконує завдання в одному потоці
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

        // запуск завдання через 5 секунд
        scheduler.schedule(() -> {
            System.out.println("5 секунд від запуску програми");
            // завершення роботи
            scheduler.shutdown();
        }, 5, TimeUnit.SECONDS);
    }
}


