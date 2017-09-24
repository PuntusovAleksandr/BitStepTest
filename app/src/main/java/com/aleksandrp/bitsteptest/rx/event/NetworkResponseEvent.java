package com.aleksandrp.bitsteptest.rx.event;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class NetworkResponseEvent<T> extends BaseEvent {

    private T data;
    private boolean sucess;

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean isSucess() {
        return sucess;
    }

    public void setSucess(boolean sucess) {
        this.sucess = sucess;
    }

    @Override
    public String toString() {
        return "NetworkResponseEvent{" +
                "data=" + data +
                ", sucess=" + sucess +
                '}';
    }

}
