package com.easternblu.khub.common.rx;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;

import com.easternblu.khub.common.util.Converter;
import com.easternblu.khub.common.util.Maps;
import com.easternblu.khub.common.util.Strings;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import io.reactivex.subjects.PublishSubject;
import timber.log.Timber;

/**
 * 1) Call {@link #get()} to get the singleton and {@link #getSubject(Class)} and {@link Subject#subscribe(Class, Action)} and give it a unique object for a subject shared globally
 * <p>
 * 2) Or Call {@link #subscribe(Class, Action)} to subscribe with your {@link Action}
 * <p>
 * 3) Call {@link Subscription#dispose()} or {@link #dispose(Subscription)} if you no longer want to receive event
 * <p>
 * Created by pan on 15/11/17.
 */
public class RxBus {
    public static boolean DEBUG = false;
    public static final String TAG = RxBus.class.getSimpleName();
    private static volatile RxBus SINGLETON = null;


    public static RxBus get() {
        RxBus r = SINGLETON;
        if (r == null) {
            synchronized (RxBus.class) {    // while we were waiting for the lock, another
                r = SINGLETON;       // thread may have instantiated the object
                if (r == null) {
                    r = new RxBus();
                    SINGLETON = r;
                }
            }
        }
        return r;
    }


    private static Map<Object, int[]> DEBUG_COUNTS = new HashMap<>();

    private static synchronized void onSubscribe(Object key) {
        int[] ints = DEBUG_COUNTS.get(key);
        if (ints == null) {
            DEBUG_COUNTS.put(key, ints = new int[2]);
        }
        ints[0]++;
    }

    private static synchronized void onDispose(Object key) {
        int[] ints = DEBUG_COUNTS.get(key);
        if (ints == null) {
            DEBUG_COUNTS.put(key, ints = new int[2]);
        }
        ints[1]++;
    }

    public synchronized static void printDebug(int logLevel) {


        List<Pair<Object, Integer>> debugCounts = Maps.toList(DEBUG_COUNTS, new Converter<Pair<Object, int[]>, Pair<Object, Integer>>() {
            @Override
            public Pair<Object, Integer> convert(Pair<Object, int[]> from) {
                return new Pair<>(from.first, from.second[0] - from.second[1]);
            }
        });

        Comparator temp = new Comparator<Pair<Object, Integer>>() {
            @Override
            public int compare(Pair<Object, Integer> o1, Pair<Object, Integer> o2) {
                return -1 * o1.second.compareTo(o2.second);
            }
        };


        Collections.sort(debugCounts, temp);
        String msg = Strings.repeat("=", 50) + "\n";
        for (Pair<Object, Integer> debugCount : debugCounts) {
            if (debugCount.second > 0) {
                msg += (debugCount.first.toString() + " usage: " + (debugCount.second) + "\n");
            }
        }

    }


    private Map<Object, Subject> buses;

    private RxBus() {
        buses = Collections.synchronizedMap(new HashMap<Object, Subject>());
    }


    /**
     * Get a {@link Subject} shared globally across the app
     *
     * @param key
     * @return
     */
    @NonNull
    public synchronized Subject getSubject(Class<?> key) {
        Subject bus = buses.get(key);
        if (bus == null) {
            buses.put(key, bus = new Subject(key));
        }
        return bus;
    }

    /**
     * Handy method to subscribe to a class.
     * <p>
     * All object of that class send to RxBus will also call {@link Action#onEvent(Object)}
     *
     * @param clazz
     * @param action
     * @param <A>
     * @return
     */
    public static <A> Subscription subscribe(Class<A> clazz, Action<A> action) {
        return get().getSubject(clazz).subscribe(action);
    }


    public static <A> Subscription subscribe2(Class<A> clazz) {
        return null;
    }



    /**
     * Handy method to send a event to the subject
     *
     * @param object
     * @param <A>
     */
    public static <A> void send(A object) {
        //get().getSubject(object.getClass()).onNext(object);
        send(object.getClass(), object);
    }

    /**
     * Handy method to send a event to the subject
     *
     * @param keyClass
     * @param object
     * @param <A>
     */
    public static <A> void send(Class<? extends A> keyClass, A object) {
        get().getSubject(keyClass).onNext(object);
    }


    /**
     * Handy method to dispose or unsubscribe a subscription
     *
     * @param subscription
     */
    public static void dispose(Subscription subscription) {
        if (subscription != null) {
            subscription.dispose();
        }
    }


    /**
     * A Map based class to group one or more Subscription so for disposing them
     * together
     */
    public static class Group extends HashMap<Object, Subscription> {

        /**
         * Calls {@link #subscribe(Class, Action)} and {@link Map#put(Object, Object)}
         * the {@link Subscription} in itself. So the {@link Subscription} can be managed by this group
         * and calling {@Link disposeAll} will dispose all these subscriptions
         * <p>
         * Existing {@link Subscription} with matching class will be remove and dispose
         *
         * @param clazz
         * @param listener
         * @param <A>
         * @return
         */
        public synchronized <A> Subscription subscribe(Class<A> clazz, Action<A> listener) {
            RxBus.dispose(remove(clazz));
            Subscription temp;
            put(clazz, temp = RxBus.subscribe(clazz, listener));
            return temp;
        }

        /**
         * Call {@link Subscription#dispose()} for the {@link Subscription} matching the class param
         *
         * @param clazz
         */
        public synchronized void dispose(Class<?> clazz) {
            Subscription existing = remove(clazz);
            if (existing != null) {
                existing.dispose();
            }
        }

        /**
         * Call {@link Subscription#dispose()} for all {@link Subscription} in this map
         * <p>
         * Afterward, it will remove all items
         */
        public synchronized void disposeAll() {
            for (Object key : this.keySet()) {
                Subscription subscription = get(key);
                if (subscription != null) {
                    subscription.dispose();
                }
            }
            clear();
        }
    }


    /**
     * Get Subject (internally backed by {@link PublishSubject})
     *
     * @param <A>
     */
    public static class Subject<A> {

        @NonNull
        protected PublishSubject<A> subject;
        protected Object key;

        public Subject(@NonNull Object key) {
            this.key = key;
            this.subject = PublishSubject.create();
        }

        @NonNull
        public synchronized Subscription subscribe(final Action<A> action) {
            if (RxBus.DEBUG) {
                Timber.d("subscribe(key=" + key + ") action = " + action.toString());
                RxBus.onSubscribe(key);
            }
            return new Subscription(key, toFlowable().subscribe(action));
        }


        protected Flowable<A> toFlowable() {
            return subject.toFlowable(BackpressureStrategy.BUFFER)
                    .subscribeOn(Schedulers.computation())
                    .observeOn(AndroidSchedulers.mainThread());
        }


        public void onNext(@NonNull A object) {
            if (object == null) {
                throw new NullPointerException("Cannot send " + object);
            }
            subject.onNext(object);
        }
    }


    /**
     * A container class for {@link Disposable} so that your {@link Action} can be removed
     */
    public static class Subscription {
        @Nullable
        private Disposable disposable;
        protected Object debugKey;

        public Subscription(@Nullable Object debugKey, @NonNull Disposable disposable) {
            this.disposable = disposable;
            this.debugKey = debugKey;
        }

        public void dispose() {
            if (disposable != null && !disposable.isDisposed()) {
                disposable.dispose();
                if (debugKey != null && DEBUG) {
                    onDispose(debugKey);
                }
            }
        }

        @Nullable
        public Disposable getDisposable() {
            return disposable;
        }
    }


    /**
     * Callback when a new object is sent
     *
     * @param <A>
     */
    public abstract static class Action<A> implements Consumer<A> {

        public Action() {
        }

        @Override
        public final void accept(A obj) throws Exception {
            onEvent(obj);
        }

        /**
         * Implement this.
         * <p>
         * This is the callback
         *
         * @param obj
         * @throws Exception
         */
        protected abstract void onEvent(A obj) throws Exception;
    }
}
