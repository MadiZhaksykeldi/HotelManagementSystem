package com.example.demo.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class Response<T> {

    @ApiModelProperty(notes = "Response status ", required = true)
    private String status;

    @ApiModelProperty(notes = "Response message ", required = true)
    private T message;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public T getMessage() {
        return message;
    }

    public void setMessage(T message) {
        this.message = message;
    }
}
