package com.aleksandrp.bitsteptest.presenter.interfaces;

/**
 * Created by AleksandrP on 24.09.2017.
 */

public abstract class BasePresenter {

    protected MvpView mvpView;

    // TODO: 26.03.16 Refactor this method.
    public abstract void init();

    public void destroy(){
        mvpView = null;
    };

    public MvpView getMvpView() {
        return mvpView;
    }

    public void setMvpView(MvpView mvpView) {
        this.mvpView = mvpView;
    }


}