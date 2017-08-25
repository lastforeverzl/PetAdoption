package com.zackyzhang.petadoption.api.model;

import com.google.gson.annotations.SerializedName;

/**
 * Created by lei on 8/25/17.
 */

public class HeaderBean {

    @com.google.gson.annotations.SerializedName("timestamp")
    private TimestampBean timestamp;
    @com.google.gson.annotations.SerializedName("status")
    private StatusBean status;
    @com.google.gson.annotations.SerializedName("version")
    private VersionBean version;

    public String getTimestamp() {
        return timestamp.getValue();
    }

    public void setTimestamp(TimestampBean timestamp) {
        this.timestamp = timestamp;
    }

    public String getStatus() {
        return status.getCode().getValue();
    }

    public void setStatus(StatusBean status) {
        this.status = status;
    }

    public String getVersion() {
        return version.getValue();
    }

    public void setVersion(VersionBean version) {
        this.version = version;
    }

    public static class TimestampBean {
        @com.google.gson.annotations.SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }

    public static class StatusBean {
        @SerializedName("message")
        private MessageBean message;
        @SerializedName("code")
        private CodeBean code;

        public MessageBean getMessage() {
            return message;
        }

        public void setMessage(MessageBean message) {
            this.message = message;
        }

        public CodeBean getCode() {
            return code;
        }

        public void setCode(CodeBean code) {
            this.code = code;
        }

        public static class MessageBean {
        }

        public static class CodeBean {
            @SerializedName("value")
            private String value;

            public String getValue() {
                return value;
            }

            public void setValue(String value) {
                this.value = value;
            }
        }
    }

    public static class VersionBean {

        @SerializedName("value")
        private String value;

        public String getValue() {
            return value;
        }

        public void setValue(String value) {
            this.value = value;
        }
    }


}
