package task1;

import java.util.Scanner;
import java.util.concurrent.ExecutionException;

public class main {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Введіть кількість стопчиків (перша матриця): ");
        int numColumn = scanner.nextInt();
        System.out.print("Введіть кількість рядочків (перша матриця): ");
        int numRow = scanner.nextInt();
        System.out.print("Введіть кількість стопчиків (друга матриця): ");
        int numColumn2 = scanner.nextInt();
        System.out.print("Введіть кількість рядочків (друга матриця): ");
        int numRow2 = scanner.nextInt();
        System.out.print("Введіть початкове значення діапазону: ");
        int startNumber = scanner.nextInt();
        System.out.print("Введіть кінцеве значення діапазону: ");
        int endNumber = scanner.nextInt();
        scanner.close();

        int[][] matrixA = WorkWithMatrix.generateMatrix(numRow, numColumn, startNumber, endNumber);
        int[][] matrixB = WorkWithMatrix.generateMatrix(numRow2, numColumn2, startNumber, endNumber);

        if (!WorkWithMatrix.canMultiply(matrixA, matrixB)) {
            System.out.println("Множення матриць неможливе!");
            return;
        }

        System.out.println("Матриця A:");
        WorkWithMatrix.printMatrix(matrixA);
        System.out.println("Матриця B:");
        WorkWithMatrix.printMatrix(matrixB);

        long startWorkDealing = System.currentTimeMillis();
        int[][] resultWorkDealing = WorkDealing.multiplyMatrix(matrixA,matrixB);
        System.out.println("\nWorkDealing помножена матриця:");
        WorkWithMatrix.printMatrix(resultWorkDealing);
        System.out.println("Execution time WorkDealing = " + (System.currentTimeMillis() - startWorkDealing));

        long startWorkStealing = System.currentTimeMillis();
        int[][] resultWorkStealing = WorkStealing.multiplyMatrix(matrixA,matrixB);
        System.out.println("\nWorkStealing помножена матриця:");
        WorkWithMatrix.printMatrix(resultWorkStealing);
        System.out.println("Execution time WorkStealing = " + (System.currentTimeMillis() - startWorkStealing));


    }
}
