package com.embl.ena.platform.executor;

import java.util.concurrent.ExecutionException;

@FunctionalInterface
public interface ProcessExecutor {

    public void execute(String args[]) throws InterruptedException, ExecutionException;
}
