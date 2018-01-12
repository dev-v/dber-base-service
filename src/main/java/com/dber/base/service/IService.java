package com.dber.base.service;

import java.io.Serializable;
import java.util.Collection;

import com.dber.base.mybatis.plugin.pagination.page.Page;

/**
 * <li>文件名称: IService.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月19日
 * @author dev-v
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
	 * @param key
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
	 * @param page
	 * @return
	 */
	Collection<E> queryWithoutPage(E e);

	/**
	 * <pre>
	 * 根据条件获取主键集合
	 * 最大返回1000条
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	long[] getIds(E e);

	/**
	 * <pre>
	 * 根据主键集合删除数据
	 * 最大一次删除1000条
	 * </pre>
	 * 
	 * @param key
	 * @return
	 */
	int dels(Serializable[] keys);
}
