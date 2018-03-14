package com.atguigu.zookepper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.data.Stat;

import lombok.Getter;
import lombok.Setter;

public class BalanceTest {
	
	private static final Logger logger = Logger.getLogger(BalanceTest.class);
	
	private static final String CONNECTION_STRING = "192.168.164.129:2181";
	private static final String PATH = "/bank";
	private static final String SUB_PREFIX = "sub";
	private static final int SESSION_TIMEOUT = 20 * 1000;
	private @Setter@Getter	ZooKeeper zk;
	private int index = 0;
	private List<String> serviceLists = new ArrayList<String>();
	private int total = 5;
	
	public ZooKeeper startZk() throws Exception {
		//返回一个zk的链接实例,将初始化能够办理业务的窗口加载进来,[sub5, sub4, sub3, sub2, sub1]
		return new ZooKeeper(CONNECTION_STRING, SESSION_TIMEOUT, new Watcher() {
			
			@Override
			public void process(WatchedEvent event) {
				// TODO Auto-generated method stub
				try {
					serviceLists = zk.getChildren(PATH, true);
				} catch (KeeperException | InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		});
	}

	public String doRequest() throws Exception, InterruptedException {
		index = index+1;
		for (int i = 1; i <=total; i++) {
			
			if(serviceLists.contains(SUB_PREFIX+index)) {
				return new String(zk.getData(PATH+"/"+SUB_PREFIX+index, false, new Stat()));
			}else {
				index = index +1;
			}
		}
		for (int i = 1; i <=total; i++) {
			if(serviceLists.contains(SUB_PREFIX+i)) {
				index = i;
				return new String(zk.getData(PATH+"/"+SUB_PREFIX+index, false, new Stat()));
			}
			
		}
		
		return "no this windows!!!!!";
	}
	
	public static void main(String[] args) throws Exception {

		BalanceTest bt = new BalanceTest();
		
		bt.setZk(bt.startZk());
		Thread.sleep(2000);
		
		String result = null;
		
		for (int i = 1; i <=15; i++) {
			
			result = bt.doRequest();
			Thread.sleep(1000);
			
			System.out.println("customerID:"+i+" currentWindow:"+bt.index+"  result: "+result);
		}
		
	}

	
	
}
