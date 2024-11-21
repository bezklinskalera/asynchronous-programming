package task2;

import java.io.File;
import java.util.concurrent.RecursiveTask;

class WorkStealingTask extends RecursiveTask<Integer> {
    private final File directory;
    private final String keyword;

    public WorkStealingTask(File directory, String keyword) {
        this.directory = directory;
        this.keyword = keyword;
    }

    @Override
    protected Integer compute() {
        int count = 0;
        File[] files = directory.listFiles();
        if (files == null) return 0;

        for (File file : files) {
            if (file.isDirectory()) {
                WorkStealingTask task = new WorkStealingTask(file, keyword);
                task.fork();
                count += task.join();
            } else if (file.getName().contains(keyword)) {
                count++;
            }
        }
        return count;
    }
}