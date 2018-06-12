package com.karigar.hubgaclient.RestApi;

import android.database.Observable;

import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class ApiCallback<T> implements Observer<T> {

    private void dismiss(){

    }

    @Override
    public void onSubscribe(@io.reactivex.annotations.NonNull Disposable d) {

    }

    @Override
    public void onNext(@io.reactivex.annotations.NonNull T t) {

    }

    @Override
    public void onError(@io.reactivex.annotations.NonNull Throwable e) {
        dismiss();
    }

    @Override
    public void onComplete() {
          dismiss();
    }
}
