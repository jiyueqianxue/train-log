package io.mine.ft.train;

import java.util.concurrent.*;
import java.util.concurrent.ThreadPoolExecutor.CallerRunsPolicy;

public class ThreadPoolUtil {
	
//	/**线程池的最小线程数量*/  
//    private static final int CORE_POOL_SIZE = 1;
    
    /**线程池的最大线程数量*/  
    private static final int MAX_THREAD = 100; 
    
    /**线程池的最大等待队列数量*/  
    private static final int MAX_QUEUE = 1000;//默认为Integer.MAX_VALUE
      
//    /**空闲线程在终止前等待新任务的最大时间(秒)*/  
//    private static final long KEEP_ALIVE_TIME = 0L;  
//    
//    /**长时间线程池,用于执行短时间较长的线程,需要设置最大线程数量*/  
//    private static final ExecutorService FIXED_THREAD_POOL = Executors.newFixedThreadPool(MAX_THREAD);  
//      
//    /**短时间线程池，用于存放执行时间较短的线程,无需设置线程的最大数量*/  
//    private static final ExecutorService CACHED_THREAD_POOL = Executors.newCachedThreadPool();  
//    
//    public static ExecutorService getFixedThreadPool(){
//        
//        return FIXED_THREAD_POOL;
//    }
//    public static ExecutorService getCachedThreadPool(){
//        
//        return CACHED_THREAD_POOL;
//    }
    
    private static volatile ExecutorService threadPool;

    public static ExecutorService getThreadPool() {
        if (threadPool == null) {
            //同步代码块（对象未初始化时，使用同步代码块，保证多线程访问时对象在第一次创建后，不再重复被创建）
            synchronized (ThreadPoolUtil.class) {
                if (threadPool == null) {
                	//线程超过队列最大值，调用线程会等待执行
                	RejectedExecutionHandler handler = new CallerRunsPolicy();
                	
                	threadPool = new ThreadPoolExecutor(MAX_THREAD, MAX_THREAD,
                			30L, TimeUnit.SECONDS,
                            new LinkedBlockingQueue<Runnable>(MAX_QUEUE),
                            Executors.defaultThreadFactory(),
                            handler);
                }   
            }   
        }   
        return threadPool;
    }
}
