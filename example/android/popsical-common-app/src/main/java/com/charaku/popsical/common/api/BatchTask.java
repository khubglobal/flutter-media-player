package com.easternblu.khub.common.api;

import androidx.annotation.Nullable;
import android.util.Pair;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 *
 */
public abstract class BatchTask<ID, K> {
    public enum Mode {
        ALL_REQUIRED, AT_LEAST_ONE
    }

    @Nullable
    private Map<ID, Pair<K, Throwable>> results = Collections.synchronizedMap(new HashMap<ID, Pair<K, Throwable>>());
    private Task[] tasks;
    private Mode mode;

    public BatchTask(Mode mode) {
        this.mode = mode;
    }

    protected synchronized void onTaskSuccess(ID id, K responseObject) {
        results.put(id, new Pair<K, Throwable>(responseObject, null));
        checkTasks();
    }


    protected synchronized void onTaskApplicationError(ID id, Throwable throwable) {
        results.put(id, new Pair<K, Throwable>(null, throwable));
        checkTasks();
    }


    protected synchronized void onTaskConnectionFailure(ID id, Throwable throwable) {
        results.put(id, new Pair<K, Throwable>(null, throwable));
        checkTasks();
    }


    protected abstract void onSuccess(Map<ID, K> result);

    protected abstract void onError(Map<ID, Throwable> errors);


    protected synchronized void checkTasks() {
        if (results.size() == tasks.length) {
            try {
                Map<ID, Throwable> errors = new HashMap<>();
                Map<ID, K> successes = new HashMap<>();
                for (ID id : results.keySet()) {
                    Pair<K, Throwable> result = results.get(id);
                    if (result.first != null) {
                        successes.put(id, result.first);
                    } else if (result.second != null) {
                        errors.put(id, result.second);
                    } else {
                        throw new IllegalArgumentException("Must specify a mode");
                    }
                }
                // end of loop either all passed or all failed
                if (mode == Mode.ALL_REQUIRED) {
                    if (successes.size() == tasks.length)
                        onSuccess(successes);
                    else
                        onError(errors);
                } else if (mode == Mode.AT_LEAST_ONE) {
                    if (successes.size() >= 1)
                        onSuccess(successes);
                    else
                        onError(errors);
                } else {
                    throw new IllegalArgumentException("Must specify a mode");
                }


            } finally {
                results.clear();
                tasks = null;
            }
        }
    }

    public synchronized boolean isBatchTaskRunning() {
        return tasks != null && (results == null || results.size() < tasks.length);
    }

    public void execute(List<Task> tasks) throws BatchTaskRunningException {
        execute(tasks.toArray(new Task[0]));
    }

    public synchronized void execute(final Task... tasks) throws BatchTaskRunningException {
        if (tasks.length == 0) {
            throw new BatchTaskRunningException("Specify at least one task");
        }
        if (isBatchTaskRunning()) {
            throw new BatchTaskRunningException((this.tasks.length - results.size()) + " still running ");
        }
        this.tasks = tasks;
        for (Task task : this.tasks) {
            task.asyncCall(new RetrofitApi.EnqueueCallback<ID, K>() {
                @Override
                public void onSuccess(ID id, K responseObject) {
                    onTaskSuccess(id, responseObject);
                }

                @Override
                public void onApplicationError(ID id, Throwable throwable) {
                    onTaskApplicationError(id, throwable);
                }

                @Override
                public void onConnectionFailure(ID id, Throwable throwable) {
                    onTaskConnectionFailure(id, throwable);
                }
            });
        }
    }

    public interface Task<ID, K> {
        void asyncCall(RetrofitApi.EnqueueCallback<ID, K> enqueueCallback);
    }

    public static class BatchTaskRunningException extends Exception {
        public BatchTaskRunningException(String message) {
            super(message);
        }
    }

}
