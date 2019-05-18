package cn.wendong.core.scanner.starter.utils;

import java.util.concurrent.*;

/**
 * 线程池工具类
 * 
 * @author MB yangtdo@qq.com
 * @date 2019-02-09
 */
public class ExecutorUtil {
	/**
	 * 线程池
	 */
	private static ThreadPoolExecutor executor = new ThreadPoolExecutor(8, 16, 3, TimeUnit.SECONDS,
			new LinkedBlockingQueue<>(10000), new NamedThreadFactory("class-scanner-thread-"));

	/**
	 * 在线程池执行线程
	 *
	 * @param thread
	 */
	public static void executeInPool(Thread thread) {
		executor.execute(thread);
	}
}
