package moonbox.wizard.enums;

public enum HttpCodeEnum {

    OK(200, "OK"),
    BAD_REQUEST(400, "Bad Request"),
    UNAUTHORIZED(401, "Unauthorized"),
    FORBIDDEN(403, "Forbidden"),
    NOT_FOUND(404, "Not Found"),
    CONFLICT(409, "Conflict"),
    SERVER_ERROR(500, "Internal Server Error"),
    DUPLICATED_RECORD(600,"Duplicated Record"),
    OPERATE_FAIL(700,"Operate Fail");

    private int code;
    private String message;

    HttpCodeEnum(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public static HttpCodeEnum codeOf(int code) {
        for (HttpCodeEnum codeEnum :values()) {
            if (codeEnum.code == code) {
                return codeEnum;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }
}
