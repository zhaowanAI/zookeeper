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


public class HelloZK extends BaseZoo{
	
	public static final Logger logger = Logger.getLogger(HelloZK.class);
	
	//获取ZK的session链接对象实例
	public ZooKeeper startZk() throws Exception {
		
		return new ZooKeeper(CONNECT_STRING, SESSION_TIMEOUT, new Watcher() {
			
			@Override
			public void process(WatchedEvent arg0) {
				
				
			}
		});
	}
	
	
	//创建Znode节点并赋值
	public void createZnode(ZooKeeper zk,String path,String data) throws KeeperException, InterruptedException {
		zk.create(path, data.getBytes(), Ids.OPEN_ACL_UNSAFE, CreateMode.PERSISTENT);
		
	}
	//获取对应节点的值
	public String getZnode(ZooKeeper zk,String path) throws KeeperException, InterruptedException {
		
		String result = "";
		
		byte[] data = zk.getData(path, null, new Stat());
		
		result = new String(data);
		
		return result;
	}
	
	//关闭zk中的session链接对象实例
	public void stopZk(ZooKeeper zk) throws Exception {
		
		if(zk!=null) zk.close();
	}

	
	public static void main(String[] args) throws Exception {

		HelloZK hzk = new HelloZK();
		//链接zk
		ZooKeeper zk = hzk.startZk();
		//判断创建的path是否已经存在
		if(zk.exists(PATH, false)==null) {
			
			hzk.createZnode(zk, PATH, "0906good");
			
			String result = hzk.getZnode(zk, PATH);
			
			if(logger.isInfoEnabled()) {
				logger.info("result============>"+result);
			}
		}else {
			logger.info("exsts!!!!");
		}
		
		hzk.stopZk(zk);
	}

}
