package com.pkasemer.spacelounge.Models;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Generated;

@Generated("jsonschema2pojo")
public class OrderResponse {

    private Boolean error;
    private String message;
    private Map<String, Object> additionalProperties = new HashMap<String, Object>();

    public Boolean getError() {
        return error;
    }

    public void setError(Boolean error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Map<String, Object> getAdditionalProperties() {
        return this.additionalProperties;
    }

    public void setAdditionalProperty(String name, Object value) {
        this.additionalProperties.put(name, value);
    }

}