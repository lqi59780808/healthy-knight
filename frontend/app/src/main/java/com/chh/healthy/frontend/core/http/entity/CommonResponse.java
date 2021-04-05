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
import com.xuexiang.xutil.common.StringUtils;

import java.io.Serializable;

/**
 * @class CommonResponse
 * @classdesc
 * @author liang_xiaojian
 * @date 2020/8/26  14:36
 * @version 1.0.0
 * @see
 * @since
 */
public class CommonResponse<T> extends ApiResult<T> implements Serializable{

    private static final long serialVersionUID = -6372561804247815227L;

    public static final class Head implements Serializable {

        private static final long serialVersionUID = 9068029931352525287L;
        // 应用程序版本，必填
        private String version;
        // 应答码，0 代表成功，失败则填写异常错误码
        private String code;
        // 消息的显示全部服务端定义
        private String message;
        // 加密标志，1标记加密 0不加密
        private Integer flag = 0;

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getCode() {
            return code;
        }

        public void setCode(String code) {
            this.code = code;
        }

        public String getMessage() {
            return message;
        }

        public void setMessage(String message) {
            this.message = message;
        }

        public Integer getFlag() {
            return flag;
        }

        public void setFlag(Integer flag) {
            this.flag = flag;
        }
    }

    private Head head;

    private T body;

    public CommonResponse() {
        this.head = new Head();
    }

    public Head getHead() {
        return head;
    }

    public void setHead(Head head) {
        this.head = head;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }

    @Override
    public String toString() {
        return "CommonResponse{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }

    public static <T> CommonResponseBuilder<T> builder() {
        return new CommonResponseBuilder<>();
    }

    public static final class CommonResponseBuilder<T> {

        private final Head head;

        private T body;

        private CommonResponseBuilder() {
            this.head = new Head();
        }

        public CommonResponseBuilder<T> withVersion(String version) {
            this.head.setCode(version);
            return this;
        }

        public CommonResponseBuilder<T> withCode(String code) {
            this.head.setCode(code);
            return this;
        }

        public CommonResponseBuilder<T> withMessage(String message) {
            this.head.setMessage(message);
            return this;
        }

        public CommonResponseBuilder<T> withFlag(int flag) {
            this.head.setFlag(flag);
            return this;
        }

        public CommonResponseBuilder<T> withBody(T body) {
            this.body = body;
            return this;
        }

        /**
         * Build result.
         *
         * @return result
         */
        public CommonResponse<T> build() {
            CommonResponse<T> commonResponse = new CommonResponse<>();
            commonResponse.setHead(head);
            if (StringUtils.isEmpty(commonResponse.getHead().getVersion())) {
                commonResponse.getHead().setVersion("0.0.1");
            }
            commonResponse.setBody(body);
            return commonResponse;
        }
    }

    @Override
    public int getCode() {
        return Integer.parseInt(head.getCode());
    }

    @Override
    public String getMsg() {
        return head.getMessage();
    }

    @Override
    public T getData() {
        return body;
    }

    @Override
    public boolean isSuccess() {
        return super.isSuccess();
    }
}
