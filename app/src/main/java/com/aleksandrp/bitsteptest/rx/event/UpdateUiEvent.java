package com.aleksandrp.bitsteptest.rx.event;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class UpdateUiEvent<T> extends BaseEvent {


    private T data;
    private boolean success;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }
}
