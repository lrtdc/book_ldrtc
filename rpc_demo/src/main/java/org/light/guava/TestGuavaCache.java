package org.light.guava;

import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;

import com.google.common.cache.Cache;
import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

public class TestGuavaCache {
	
	private LoadingCache<String,String> cahceBuilder;
	
	public TestGuavaCache(){
		this.init();
	}
	
	private void init(){
		CacheLoader<String, String> cacheLoader = new CacheLoader<String, String>(){
            @Override
            public String load(String key) throws Exception {        
                String strProValue="hello load "+key+"!";                
                return strProValue;
            }
            
            @Override
            public Map<String,String> loadAll(Iterable<? extends String> iterable) throws Exception {
            	Map<String,String> rtMap = new HashMap<>();
            	Iterator<? extends String> iters = iterable.iterator();
            	String tmpKey = null;
            	while(iters.hasNext()){
            		tmpKey = iters.next();
            		rtMap.put(tmpKey, "hello loadAll "+tmpKey+" !");
            	}
                return rtMap;
            }
        };
		cahceBuilder = CacheBuilder.newBuilder()
							.concurrencyLevel(10) //设置并发级别为8，并发级别是指可以同时写缓存的线程数
							.maximumSize(10000) //设置缓存最大容量为10000，超过10000之后就会按照LRU最近虽少使用算法来移除缓存项
							.expireAfterAccess(5, TimeUnit.MINUTES) // 根据某个键值对最后一次访问之后5分钟后移除
							.expireAfterWrite(5, TimeUnit.MINUTES)  // 根据某个键值对被创建或值被替换后5分钟移除
							.refreshAfterWrite(1, TimeUnit.MINUTES)  // 给定时间：1分钟内没有被读/写访问，则回收
					        .build(cacheLoader);        
	}
	
	 public void testLoadingCache() throws Exception{
        System.out.println("jerry value : "+cahceBuilder.get("jerry"));
        System.out.println("peida value : "+cahceBuilder.get("peida"));
        cahceBuilder.put("harry", "ssdded");
        System.out.println("harry value : "+cahceBuilder.get("harry"));
        List<String> params = Lists.newArrayList("灵魂", "歌唱", "内心");
        ImmutableMap<String,String> rts = cahceBuilder.getAll(params);
        System.out.println(rts);
        cahceBuilder.invalidate("jerry");   //单个清除
        cahceBuilder.invalidateAll(params); //批量清楚
        cahceBuilder.invalidateAll();		//清楚所有缓存项
    }
	 
	public void testcallableCache()throws Exception{
        Cache<String, String> cache = CacheBuilder.newBuilder().maximumSize(1000).build();  
        String resultVal = cache.get("jerry", new Callable<String>() {  
            public String call() {  
                String strProValue="hello "+"jerry"+"!";                
                return strProValue;
            }  
        });  
        System.out.println("jerry value : " + resultVal);
        
        resultVal = cache.get("peida", new Callable<String>() {  
            public String call() {  
                String strProValue="hello "+"peida"+"!";                
                return strProValue;
            }  
        });  
        System.out.println("peida value : " + resultVal);  
    }

	
	public void test(){
		try {
			testLoadingCache();
		} catch (Exception e) {
			e.printStackTrace();
		}
//		try {
//			this.testcallableCache();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
	}

	public static void main(String[] args) {
		TestGuavaCache tgc = new TestGuavaCache();
		tgc.test();
	}

}
