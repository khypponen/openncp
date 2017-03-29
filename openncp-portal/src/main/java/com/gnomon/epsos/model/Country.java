package com.gnomon.epsos.model;

import java.io.Serializable;

public class Country implements Serializable {

    private static final long serialVersionUID = 487748229692071744L;
    
    private String name;
    private String code;

    public Country(String name, String code) {
        this.name = name;
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
