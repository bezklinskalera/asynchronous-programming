package task2;

import java.io.File;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

class WorkDealingTask {
    public static int countFiles(File directory, String keyword) throws InterruptedException, ExecutionException {
        ExecutorService executor = Executors.newCachedThreadPool();
        int count = 0;
        File[] files = directory.listFiles();
        if (files == null) return 0;

        Future<Integer>[] futures = new Future[files.length];
        int index = 0;

        for (File file : files) {
            if (file.isDirectory()) {
                futures[index++] = executor.submit(() -> countFiles(file, keyword));
            } else {
                futures[index++] = executor.submit(() -> file.getName().contains(keyword) ? 1 : 0);
            }
        }

        for (Future<Integer> future : futures) {
            if (future != null) count += future.get();
        }

        executor.shutdown();
        return count;
    }
}
