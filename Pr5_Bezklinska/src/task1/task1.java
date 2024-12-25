package task1;

/*1. Створіть кілька асинхронних завдань, які виконуються паралельно.
Виведіть результат першого завершеного успішно завдання.*/

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/*КОД ЧАСТКОВО ВЗЯТИЙ З ЛЕКЦІЇ*/
public class task1 {
    static void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    static void executionThread(){
        System.out.println("Thread execution - " + Thread.currentThread().getName());
    }

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        CompletableFuture<String> completableFuture1 = CompletableFuture.supplyAsync(() -> {
            sleep(1000);
            String stringToPrint = "Task 1";
            System.out.println("----\nExecute - " + stringToPrint);
            executionThread();
            return stringToPrint;
        });

        CompletableFuture<String> completableFuture2 = CompletableFuture.supplyAsync(() -> {
            sleep(2000);
            String stringToPrint = "Task 2";
            System.out.println("----\nExecute - " + stringToPrint);
            executionThread();
            return stringToPrint;
        });

        CompletableFuture<String> completableFuture3 = CompletableFuture.supplyAsync(() -> {
            sleep(500);
            String stringToPrint = "Task 3";
            System.out.println("----\nExecute - " + stringToPrint);
            executionThread();
            return stringToPrint;
        });

        CompletableFuture<Object> resultantCf = CompletableFuture.anyOf(completableFuture1, completableFuture2, completableFuture3);

        System.err.println("Final Result - " + resultantCf.get());
        sleep(2500);
    }
}
