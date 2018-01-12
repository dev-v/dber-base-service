package com.dber.base.mybatis.plugin.pagination;

import com.dber.base.mybatis.plugin.pagination.page.Page;
import com.mysql.jdbc.util.LRUCache;

/**
 * <li>文件名称: CacheCount.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月20日
 * @author dev-v
 */
public class CacheCount {

	private static final LRUCache<String, Long> CACHE_MAP = new LRUCache<>(10000);

	/**
	 * <pre>
	 * 若沒有緩存 返回 -1
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	public static final long getCacheCount(String key) {
		Long count = CACHE_MAP.get(key);
		if (count == null) {
			return -1;
		} else {
			return count;
		}
	}

	public static final void cacheCount(String key, Page<?> page) {
		if (page.getAllPage() > 2) {
			CACHE_MAP.put(key, page.getCount());
		} else {
			CACHE_MAP.remove(key);
		}
	}
}
