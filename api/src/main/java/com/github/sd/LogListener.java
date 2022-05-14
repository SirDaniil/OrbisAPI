package com.github.sd;

public interface LogListener
    {
        LogListener Blank = new LogListener() { };

        default void serverResponded(long delta)
            { }

        default void contentRead(long delta)
            { }
    }
