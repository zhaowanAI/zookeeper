package com.atguigu.zookepper;


import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.data.Stat;

import com.atguigu.unit.BaseZoo;



public class WatcherOne extends BaseZoo{
	
	public static final Logger logger = Logger.getLogger(WatcherOne.class);
	
	//获取zk的session链接对象实例
	public ZooKeeper startZk() throws Exception {
		
	return	new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT,new Watcher() {
		
		@Override
		public void process(WatchedEvent arg0) {
			// TODO Auto-generated method stub
			
		}
	});
				
	}
	//创建Znode节点
	public void createZnode(String path,String data) throws KeeperException, InterruptedException {
		
		zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	
	
	//获取Znode节点
	public String  getZnode(String path) throws Exception{
		
		String result = "";
		
		byte[] data2 = zk.getData(path, new Watcher() {
			
			@Override
			public void process(WatchedEvent arg0) {

				try {
					triggerValue(path);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Stat());
		
		result = new String(data2);
		
		return result;
		
	} 
	public void triggerValue(String path) throws Exception {
		
		String result = "";
		byte[] data3 = zk.getData(path, null, new Stat());
		result = new String(data3);
		
		logger.info("triggerValue--------result: "+result);	//BBB
		
	}
	public static void main(String[] args) throws Exception {
		WatcherOne one =new WatcherOne();
		
		one.setZk(one.startZk());
		if(one.getZk().exists(PATH, false)==null) {
			one.createZnode(PATH, "AAAAA");
			
			String result = one.getZnode(PATH);
			
			if (logger.isInfoEnabled()) 
			{
				logger.info("main(String[]) -------- String result=" + result);
			}			
		}else {
			logger.info("this node has already exists!!!!");			
		}
		
		Thread.sleep(Long.MAX_VALUE);
		
	}
	
}

