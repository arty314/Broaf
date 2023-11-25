package com.example.broaf;

import androidx.annotation.MainThread;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.util.concurrent.atomic.AtomicBoolean;

public class SingleLiveEvent<T> extends MutableLiveData<T> {

    private final AtomicBoolean mPending = new AtomicBoolean(false);

    @Override
    public void observe(LifecycleOwner owner, Observer<? super T> observer) {
        if (hasActiveObservers()) {
            // 이미 옵저버가 활성 상태이면 제거한다.
            removeObserver(observer);
        }
        super.observe(owner, new Observer<T>() {
            @Override
            public void onChanged(T t) {
                if (mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }

    @MainThread
    public void setValue(T value) {
        mPending.set(true);
        super.setValue(value);
    }

    /**
     * 사용하지 않는다면 null로 설정
     */
    @MainThread
    public void call() {
        setValue(null);
    }
}