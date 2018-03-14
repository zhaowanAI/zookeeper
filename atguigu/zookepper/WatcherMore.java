package com.atguigu.zookepper;

import org.apache.log4j.Logger;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooDefs.Ids;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import com.atguigu.unit.BaseZoo;

import lombok.Getter;
import lombok.Setter;


public class WatcherMore extends BaseZoo{
	public static final Logger logger = Logger.getLogger(WatcherMore.class);

	private@Setter@Getter String newValue = "";
	private@Setter@Getter String oldValue = "";
	
	//创建ZK的session链接对象实例
	public ZooKeeper startZk() throws Exception {
		
		return 	new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
			
			@Override
			public void process(WatchedEvent arg0) {
				
			}
		});
		
	}
	//创建Znode节点并赋值
	public void createZk(String path,String data) throws Exception, InterruptedException {
		
		zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
	}
	//获取Znode节点
	public String getZnode(String path) throws Exception, InterruptedException {
		String result = "";
		 byte[] data = zk.getData(path, new Watcher() {
			
			@Override
			public void process(WatchedEvent arg0) {
				
				try {
					listenPath(path);
				} catch (KeeperException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Stat());
		result = new String(data);
		 oldValue = result;
		return result;
	}
	public boolean listenPath(String path) throws KeeperException, InterruptedException {
		String result = "";
		byte[] data1= zk.getData(path, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				try {
					listenPath(path);
				} catch (KeeperException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}, new Stat());
		result = new String(data1);
		newValue = result;
		if(oldValue.equals(newValue)) {
			logger.info("no changes----------!!!");
			return false;
		}else {
			logger.info("oldValue:"+oldValue+"    change  to =====>"+"newValue:"+newValue);
			
			oldValue = newValue;
			
			return true;
		}
		
		
	}

	public static void main(String[] args) throws Exception {

		WatcherMore more = new WatcherMore();
		
		more.setZk(more.startZk());
		
		if(more.getZk().exists(PATH, false)==null) {
			
			more.createZk(PATH, "hello byby...!");
			
			String znode = more.getZnode(PATH);
			
			if (logger.isInfoEnabled()) 
			{
				logger.info("main(String[]) -------- String result=" + znode);
			}			
		}else {
			logger.info("this node has already exists!!!!");			
		}
		
		Thread.sleep(Long.MAX_VALUE);
		
	}

}
