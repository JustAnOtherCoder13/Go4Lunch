package com.picone.core.utils;

import io.reactivex.Scheduler;

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
