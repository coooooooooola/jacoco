package com.swhysc.cov.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum GitWorkSpaceStatusEnum {

    EMPTY(1,"空目录"),
    EXIST(2,"目标gitUrl已存在"),
    OTHER(3,"已存在其他gitUrl");

    private Integer code;
    private String desc;

}
