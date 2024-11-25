package task1;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);

        // Введення розмірів для першої матриці
        System.out.print("Введіть кількість стопчиків (перша матриця): ");
        int numColumn = scanner.nextInt();
        System.out.print("Введіть кількість рядочків (перша матриця): ");
        int numRow = scanner.nextInt();

        // Введення розмірів для другої матриці
        System.out.print("Введіть кількість стопчиків (друга матриця): ");
        int numColumn2 = scanner.nextInt();
        System.out.print("Введіть кількість рядочків (друга матриця): ");
        int numRow2 = scanner.nextInt();

        // Введення діапазону значень для генерації матриць
        System.out.print("Введіть початкове значення діапазону: ");
        int startNumber = scanner.nextInt();
        System.out.print("Введіть кінцеве значення діапазону: ");
        int endNumber = scanner.nextInt();
        scanner.close();

        // Генерація матриць за заданими параметрами
        int[][] matrixA = WorkWithMatrix.generateMatrix(numRow, numColumn, startNumber, endNumber);
        int[][] matrixB = WorkWithMatrix.generateMatrix(numRow2, numColumn2, startNumber, endNumber);

        // Перевірка можливості множення матриць
        if (!WorkWithMatrix.canMultiply(matrixA, matrixB)) {
            System.out.println("Множення матриць неможливе!");
            return;
        }

        // Виведення згенерованих матриць
        System.out.println("Матриця A:");
        WorkWithMatrix.printMatrix(matrixA);
        System.out.println("Матриця B:");
        WorkWithMatrix.printMatrix(matrixB);

        // Множення матриць за допомогою методу "WorkDealing"
        long startWorkDealing = System.currentTimeMillis();
        int[][] resultWorkDealing = WorkDealing.multiplyMatrix(matrixA,matrixB);
        System.out.println("\nWorkDealing помножена матриця:");
        WorkWithMatrix.printMatrix(resultWorkDealing);
        System.out.println("Execution time WorkDealing = " + (System.currentTimeMillis() - startWorkDealing));

        // Множення матриць за допомогою методу "WorkStealing"
        long startWorkStealing = System.currentTimeMillis();
        int[][] resultWorkStealing = WorkStealing.multiplyMatrix(matrixA,matrixB);
        System.out.println("\nWorkStealing помножена матриця:");
        WorkWithMatrix.printMatrix(resultWorkStealing);
        System.out.println("Execution time WorkStealing = " + (System.currentTimeMillis() - startWorkStealing));


    }
}
