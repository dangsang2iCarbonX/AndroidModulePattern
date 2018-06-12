package com.icarbonx.smartdevice.http;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 *
 * @param <T>
 */
public abstract class AbstractObserver<T> implements Observer<T> {

    public abstract void onSuccess(T t);
    public void onFailure(Throwable throwable){}

    @Override
    public void onSubscribe(Disposable d) {
    }

    @Override
    public void onNext(T t) {
        onSuccess(t);
    }

    @Override
    public void onError(Throwable e) {
        onFailure(e);
    }

    @Override
    public void onComplete() {

    }
}
