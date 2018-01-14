package com.dber.base.mapper;

import java.io.Serializable;
import java.util.Collection;

import com.dber.base.mybatis.plugin.pagination.page.Page;

/**
 * <li>文件名称: IMapper.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: dao层基础操作</li>
 * <li>其他说明: ...</li>
 * 
 * @version 1.0
 * @since 2017年12月19日
 * @author dev-v
 */
public interface IMapper<E> {
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
	 * 根据唯一索引保存数据 ：
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
	 * 查询返回的数据只有一条
	 *
	 * @param e
	 * @return
	 */
	E queryOne(E e);

	/**
	 * <pre>
	 * 不建议使用
	 * </pre>
	 * 
	 * @return
	 */
	Collection<E> queryWithoutPage(E e);

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
}
