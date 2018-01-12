package com.dber.base.web.controller;

import java.util.Collection;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpSession;

import com.dber.base.web.login.ILoginService;
import com.dber.base.web.vo.Account;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.dber.base.mybatis.plugin.pagination.page.Page;
import com.dber.base.web.vo.Response;
import com.dber.base.exception.system.NotFoundException;
import com.dber.base.exception.system.login.NotLoginException;
import com.dber.base.service.IService;

/**
 * <li>文件名称: AbstractController.java</li>
 * <li>修改记录: ...</li>
 * <p>
 * <pre>
 * 内容摘要: controller顶层类
 * 需要登录验证  统一controller处理的公共方法需集成此类
 * </pre>
 * <p>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月21日
 */
public abstract class AbstractController<E> {

    @Autowired
    private ILoginService loginService;

    IService<E> service;

    public Account getAccount(HttpSession session) throws NotLoginException {
        return loginService.getAccount(session);
    }

    @RequestMapping("/insert")
    public Response<E> insert(E e) {
        service.insert(e);
        return Response.newSuccessResponse(e);
    }

    /**
     * <pre>
     * 根据主键修改数据
     * </pre>
     *
     * @param e
     * @return 修改成功行数
     */
    @RequestMapping("/update")
    Response<Integer> update(E e) {
        return Response.newSuccessResponse(service.update(e));
    }

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
    @RequestMapping("/save")
    Response<E> save(E e) {
        service.save(e);
        return Response.newSuccessResponse(e);
    }

    /**
     * <pre>
     * 根据主键删除数据
     * </pre>
     *
     * @param key
     * @return
     */
    @RequestMapping("/del/{id}")
    Response<Integer> del(@PathVariable long id) {
        return Response.newSuccessResponse(service.del(id));
    }

    /**
     * <pre>
     * 根据主键获取数据
     * </pre>
     *
     * @param key
     * @return
     */
    @RequestMapping("/get/{id}")
    Response<E> get(@PathVariable long id) {
        E e = service.get(id);
        if (e == null) {
            throw new NotFoundException();
        }
        return Response.newSuccessResponse(e);
    }

    /**
     * <pre>
     * 根据主键集合获取数据
     * </pre>
     *
     * @param key
     * @return
     */
    @RequestMapping("/gets")
    Response<Collection<E>> gets(Long[] ids) {
        return Response.newSuccessResponse(service.gets(ids));
    }

    /**
     * <pre>
     * 分页查询数据
     * 默认20页
     * </pre>
     *
     * @param e
     * @return
     */
    @RequestMapping("/query/{currentPage}")
    Response<Page<E>> query(@PathVariable int currentPage, E data) {
        Page<E> page = new Page<>(currentPage);
        page.setCondition(data);
        page.setSort("modify_time desc");
        service.query(page);
        return Response.newSuccessResponse(page);
    }

    /**
     * 不带分页查询
     * xxxxxxxxxx
     *
     * @param data
     * @return
     */
    @RequestMapping("/query")
    Response<Collection<E>> queryWithoutPage(E data) {
        return Response.newSuccessResponse(service.queryWithoutPage(data));
    }

    /**
     * <pre>
     * 根据条件获取主键集合
     * 最大返回1000条
     * </pre>
     *
     * @param key
     * @return
     */
    @RequestMapping("/ids")
    public Response<long[]> getIds(E e) {
        return Response.newSuccessResponse(service.getIds(e));
    }

    /**
     * <pre>
     * 根据主键集合删除数据
     * 最大一次删除1000条
     * </pre>
     *
     * @param key
     * @return
     */
    @RequestMapping("/dels")
    public Response<Integer> dels(Long[] ids) {
        return Response.newSuccessResponse(service.dels(ids));
    }

    @PostConstruct
    public final void setService() {
        this.service = getService();
    }

    protected abstract IService<E> getService();
}
