package com.dber.base.web.controller;

import com.dber.base.entity.Response;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

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
public abstract class AbstractController<E> extends AbstractReadController {

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
    public Response<Integer> update(E e) {
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
    public Response<E> save(E e) {
        service.save(e);
        return Response.newSuccessResponse(e);
    }

    /**
     * <pre>
     * 根据主键删除数据
     * </pre>
     *
     * @return
     */
    @RequestMapping("/del/{id}")
    public Response<Integer> del(@PathVariable long id) {
        return Response.newSuccessResponse(service.del(id));
    }

    /**
     * <pre>
     * 根据条件删除数据
     * </pre>
     *
     * @return
     */
    @RequestMapping("/delWithCondition")
    public Response<Integer> del(E e) {
        return Response.newSuccessResponse(service.del(e));
    }

    /**
     * <pre>
     * 根据主键集合删除数据
     * 最大一次删除1000条
     * </pre>
     *
     * @return
     */
    @RequestMapping("/dels")
    public Response<Integer> dels(Long[] ids) {
        return Response.newSuccessResponse(service.dels(ids));
    }
}