package task1;

import java.util.concurrent.*;

public class WorkDealing {

    public static int[][] multiplyMatrix(int[][] matrixA, int[][] matrixB) throws ExecutionException, InterruptedException {

        ExecutorService executorService = Executors.newCachedThreadPool();
        int rows = matrixA.length;
        int cols = matrixB[0].length;
        int[][] result = new int[rows][cols];

        Future<?>[] futures = new Future<?>[rows];
        for (int i = 0; i < rows; i++) {
            final int row = i;
            futures[i] = executorService.submit(() -> {
                for (int j = 0; j < cols; j++) {
                    result[row][j] = 0;
                    for (int k = 0; k < matrixA[0].length; k++) {
                        result[row][j] += matrixA[row][k] * matrixB[k][j];
                    }
                }
            });
        }

        for (Future<?> future : futures) {
            future.get();
        }

        executorService.shutdown();
        return result;
    }





}
