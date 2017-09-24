package com.aleksandrp.bitsteptest.presenter.interfaces;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public interface PresenterEventListener {

    /**
     * Method register event lisener. for Presenter.
     * Called in onStart for Activity or Fragment
     * Called in ,OnCreate for Service
     */
    void registerSubscriber();

    /**
     * Method unregister event lisener. for Presenter.
     * Called in onStop for Activity or Fragment
     * Called in OnDestroy for Service
     */
    void unRegisterSubscriber();
}
