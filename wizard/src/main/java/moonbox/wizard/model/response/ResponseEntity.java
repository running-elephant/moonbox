package moonbox.wizard.model.response;

import lombok.Data;
import moonbox.wizard.enums.HttpCodeEnum;

@Data
@SuppressWarnings("unchecked")
public class ResponseEntity<T> {
    private int code;
    private String msg;
    private T data;


    private static ResponseEntity buildHttpCodeEnumEntry(HttpCodeEnum codeEnum) {
        ResponseEntity entity = new ResponseEntity();
        entity.code = codeEnum.getCode();
        entity.msg = codeEnum.getMessage();
        return entity;
    }

    public static <T> ResponseEntity buildOkEntry(T data) {
        ResponseEntity entity = buildHttpCodeEnumEntry(HttpCodeEnum.OK);
        entity.data = data;
        return entity;
    }

    public static ResponseEntity buildBadRequestEntry(String... msg) {
        ResponseEntity entity = buildHttpCodeEnumEntry(HttpCodeEnum.BAD_REQUEST);
        if (msg.length > 0) {
            entity.msg = msg[0];
        }
        return entity;
    }

    public static ResponseEntity buildServerErrorEntry(String... msg) {
        ResponseEntity entity = buildHttpCodeEnumEntry(HttpCodeEnum.SERVER_ERROR);
        if (msg.length > 0) {
            entity.msg = msg[0];
        }
        return entity;
    }

    public static ResponseEntity buildNotFoundEntry(String... msg) {
        ResponseEntity entity = buildHttpCodeEnumEntry(HttpCodeEnum.NOT_FOUND);
        if (msg.length > 0) {
            entity.msg = msg[0];
        }
        return entity;
    }

    public static ResponseEntity buildForbiddenEntry(String... msg) {
        ResponseEntity entity = buildHttpCodeEnumEntry(HttpCodeEnum.FORBIDDEN);
        if (msg.length > 0) {
            entity.msg = msg[0];
        }
        return entity;
    }

    public static ResponseEntity buildDuplicatedEntry(String... msg) {
        ResponseEntity entity = buildHttpCodeEnumEntry(HttpCodeEnum.DUPLICATED_RECORD);
        if (msg.length > 0) {
            entity.msg = msg[0];
        }
        return entity;
    }

    public static ResponseEntity buildOperateFailEntry(String... msg) {
        ResponseEntity entity = buildHttpCodeEnumEntry(HttpCodeEnum.OPERATE_FAIL);
        if (msg.length > 0) {
            entity.msg = msg[0];
        }
        return entity;
    }


    public static boolean isOkEntry(ResponseEntity entity) {
        return entity != null && entity.getCode() == HttpCodeEnum.OK.getCode();
    }
}
