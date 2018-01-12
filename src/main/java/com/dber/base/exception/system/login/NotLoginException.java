package com.dber.base.exception.system.login;

import com.dber.base.exception.BaseException;
import com.dber.base.exception.FrameworkException;

/**
 * <li>文件名称: NotLoginException.java</li>
 * <li>修改记录: ...</li>
 * <li>内容摘要: ...</li>
 * <li>其他说明: ...</li>
 *
 * @author dev-v
 * @version 1.0
 * @since 2017年12月21日
 */
public class NotLoginException extends FrameworkException {

    private static final long serialVersionUID = 8117046608888737346L;

    private static final int code = 600;

    static {
        BaseException.registCode(code, NotLoginException.class);
    }

    public NotLoginException() {
        super(code, "你还没有登录，无法获取相应数据，请先登录！");
    }

}
