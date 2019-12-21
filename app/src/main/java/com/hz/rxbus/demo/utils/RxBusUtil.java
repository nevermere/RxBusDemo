package com.hz.rxbus.demo.utils;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import io.reactivex.subjects.Subject;

/**
 * 描述:RxBus工具类
 *
 * @author fzJiang
 * @date 2019-04-10
 */
public class RxBusUtil {

    private static volatile RxBusUtil INSTANCE;

    private final Subject<Object> mSubject;

    private Disposable mDisposable;

    private RxBusUtil() {
        mSubject = PublishSubject.create().toSerialized();
    }

    private static class Holder {
        private static final RxBusUtil BUS = new RxBusUtil();
    }

    public static RxBusUtil get() {
        if (INSTANCE == null) {
            synchronized (RxBusUtil.class) {
                if (INSTANCE == null) {
                    INSTANCE = Holder.BUS;
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 注册无背压
     *
     * @param clazz    事件
     * @param consumer Consumer
     */
    @SuppressWarnings("unchecked")
    public void subscribe(Class clazz, Consumer consumer) {
        mDisposable = toObservable(clazz)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(consumer);
    }

    private <T> Observable<T> toObservable(Class<T> classType) {
        return mSubject.ofType(classType);
    }

    /**
     * 取消订阅
     */
    public void unSubscribe() {
        if (mDisposable != null && mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
    }

    /**
     * 判断是否注册无背压RxBus
     *
     * @return true/false
     */
    private boolean hasSubscribers() {
        return mSubject.hasObservers();
    }

    /**
     * 发送无背压消息
     *
     * @param obj 消息
     */
    public void post(Object obj) {
        if (hasSubscribers()) {
            mSubject.onNext(obj);
        }
    }
}