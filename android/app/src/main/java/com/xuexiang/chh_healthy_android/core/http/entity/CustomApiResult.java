
package com.xuexiang.chh_healthy_android.core.http.entity;

import com.xuexiang.xhttp2.model.ApiResult;

public class CustomApiResult<T extends CommonResponse> extends ApiResult<T> {
    private T result;

    @Override
    public int getCode() {
        return Integer.parseInt(result.getHead().getCode());
    }

    @Override
    public String getMsg() {
        if (result.getHead().getCode().equals("0")) {
            return "成功";
        } else {
            return result.getHead().getMessage();
        }
    }

    @Override
    public T getData() {
        return result;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess();
    }
}
