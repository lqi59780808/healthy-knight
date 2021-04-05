
/*
 * Copyright (C) 2021 xuexiangjys(xuexiangjys@163.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.chh.healthy.frontend.core.http.entity;

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
