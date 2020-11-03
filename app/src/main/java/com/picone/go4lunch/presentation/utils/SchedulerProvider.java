package com.picone.go4lunch.presentation.utils;

import io.reactivex.Completable;
import io.reactivex.CompletableSource;
import io.reactivex.CompletableTransformer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;

public class SchedulerProvider {

    private Scheduler io;
    private Scheduler ui;

    public SchedulerProvider(Scheduler io, Scheduler ui) {
        this.io = io;
        this.ui = ui;
    }

    public Scheduler getIo() {
        return io;
    }

    public Scheduler getUi() {
        return ui;
    }
}
