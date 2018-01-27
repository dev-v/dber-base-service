package com.dber.base.web.controller;

import com.dber.base.entity.Response;
import com.dber.base.login.LoginCheckController;
import com.dber.base.mybatis.plugin.pagination.page.Page;
import com.dber.base.service.IService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.PostConstruct;
import java.util.Collection;

/**
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2018/1/17
 */
public abstract class AbstractReadController<E> extends LoginCheckController {

    protected IService<E> service;

    /**
     * <pre>
     * 根据主键获取数据
     * </pre>
     *
     * @return
     */
    @RequestMapping("/get/{id}")
    public Response<E> get(@PathVariable long id) {
        E e = service.get(id);
        return Response.newSuccessResponse(e);
    }

    /**
     * <pre>
     * 根据主键集合获取数据
     * </pre>
     *
     * @return
     */
    @RequestMapping("/gets")
    public Response<Collection<E>> gets(Long[] ids) {
        return Response.newSuccessResponse(service.gets(ids));
    }

    /**
     * <pre>
     * 分页查询数据
     * 默认20页
     * </pre>
     *
     * @return
     */
    @RequestMapping("/query/{currentPage}")
    public Response<Page<E>> query(@PathVariable int currentPage, E data) {
        Page<E> page = new Page<>(currentPage);
        page.setCondition(data);
        page.setSort("id desc");
        service.query(page);
        return Response.newSuccessResponse(page);
    }

    /**
     * 不带分页查询
     *
     * @param data
     * @return
     */
    @RequestMapping("/query")
    public Response<Collection<E>> queryWithoutPage(E data) {
        return Response.newSuccessResponse(service.queryWithoutPage(data));
    }

    /**
     * 查询一条数据
     *
     * @param data
     * @return
     */
    @RequestMapping("/queryOne")
    public Response<E> queryOne(E data) {
        return Response.newSuccessResponse(service.queryOne(data));
    }

    /**
     * <pre>
     * 根据条件获取主键集合
     * 最大返回1000条
     * </pre>
     *
     * @return
     */
    @RequestMapping("/ids")
    public Response<long[]> getIds(E e) {
        return Response.newSuccessResponse(service.getIds(e));
    }

    @PostConstruct
    public final void setService() {
        this.service = getService();
    }

    protected abstract IService<E> getService();
}
