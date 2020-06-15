package com.tju.suma.test;

import com.tju.suma.bean.DicOwlBean;
import com.tju.suma.bean.DicOwlMap;
import com.tju.suma.bean.DicRdfDataBean;
import com.tju.suma.bean.DicRdfDataMap;
import com.tju.suma.reason.DicSerialReason;
import com.tju.suma.reason.SameAsReason;
import lombok.extern.slf4j.Slf4j;
import org.apache.log4j.Logger;
import org.apache.xerces.dom3.as.ASObjectList;

import java.util.*;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Parallel {

    public static AtomicInteger anonymous = new AtomicInteger(-2);
    private static Logger log = Logger.getLogger(Parallel.class);
    static int n = Runtime.getRuntime().availableProcessors();
    static ThreadPoolExecutor threadPool = new ThreadPoolExecutor(n, n,
            60L, TimeUnit.MILLISECONDS, new LinkedBlockingQueue<>());;
    static public AtomicInteger index;

    public static void reason(int step, int eachThreadDataSize, int threadSize) throws InterruptedException, ExecutionException {
        long startTime = System.currentTimeMillis();
        int loopCount = 1;
        int currLoopIndex = 0;
        int currLoopLastIndex = 0;
        System.out.println("ThreadSize: "+n);
        if (threadSize != -1) {
            n = threadSize;
        }
        //遍历数据
        Map<Integer, DicRdfDataBean> totalData = DicRdfDataMap.getDicDataMap();
        //存放每次新生成的数据,每次循环结束把新数据copy到iteratorMap进行第二轮迭代
//        Map<Integer, DicRdfDataBean> stashMap = DicRdfDataMap.getDicStashMap();
//        //迭代数据，第二轮以后迭代 iteratorMap，每次结束把数据copy到totalData进行存储
//        Map<Integer, DicRdfDataBean> iteratorMap = DicRdfDataMap.getDicIteratorMap();
        //规则
//        Map<String, List<DicOwlBean>> totalRule = DicOwlMap.getRuleMap();
        //索引
        index = new AtomicInteger(totalData.size());
        log.info("----------------------Start Materialization--------------------------");

        while (true) {
            log.info("loopCount " + loopCount + " dataCount " + index.get());
            //当前推理轮次的最大id

            currLoopLastIndex = index.get();

            List<Future<Boolean>> futures = new ArrayList<>();
            while (currLoopIndex < currLoopLastIndex) {
                int begin = currLoopIndex;
                int end = currLoopIndex + eachThreadDataSize < currLoopLastIndex ? currLoopIndex + eachThreadDataSize : currLoopLastIndex - 1;
                Future<Boolean> future = threadPool.submit(new MyNewTask(begin, end));
                futures.add(future);
                currLoopIndex = end + 1;
            }
            for (Future<Boolean> future : futures) {
                future.get();
            }

            if (currLoopIndex == index.get()) {
                //没有新数据产生
                log.info("No new data was generated!");
                break;
            }
            if (loopCount >= step) {
                log.info(step + "-step universal model is finished");
                break;
            }
            loopCount++;


        }

        long endTime = System.currentTimeMillis();
        System.out.println("总运行时间为： " + (endTime - startTime));
        threadPool.shutdown();


    }

//    public static void main(String[] args) throws InterruptedException {
//        long startTime = System.currentTimeMillis();
//        for (int i = 0; i < n; i++) {
//            NewTask newTask = new NewTask();
//            threadPool.submit(newTask);
//        }
//        threadPool.shutdown();
//        if (threadPool.isTerminated()) {
//            long endTime = System.currentTimeMillis();
//            System.out.println("总运行时间为： " + (endTime - startTime));
//        }
//
//    }
}
