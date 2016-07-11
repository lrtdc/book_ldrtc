package org.light.curator;

import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.UUID;
import java.util.concurrent.ConcurrentSkipListSet;

import org.apache.curator.RetryPolicy;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.api.CuratorWatcher;
import org.apache.curator.framework.recipes.cache.PathChildrenCache;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheEvent;
import org.apache.curator.framework.recipes.cache.PathChildrenCacheListener;
import org.apache.curator.framework.CuratorFrameworkFactory.Builder;
import org.apache.curator.framework.state.ConnectionState;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.utils.ZKPaths;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.ZooDefs;
import org.apache.zookeeper.data.Stat;

public class StudyCurator {
    private CuratorFramework client;

    public StudyCurator() {
        String zkhost = "10.111.0.95:2181";
//        RetryPolicy rp = new ExponentialBackoffRetry(1000, 3);// 重试机制
//        Builder builder = CuratorFrameworkFactory.builder().connectString(zkhost).connectionTimeoutMs(5000).sessionTimeoutMs(5000).retryPolicy(rp);
//        builder.namespace("test");
//        client = builder.build();
        
        RetryPolicy retryPolicy = new ExponentialBackoffRetry(1000, 3);
        client = CuratorFrameworkFactory.newClient(zkhost, retryPolicy);
        client.start();
    }

    /**
     * 
     * 监听节点变化
     * 
     */
    public void watch() throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client, "/yidian/serving/test_zkdash", false);
        cache.start();

        System.out.println("监听开始/zk........");
        PathChildrenCacheListener plis = new PathChildrenCacheListener() {

            @Override
            public void childEvent(CuratorFramework client, PathChildrenCacheEvent event)
                    throws Exception {
                switch (event.getType()) {
                    case CHILD_ADDED: {
                        System.out.println("Node added: "
                                + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }

                    case CHILD_UPDATED: {
                        System.out.println("Node changed: "
                                + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }

                    case CHILD_REMOVED: {
                        System.out.println("Node removed: "
                                + ZKPaths.getNodeFromPath(event.getData().getPath()));
                        break;
                    }
                }

            }
        };
        // 注册监听
        cache.getListenable().addListener(plis);

    }

    public void test() {
        System.out.println(client.getState().toString());
        try {
            System.out.println(client.getChildren().forPath("/"));
            // client.setData().forPath("/yidian/serving/test_zkdash");

            System.out.println(new String(client.getData().forPath("/yidian/serving/test_zkdash")));
        } catch (Exception e) {
            e.printStackTrace();
        }
        UUID uuid = UUID.randomUUID();
        System.out.println(uuid);
//        try {
//            this.watch();
//        } catch (Exception e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
    }

    public static void main(String[] args) {
        StudyCurator sc = new StudyCurator();
        sc.test();
    }

}
