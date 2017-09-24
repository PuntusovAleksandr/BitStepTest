package com.aleksandrp.bitsteptest.rx.event;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class NetworkRequestEvent<T> extends BaseEvent {

    private T data;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
