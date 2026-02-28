package com.mysite.sbb.error;

public class ApiErrorResponse {
    private String timestamp;
    private int status;
    private String code;
    private String message;
    private String path;

    public ApiErrorResponse(String timestamp, int status, String code, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public String getTimestamp() { return timestamp; }
    public int getStatus() { return status; }
    public String getCode() { return code; }
    public String getMessage() { return message; }
    public String getPath() { return path; }
}