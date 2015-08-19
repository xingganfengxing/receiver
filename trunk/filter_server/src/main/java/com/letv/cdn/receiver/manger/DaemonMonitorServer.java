package com.letv.cdn.receiver.manger;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.letv.cdn.common.Env;

/**
 * 容灾监控类：若主节点服务异常终止，备份节点启动继续工作 对过滤进程监控并提供容灾支持
 * 
 * @date 2014-10-22
 * @author sunyan
 *
 */
public class DaemonMonitorServer {
	private static final Logger log = LoggerFactory
			.getLogger(DaemonMonitorServer.class);

	private static final String SERVER_NAME; // 提供服务机器名称
	private static final Long FREQUENCE; // 报告存活性频率
	private static final String FILE_PATH; // 生产报告路径
	private static boolean isMainServer; // 主结点标识
	private static boolean initialStatus; // 初始状态标识
	private static DaemonMonitorServer server;
	private File runInfofile = new File(FILE_PATH); // 报告文件

	static {
		FILE_PATH = Env.get("monitorFilePath");
		SERVER_NAME = StringUtils.isEmpty(Env.get("serverName")) ? "anonymous"
				: Env.get("serverName");
		FREQUENCE = StringUtils.isEmpty(Env.get("monitor.frequence")) ? 5000
				: Long.parseLong(Env.get("monitor.frequence"));
	}

	/**
	 * 判断是否成为主服务节点: 1.若报告文件不存在，则先生成文件的服务为主服务 2.若报告文件存在，则根据文件内容判断是否要成为主服务
	 * 
	 * @return
	 * @throws Exception
	 */
	public static boolean isMainFilterServer() throws Exception {
		if (StringUtils.isEmpty(FILE_PATH)) {
			throw new Exception("未定义monitorFile路径");
		}
		File monitorFile = new File(FILE_PATH);
		if (!monitorFile.exists()) {
			monitorFile.createNewFile();
			return true;
		} else {
			RandomAccessFile ranf = new RandomAccessFile(monitorFile, "r");
			if (!isMainServerOk(ranf.readLine())) {
				ranf.close();
				return true;
			}
			ranf.close();
			return false;
		}
	}

	public static synchronized void startDaemonMonitorServer(
			boolean isMainFilterServer) throws Exception {

		isMainServer = isMainFilterServer;
		initialStatus = isMainFilterServer;
		if (server != null)
			return;
		server = new DaemonMonitorServer();
		try {
			server.start();
		} catch (Exception ex) {
			log.error("DaemonMonitorServer start error: " + ex.getMessage());
			throw ex;
		}
		log.info("============ DaemonMonitorServer Is Running. ============");
	}

	/**
	 * 启动后台监控线程
	 */
	private void start() {
		DisasterRecoveryMonitor monitorThread = new DisasterRecoveryMonitor();
		new Thread(monitorThread).start();
	}

	/**
	 * 监控主节点运行状态线程
	 * 
	 * @author sunyan
	 *
	 */
	class DisasterRecoveryMonitor implements Runnable {
		String dataToWrite;
		String s = null;

		FileChannel channel = null;
		FileLock lock = null;

		RandomAccessFile raf;

		@Override
		public void run() {
			while (true) {
				// 若成为主服务节点，则不断写状态
				if (isMainServer) {
					dataToWrite = DaemonMonitorServer.SERVER_NAME + ",time:"
							+ System.currentTimeMillis() + ",status:running"
							+ "\r\n";
					//log.info("server is writing---"+dataToWrite);
					if (runInfofile.exists()) {
						try {
							raf = new RandomAccessFile(runInfofile, "rw");
							channel = raf.getChannel();
							lock = channel.lock();// 独占锁
							channel.write(ByteBuffer.wrap((dataToWrite)
									.getBytes()));
						} catch (IOException e) {
							log.error(DaemonMonitorServer.SERVER_NAME
									+ "写监控文件出错", e);
						} finally {
							if (lock != null) {
								try {
									lock.release();
									lock = null;
								} catch (IOException e) {
									log.error(DaemonMonitorServer.SERVER_NAME
											+ "解锁监控文件失败", e);
								}
							}
							if (channel != null) {
								try {
									channel.close();
									channel = null;
								} catch (IOException e) {
									log.error(DaemonMonitorServer.SERVER_NAME
											+ "关闭写文件channel失败", e);
								}
							}
						}

					} else {
						try {
							runInfofile.createNewFile();
						} catch (IOException e) {
							log.error(DaemonMonitorServer.SERVER_NAME
									+ "创建文件失败", e);
						}
					}
					try {
						// 每隔FREQUENCE毫秒，写一次记录
						Thread.sleep(FREQUENCE);
					} catch (InterruptedException e) {
						log.error(DaemonMonitorServer.SERVER_NAME
								+ "daemonMonitor线程异常中断", e);
					}
				} else {
					//log.info("server is reading---"+DaemonMonitorServer.SERVER_NAME);
					// 若由主结点变成备份结点，则需要将其服务关闭
					if (initialStatus != isMainServer) {
						try {
							DisruptorServer.stopDisruptorServer();
							MessageServer.stopMessageServer();
							KafkaProcessManager.closeAllProcess();
						} catch (Exception e) {
							log.error(DaemonMonitorServer.SERVER_NAME
									+ "停止服务异常", e);
						}
					}
					if (runInfofile.exists()) {
						try {
							raf = new RandomAccessFile(runInfofile, "r");
							s = raf.readLine();
							if (!isMainServerOk(s)) {
								isMainServer = true; // 如果判断主节点没有正常工作，则backup节点变为主结点
								// 成为主节点后启动服务
								DisruptorServer.startDisruptorServer();
								MessageServer.startMessageServer();
								KafkaProcessManager.getAllProcess();
							}
						} catch (FileNotFoundException e) {
							log.error(
									DaemonMonitorServer.SERVER_NAME + "读文件出错",
									e);
						} catch (IOException e) {
							log.error(
									DaemonMonitorServer.SERVER_NAME + "读文件出错",
									e);
						} catch (Exception e) {
							log.error(
									DaemonMonitorServer.SERVER_NAME + "读文件出错",
									e);
						}
					} else {
						try {
							runInfofile.createNewFile();
						} catch (IOException e) {
							log.error(DaemonMonitorServer.SERVER_NAME
									+ "创建文件失败", e);
						}
					}
					try {
						// 每隔FREQUENCE毫秒，读一次记录
						Thread.sleep(3000);
					} catch (InterruptedException e) {
						log.error(DaemonMonitorServer.SERVER_NAME
								+ "daemonMonitor线程异常中断", e);
					}
				}
			}
		}
	}

	/**
	 * 根据运行日志文件判断主节点是否正常运行
	 * 
	 * @param latestlog
	 * @return
	 */
	private static boolean isMainServerOk(String latestlog) {
		String tempTime;
		String[] tempArr = latestlog.split(",");
		for (String str : tempArr) {
			if (str.startsWith("time")) {
				tempTime = str.split(":")[1];
				return System.currentTimeMillis() - Long.parseLong(tempTime) < FREQUENCE * 2;
			}
		}
		return false;
	}
}
