package com.linlazy.mmorpg.server.db.queue;

import com.linlazy.mmorpg.server.db.dao.BaseJdbc;
import com.linlazy.mmorpg.server.db.entity.Entity;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 数据库DB队列处理器
 * @author linlazy
 */
@Component
public class DbQueueManager<T extends Entity> {

    private static Logger logger = LoggerFactory.getLogger(DbQueueManager.class);

    @Autowired
    private BaseJdbc baseJdbc;

    /**
     * 实体队列
     */
    protected Queue<T> queue = new ConcurrentLinkedQueue<>();

    /**
     * 每次提交數量
     */
    private int eachSubmitNum =3;

    /**
     * db执行线程
     */
    private static ThreadPoolExecutor EXECUTOR;

    @PostConstruct
    public void init(){
        ThreadGroup threadGroup = new ThreadGroup("dbGroup");
        NamedThreadFactory dbPrepareThreadFactory = new NamedThreadFactory(threadGroup,
                "dbPrepareThread");
        NamedThreadFactory dbExecuteThreadFactory = new NamedThreadFactory(threadGroup,
                "dbExecuteThread");

        EXECUTOR = new ThreadPoolExecutor(1, 1, 30, TimeUnit.SECONDS,
                new LinkedBlockingQueue<Runnable>(), dbExecuteThreadFactory);

        Thread pollDBQueueThread = dbPrepareThreadFactory.newThread(() -> {
            while (true){

                try {
                    if (queue.isEmpty()) {
                        continue;
                    }
                    logger.error("pollDBQueueThread");
                    List<Entity> entityList = new ArrayList<>();
                    for (int i = 0; i < eachSubmitNum; i++) {
                        T entity = queue.poll();
                        if (entity == null) {
                            continue;
                        }

                        entityList.add(entity);
                    }
                    EXECUTOR.submit(new DBTask(entityList,baseJdbc));
                } catch (Exception ex) {
                    logger.error("{}", ex);
                }
            }
        });
        pollDBQueueThread.setDaemon(true);
        pollDBQueueThread.start();
    }


    public  void pushEntity(T entity){
        queue.add(entity);
    }
}
