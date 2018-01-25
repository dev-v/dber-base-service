package com.dber.base.mybatis.plugin.pagination.ext;

import com.dber.base.mybatis.plugin.pagination.page.Page;
import org.apache.commons.collections.map.LRUMap;

/**
 * <li>文件名称: CacheCount.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月20日
 */
public class CacheCount {

    private static final LRUMap CACHE_MAP = new LRUMap(10000);

    /**
     * <pre>
     * 若沒有緩存 返回 -1
     * </pre>
     *
     * @param key
     * @return
     */
    public static final long getCacheCount(String key) {
        Long count = (Long) CACHE_MAP.get(key);
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
