package com.example.cne_shop.myException;

import java.io.IOException;

/**
 * Created by 博 on 2017/7/1.
 */

public class GSON_ANALYZE_MESSAGE_FAILURE extends IOException {
    private static final long serialVersionUID = -6547904276907101598L;

    public GSON_ANALYZE_MESSAGE_FAILURE() {
        super();
    }

    public GSON_ANALYZE_MESSAGE_FAILURE(String message) {
        super(message);
    }

    public GSON_ANALYZE_MESSAGE_FAILURE(String message, Throwable cause) {
        super(message, cause);
    }

    public GSON_ANALYZE_MESSAGE_FAILURE(Throwable cause) {
        super(cause);
    }
}
