package com.dber.base.service;

import java.io.Serializable;
import java.util.Collection;

import com.dber.base.result.Page;

/**
 * <li>文件名称: IService.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月19日
 */
public interface IService<E> {

    /**
     * <pre>
     * 插入数据 若主键自动生成 主键会设置到实体中
     * </pre>
     *
     * @param e
     * @return 返回插入数量
     */
    int insert(E e);

    /**
     * <pre>
     * 根据主键删除数据
     * </pre>
     *
     * @param key
     * @return 删除成功行数
     */
    int del(Serializable key);

    /**
     * <pre>
     * 根据主键修改数据
     * </pre>
     *
     * @param e
     * @return 修改成功行数
     */
    int update(E e);

    /**
     * <pre>
     * 保存数据 ：
     * 有id为修改
     * 无id为新增
     * </pre>
     *
     * @param e
     * @return
     */
    int save(E e);

    /**
     * <pre>
     * 根据主键获取数据
     * </pre>
     *
     * @param key
     * @return
     */
    E get(Serializable key);

    /**
     * <pre>
     * 根据主键集合获取数据
     * </pre>
     *
     * @param keys
     * @return
     */
    Collection<E> gets(Serializable[] keys);

    /**
     * <pre>
     * 分页查询数据
     * </pre>
     *
     * @param e
     * @return
     */
    Collection<E> query(Page<E> e);

    /**
     * <pre>
     * 不建议使用
     * 若一定要使用 请务必加上limit字句
     * </pre>
     *
     * @return
     */
    Collection<E> queryWithoutPage(E e);

    /**
     * 查询返回的数据只有一条
     *
     * @param e
     * @return
     */
    E queryOne(E e);

    boolean has(E e);

    /**
     * <pre>
     * 根据条件获取主键集合
     * 最大返回1000条
     * </pre>
     *
     * @return
     */
    long[] getIds(E e);

    /**
     * <pre>
     * 根据主键集合删除数据
     * 最大一次删除1000条
     * </pre>
     *
     * @param keys
     * @return
     */
    int dels(Serializable[] keys);

    /**
     * 根据删除条件删除数据
     * @param e
     * @return
     */
    int del(E e);

    /**
     * 根据查询条件获取满足条件的记录数量
     * @param e
     * @return
     */
    int count(E e);
}
