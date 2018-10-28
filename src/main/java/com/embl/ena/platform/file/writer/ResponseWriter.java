package com.embl.ena.platform.file.writer;

import java.util.concurrent.CopyOnWriteArrayList;

import com.embl.ena.platform.bean.OutputBean;

@FunctionalInterface
public interface ResponseWriter {

    public void write(CopyOnWriteArrayList<OutputBean> outputList);
}
