package com.linlazy.mmorpglintingyang.server.threadpool;

import io.netty.util.internal.PriorityQueue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * 一个线程对应一个任务队列
 * @author linlazy
 */
public class SingleThread{


    private Logger logger = LoggerFactory.getLogger(SingleThread.class);


    private Thread thread;

    private long lastExecutionTime;
    /**
     *  最大任务数
     */
    private final int maxPendingTasks;

    /**
     *  实际执行任务队列
     */
    private Queue<Runnable> taskQueue;
    /**
     * 线程任务队列
     */
    private Queue<Runnable> tailTasks;

    /**
     * 调度任务队列
     */
    PriorityQueue<ScheduledFutureTask> scheduledTaskQueue;

    private static final Runnable WAKEUP_TASK = () -> {
        // Do nothing.
    };

    public SingleThread(int maxPendingTasks) {
        this.maxPendingTasks = Math.max(16, maxPendingTasks);
        taskQueue = newTaskQueue(this.maxPendingTasks);
        tailTasks = newTaskQueue(this.maxPendingTasks);
    }

    private Queue<Runnable> newTaskQueue(int maxPendingTasks) {
        return new LinkedBlockingQueue<Runnable>(maxPendingTasks);
    }

    private Runnable pollTask() {
        assert inSameThread();

        return pollTaskFrom(taskQueue);
    }

    private boolean inSameThread() {
       return thread == Thread.currentThread();
    }

    private Runnable pollTaskFrom(Queue<Runnable> taskQueue) {
        for (;;) {
            Runnable task = taskQueue.poll();
            if (task == WAKEUP_TASK) {
                continue;
            }
            return task;
        }
    }




    /**
     * 插入队列
     * @param task
     * @return
     */
    boolean offerTask(Runnable task) {

        return taskQueue.offer(task);
    }

    /**
     * 运行所有任务
     * @return 若至少运行过一次任务则返回真
     */
    private boolean runAllTasks(){

        boolean runAtLeastOne =false;
        boolean fetchAll =false;
        do{

            //拉去所有调度任务
            fetchAll = fetchFromScheduledTaskQueue();
            if(runAllTasksFrom(taskQueue)){
                runAtLeastOne =true;
            }


            //已拉取完所有调度任务
        }while (!fetchAll);

        if (runAtLeastOne) {
            lastExecutionTime = System.nanoTime();
        }
        afterRunningAllTasks();
        return runAtLeastOne;
    }

    /**
     * timeoutNanos时间内运行所有任务，
     * @param timeoutNanos 超时时间
     * @return 若有一次任务执行，则返回true
     */
    private boolean runAllTasks(long  timeoutNanos){
        //拉去调度队列任务到实际执行队列
        fetchFromScheduledTaskQueue();
        //取出实际执行队列首个任务
        Runnable task = pollTask();
        if (task == null) {
            //执行后续任务队列
            afterRunningAllTasks();
        }

        final long deadline =System.nanoTime() + timeoutNanos;
        //执行实际任务队列
        long runTasks = 0;
        for (;;){
            safeExecute(task);

            runTasks ++;

            //每执行64次任务检查一下是否超时
            if ((runTasks & 0x3F) == 0) {
                lastExecutionTime = System.nanoTime();
                if (lastExecutionTime >= deadline) {
                    break;
                }
            }

            task = pollTask();
            if (task == null) {
                this.lastExecutionTime = System.nanoTime();
                break;
            }
        }
        //执行后续任务队列
        afterRunningAllTasks();
        return true;
    }

    private void afterRunningAllTasks() {
        runAllTasksFrom(tailTasks);
    }


    /**
     * 执行所有任务
     * @param taskQueue
     * @return 返回执行任务结果
     */
    private  boolean runAllTasksFrom(Queue<Runnable> taskQueue) {

        Runnable task = pollTaskFrom(taskQueue);
        if (task == null) {
            return false;
        }
        for (;;) {
            safeExecute(task);
            task = pollTaskFrom(taskQueue);
            if (task == null) {
                return true;
            }
        }
    }

    /**
     * 执行任务
     * @param task 任务
     */
    private  void safeExecute(Runnable task) {
        try {
            task.run();
        } catch (Throwable t) {
            logger.warn("A task raised an exception. Task: {}", task, t);
        }
    }


    private boolean fetchFromScheduledTaskQueue() {
        //todo
        int nanoTime = 0;
        Runnable scheduledTask  = pollScheduledTask(nanoTime);
        while (scheduledTask != null) {
            if (!taskQueue.offer(scheduledTask)) {
                // 任务队列中没有剩余空间，添加回scheduledTaskQueue，以便再次拾取它。
                this.scheduledTaskQueue.add((ScheduledFutureTask) scheduledTask);
                return false;
            }
            scheduledTask  = pollScheduledTask(nanoTime);
        }
        return true;
    }

    private Runnable pollScheduledTask(int nanoTime) {
        ScheduledFutureTask scheduledTask = this.scheduledTaskQueue == null ? null : this.scheduledTaskQueue.peek();
        if (scheduledTask == null) {
            return null;
        }

        if (scheduledTask.getDeadlineNanos() <= nanoTime) {
            scheduledTaskQueue.remove();
            return scheduledTask;
        }
        return null;
    }

    /**
     * 增加任务
     * @param task
     */
    private void addTask(Runnable task) {
        if (task == null) {
            throw new NullPointerException("task");
        }

        offerTask(task);
    }


    private Runnable peekTask() {
        assert inSameThread();
        return taskQueue.peek();
    }

    private boolean hasTasks() {
        assert inSameThread();
        return !taskQueue.isEmpty();
    }
}
