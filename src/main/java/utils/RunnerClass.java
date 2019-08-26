package utils;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class RunnerClass {

    public ThreadPoolExecutor getExecutorService() {
        return executorService;
    }

    public void setExecutorService(ThreadPoolExecutor executorService) {
        this.executorService = executorService;
    }

    private ThreadPoolExecutor executorService = new ThreadPoolExecutor(5, 5, 0L,TimeUnit.MILLISECONDS,
            new ArrayBlockingQueue<Runnable>(1000), new ThreadPoolExecutor.CallerRunsPolicy());

    public static void main(String[] args) {




    }
}
