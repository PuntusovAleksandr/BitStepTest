package com.aleksandrp.bitsteptest.rx;

import rx.Observable;
import rx.subjects.PublishSubject;
import rx.subjects.SerializedSubject;
import rx.subjects.Subject;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public class BusProvider {

    private static final Subject<Object, Object> eventBus = new SerializedSubject<>(PublishSubject.create());

    public static void send(Object o) {
        eventBus.onNext(o);
    }

    public static Observable<Object> observe() {
        return eventBus;
    }

}
