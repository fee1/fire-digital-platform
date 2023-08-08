package com.huajie.domain.entity;

import java.io.Serializable;

public class SysDicValue implements Serializable {
    private Integer id;

    private String dicCode;

    private Integer valueNo;

    private String valueCode;

    private String valueName;

    private static final long serialVersionUID = 1L;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDicCode() {
        return dicCode;
    }

    public void setDicCode(String dicCode) {
        this.dicCode = dicCode;
    }

    public Integer getValueNo() {
        return valueNo;
    }

    public void setValueNo(Integer valueNo) {
        this.valueNo = valueNo;
    }

    public String getValueCode() {
        return valueCode;
    }

    public void setValueCode(String valueCode) {
        this.valueCode = valueCode;
    }

    public String getValueName() {
        return valueName;
    }

    public void setValueName(String valueName) {
        this.valueName = valueName;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", dicCode=").append(dicCode);
        sb.append(", valueNo=").append(valueNo);
        sb.append(", valueCode=").append(valueCode);
        sb.append(", valueName=").append(valueName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}