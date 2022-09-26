package com.abc.eatwell.common;

import lombok.Data;
import java.util.HashMap;
import java.util.Map;

/**
 * common return result class
 * @param <T>
 */
@Data
public class R<T> {

    private Integer code; // code: 1 is success, 0 and other numbers are error

    private String msg; // error message

    private T data; // data

    private Map map = new HashMap(); // dynamic data

    public static <T> R<T> success(T object) {
        R<T> r = new R<T>();
        r.data = object;
        r.code = 1;
        return r;
    }

    public static <T> R<T> error(String msg) {
        R r = new R();
        r.msg = msg;
        r.code = 0;
        return r;
    }

    public R<T> add(String key, Object value) {
        this.map.put(key, value);
        return this;
    }

}
