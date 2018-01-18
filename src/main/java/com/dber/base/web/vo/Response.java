package com.dber.base.web.vo;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import com.alibaba.fastjson.annotation.JSONField;

import com.dber.base.result.Result;
import lombok.Data;

/**
 * <li>文件名称: Response.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: 用于封装响应信息</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月20日
 */
@Data
public class Response<E> implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 409194411665386603L;

    /**
     * 响应代码 200 表示成功 500 + 表示访问异常
     */
    private int code = 0;

    /**
     * 响应的uuid 用来追踪请求响应
     */
    private long uuid = 0;

    /**
     * 返回失败消息 （发生异常时，将异常信息的Throwable.getMessage设置到消息中）
     */
    private String msg;

    /**
     * <pre>
     * 访问成功后的结果真实数据
     * <b>注意：后台异常对象不能直接返回，只准返回异常消息如Throwable.getMessage</b>
     * </pre>
     */
    private E response;

    private Response(int code, E response) {
        this.setCode(code);
        this.setUuid(uuid);
        this.setResponse(response);
    }

    /**
     * <pre>
     * 给序列化工具使用 请勿在代码中直接使用
     *
     * <b>
     * 代码中使用见：
     * </b>
     * &#64;see
     * {@link #newResponse(int, Object)}
     * {@link #newSuccessResponse(Object)}
     * {@link #newFailureResponse(String)}
     * </pre>
     */
    public Response() {
    }

    public static <E> Response<E> newResponse(int code, E response) {
        return new Response<E>(code, response);
    }

    public static <E> Response<E> newSuccessResponse(E response) {
        return newResponse(200, response);
    }

    /**
     * <pre>
     * </pre>
     *
     * @param msg 失败消息
     * @return
     */
    public static Response<Throwable> newFailureResponse(int code, String msg) {
        if (code == 200) {
            code = 500;
        }
        Response<Throwable> response = new Response<Throwable>(code, null);
        response.setMsg(msg);
        return response;
    }

    /**
     * <pre>
     * </pre>
     *
     * @param msg 失败消息
     * @return
     */
    public static Response<Throwable> newFailureResponse(String msg) {
        return newFailureResponse(500, msg);
    }

    private static final ThreadLocal<Map<String, Object>> error_msg_map = new ThreadLocal<>();

    @JSONField(serialize = false)
    public final Map<String, Object> getErrorMsg() {
        Map<String, Object> map = error_msg_map.get();
        if (map == null) {
            map = new HashMap<>();
            error_msg_map.set(map);
        }
        map.put("code", this.getCode());
        map.put("msg", this.getMsg());
        map.put("uuid", this.getUuid());
        return map;
    }

    public boolean isSuccess() {
        return 200 == code;
    }

    public void setCode(int code) {
        if (this.code == 0) {
            this.code = code;
        }
    }

    public void setUuid(long uuid) {
        if (this.uuid == 0) {
            if (uuid == 0) {
                this.uuid = UUID.randomUUID().getMostSignificantBits();
            } else {
                this.uuid = uuid;
            }
        }

    }

    public void setResponse(E response) {
        if (response != null && this.response == null) {
            if (response instanceof Throwable) {
                this.msg = ((Throwable) response).getMessage();
            } else {
                this.response = response;
            }
        }
    }

    public Result<E> toResult() {
        Result<E> result = new Result<>();
        result.setCode(code);
        result.setMsg(msg);
        result.setUuid(uuid);
        result.setResponse(response);
        return result;
    }

}
