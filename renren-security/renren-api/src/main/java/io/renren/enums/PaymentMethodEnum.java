/**
 * Copyright (c) 2018 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package io.renren.enums;

/**
 * 菜单类型枚举
 *
 * @author Mark sunlightcs@gmail.com
 * @since 1.0.0
 */
public enum PaymentMethodEnum {
    /**
     * 微信
     */
    WECHAT(1, "微信"),
    /**
     * 支付宝
     */
    ALIPAY(2, "支付宝"),
    /**
     * 现金
     */
    CASH(3, "现金"),
    /**
     * 其他
     */
    OTHER(4, "其他");

    private int value;
    private String name;

    PaymentMethodEnum(int value, String name) {
        this.value = value;
        this.name = name;
    }

    public int value() {
        return this.value;
    }

    public int getValue() {
        return value;
    }

    public String getName() {
        return name;
    }

    public static String getByValue(Integer value) {
        for (PaymentMethodEnum methodEnum : PaymentMethodEnum.values()) {
            if(methodEnum.value==value){
                return methodEnum.getName();
            }
        }
        return "";
    }
}
