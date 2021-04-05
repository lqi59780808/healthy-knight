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

import java.io.Serializable;

/**
 * @class CommonRequest
 * @classdesc
 * @author liang_xiaojian
 * @date 2020/8/27  22:48
 * @version 1.0.0
 * @see
 * @since
 */
public class CommonRequest<T> implements Serializable {

    private static final long serialVersionUID = 4432305443028998723L;

    private Head head;

    private T body;

    public static final class Head implements Serializable {
        private static final long serialVersionUID = -8134386938077724762L;
        /**
         * 应用程序版本
         */
        private String version;

        /**
         * 全局流水-用户工号-YYYYMMDDHHMMSS，这个流水号记录于调用链中
         */
        private String gMessage;

        /**
         * 服务之间发起请求的时候填写
         */
        private String rMessage;

        /**
         * 依据该字段区分 body 对象类型
         */
        private String bodyType;

        /**
         * 设备号
         */
        private String deviceId;

        /**
         * 设备类型
         */
        private Integer deviceType;

        /**
         * 例如 android 4.0 chrome 7
         * Ios 6.0 chrome 6
         */
        private String osAndBrowser;

        /**
         * 1标记加密 0不加密
         * 默认为 0
         */
        private Integer encryptFlag = 0;

        public Head() {
            // non-args constructor
            gMessage = "xxxxxx";
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }

        public String getgMessage() {
            return gMessage;
        }

        public void setgMessage(String gMessage) {
            this.gMessage = gMessage;
        }

        public String getrMessage() {
            return rMessage;
        }

        public void setrMessage(String rMessage) {
            this.rMessage = rMessage;
        }

        public String getBodyType() {
            return bodyType;
        }

        public void setBodyType(String bodyType) {
            this.bodyType = bodyType;
        }

        public String getDeviceId() {
            return deviceId;
        }

        public void setDeviceId(String deviceId) {
            this.deviceId = deviceId;
        }

        public Integer getDeviceType() {
            return deviceType;
        }

        public void setDeviceType(Integer deviceType) {
            this.deviceType = deviceType;
        }

        public String getOsAndBrowser() {
            return osAndBrowser;
        }

        public void setOsAndBrowser(String osAndBrowser) {
            this.osAndBrowser = osAndBrowser;
        }

        public Integer getEncryptFlag() {
            return encryptFlag;
        }

        public void setEncryptFlag(Integer encryptFlag) {
            this.encryptFlag = encryptFlag;
        }
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
        return "CommonRequest{" +
                "head=" + head +
                ", body=" + body +
                '}';
    }


}
