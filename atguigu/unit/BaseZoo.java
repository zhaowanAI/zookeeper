package com.atguigu.unit;

import org.apache.log4j.Logger;
import org.apache.zookeeper.ZooKeeper;

import com.atguigu.zookepper.HelloZK;

import lombok.Getter;
import lombok.Setter;





public  class BaseZoo {
	
	
	public static final String CONNECT_STRING = "192.168.164.129:2181";
	public static final String PATH = "/zhao";
	public static final int SESSION_TIMEOUT = 20*1000;

	public@Setter@Getter ZooKeeper zk = null;
	
}
