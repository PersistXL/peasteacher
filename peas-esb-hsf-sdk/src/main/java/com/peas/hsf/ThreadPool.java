package com.peas.hsf;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.*;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * Created by duanyihui on 2017/5/9.
 */
public class ThreadPool<T> {

    public static <T> ThreadPool<T> newPool(int fixedThread) {
        return new ThreadPool<>(fixedThread);
    }


    private ListeningExecutorService executorService;

    private List<ListenableFuture<T>> futures = Lists.newArrayList();

    private ThreadPool(int fixedThread) {
        ThreadFactoryBuilder builder = new ThreadFactoryBuilder();
        builder.setNameFormat("HSF-Thread-Pool:%s");
        executorService = MoreExecutors.listeningDecorator(Executors.newFixedThreadPool(fixedThread, builder.build()));
    }


    public void submit(Callable<T> task) {
        ListenableFuture<T> listenableFuture = executorService.submit(task);
        futures.add(listenableFuture);
    }

    public void call(Handler<T> handler) {
        call(handler, true);
    }

    public void call(Handler<T> handle, boolean autoClose) {
        ListenableFuture<List<T>> transform = Futures.transform(Futures.allAsList(futures), (List<T> objects) ->
        {
            ListenableFuture<List<T>> f = Futures.immediateFuture(objects);
            return f;
        });
        futures.clear();
        Futures.addCallback(transform, new FutureCallback<List<T>>() {
            @Override
            public void onSuccess(List<T> objects) {
                handle.handle(objects);
            }

            @Override
            public void onFailure(Throwable throwable) {
                handle.onFailure(throwable);
            }
        });
        if (autoClose) {
            shutdown();
        }
    }

    public List<T> waitReturn(boolean autoClose, Long timeout, TimeUnit unit) {
        ListenableFuture<List<T>> transform = Futures.transform(Futures.allAsList(futures), (List<T> objects) ->
        {
            ListenableFuture<List<T>> f = Futures.immediateFuture(objects);
            return f;
        });
        futures.clear();
        List<T> result = null;
        try {
            result = transform.get(timeout, unit);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (autoClose) {
            shutdown();
        }
        return result;
    }

    public void shutdown() {
        executorService.shutdown();
    }

    public static interface Handler<T> {
        void handle(List<T> result);

        void onFailure(Throwable throwable);
    }

    public static class HandlerAdapter<T> implements Handler<T> {

        @Override
        public void handle(List<T> result) {

        }

        @Override
        public void onFailure(Throwable throwable) {

        }
    }
}
