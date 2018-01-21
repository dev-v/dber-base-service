package com.dber.base.service;

import java.io.Serializable;
import java.util.Collection;

import javax.annotation.PostConstruct;

import com.dber.base.mapper.IMapper;
import com.dber.base.mybatis.plugin.pagination.page.Page;

/**
 * <li>文件名称: AbstraService.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月19日
 */
public abstract class AbstractService<E> implements IService<E> {

    IMapper<E> mapper;

    @Override
    public int insert(E e) {
        return mapper.insert(e);
    }

    @Override
    public int del(Serializable key) {
        return mapper.del(key);
    }

    @Override
    public int update(E e) {
        return mapper.update(e);
    }

    @Override
    public int save(E e) {
        return mapper.save(e);
    }

    @Override
    public E get(Serializable key) {
        return mapper.get(key);
    }

    @Override
    public Collection<E> gets(Serializable[] keys) {
        return mapper.gets(keys);
    }

    @Override
    public Collection<E> query(Page<E> page) {
        return mapper.query(page);
    }

    @Override
    public Collection<E> queryWithoutPage(E e) {
        return mapper.queryWithoutPage(e);
    }

    @Override
    public E queryOne(E e) {
        return mapper.queryOne(e);
    }

    public long[] getIds(E e) {
        return mapper.getIds(e);
    }

    @Override
    public int dels(Serializable[] keys) {
        return mapper.dels(keys);
    }

    @Override
    public int del(E e) {
        return mapper.delByCondition(e);
    }

    /**
     * <pre>
     * 返回操作实体 < E > 的纯mapper对象
     * </pre>
     *
     * @return
     */
    protected abstract IMapper<E> getMapper();

    @PostConstruct
    public final void setMapper() {
        if (this.mapper == null) {
            this.mapper = getMapper();
        }
    }
}
