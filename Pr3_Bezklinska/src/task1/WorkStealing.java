package task1;

import java.util.concurrent.*;

public class WorkStealing  extends RecursiveTask<int[][]> {

    private final int[][] matrixA;
    private final int[][] matrixB;
    private final int[][] result;
    private final int rowStart, rowEnd;

    public WorkStealing(int[][] matrixA, int[][] matrixB, int[][] result, int rowStart, int rowEnd) {
        this.matrixA = matrixA;
        this.matrixB = matrixB;
        this.result = result;
        this.rowStart = rowStart;
        this.rowEnd = rowEnd;
    }

    @Override
    protected int[][] compute() {
        if (rowEnd - rowStart <= 10) {
            for (int i = rowStart; i < rowEnd; i++) {
                for (int j = 0; j < matrixB[0].length; j++) {
                    result[i][j] = 0;
                    for (int k = 0; k < matrixA[0].length; k++) {
                        result[i][j] += matrixA[i][k] * matrixB[k][j];
                    }
                }
            }
        } else {
            int mid = (rowStart + rowEnd) / 2;
            WorkStealing task1 = new WorkStealing(matrixA, matrixB, result, rowStart, mid);
            WorkStealing task2 = new WorkStealing(matrixA, matrixB, result, mid, rowEnd);
            invokeAll(task1, task2);
        }
        return result;
    }

    public static int[][] multiplyMatrix(int[][] matrixA, int[][] matrixB) {
        int rows = matrixA.length;
        int[][] result = new int[rows][matrixB[0].length];
        ForkJoinPool pool = new ForkJoinPool();
        pool.invoke(new WorkStealing(matrixA, matrixB, result, 0, rows));
        return result;
    }
}
