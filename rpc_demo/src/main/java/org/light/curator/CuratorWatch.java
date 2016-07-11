package org.light.curator;


import javax.sound.midi.Patch; 

import org.apache.curator.RetryPolicy; 
import org.apache.curator.framework.CuratorFramework; 
import org.apache.curator.framework.CuratorFrameworkFactory; 
import org.apache.curator.framework.CuratorFrameworkFactory.Builder; 
import org.apache.curator.framework.api.CuratorWatcher; 
import org.apache.curator.framework.recipes.cache.NodeCache; 
import org.apache.curator.framework.recipes.cache.NodeCacheListener; 
import org.apache.curator.framework.recipes.cache.PathChildrenCache; 
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent; 
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener; 
import org.apache.curator.retry.ExponentialBackoffRetry; 
import org.apache.curator.utils.ZKPaths; 
import org.apache.zookeeper.WatchedEvent; 

public class CuratorWatch {
    
    static CuratorFramework zkclient=null; 
    static String nameSpace="php"; 
    static { 

      String zkhost="10.111.0.95:2181";//zk的host 
      RetryPolicy rp=new ExponentialBackoffRetry(1000, 3);//重试机制 
      Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost) 
      .connectionTimeoutMs(5000) 
      .sessionTimeoutMs(5000) 
      .retryPolicy(rp); 
      builder.namespace(nameSpace); 
      CuratorFramework zclient = builder.build(); 
      zkclient=zclient; 
      zkclient.start();// 放在这前面执行 
      zkclient.newNamespaceAwareEnsurePath(nameSpace); 
      
    } 


    public static void main(String[] args) {
        // TODO Auto-generated method stub

    }

}
