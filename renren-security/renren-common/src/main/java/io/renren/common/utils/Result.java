/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.common.utils;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.io.Serializable;

/**
 * 响应数据
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
@ApiModel(value = "响应")
public class Result<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 编码：0表示成功，其他值表示失败
     */
    @ApiModelProperty(value = "编码：0表示成功，其他值表示失败")
    private int code = 10000;
    /**
     * 消息内容
     */
    @ApiModelProperty(value = "消息内容")
    private String msg = "success";
    /**
     * 响应数据
     */
    @ApiModelProperty(value = "响应数据")
    private T data;

    public Result(int code, String msg, T data) {
        this.code = code;
        this.msg = msg;
        this.data = data;
    }

    public Result(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Result(int code, T data) {
        this.code = code;
        this.data = data;
    }

    public Result() {
    }

    public static <T> Result<T> ok(T data) {
        return new Result<>(10000, data);
    }

    public static <T> Result<T> ok() {
        return new Result<>(10000, "");
    }

    public static <T> Result<T> fail(String msg) {
        return new Result<>(20000, msg);
    }


    public static <T> Result<T> fail(String msg, int code) {
        return new Result<>(code, msg);
    }

    public boolean success(){
        return code == 10000;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
