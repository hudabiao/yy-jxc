/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 *
 * https://www.renren.io
 *
 * 版权所有，侵权必究！
 */

package io.renren.enums;

/**
 * 菜单类型枚举
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public enum OperateTypeEnum {
    /**
     * 入库
     */
    IMPORT(1),
    /**
     * 出库
     */
    EXPORT(2);

    private int value;

    OperateTypeEnum(int value) {
        this.value = value;
    }

    public int value() {
        return this.value;
    }
}
