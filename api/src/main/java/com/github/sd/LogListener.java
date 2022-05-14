package com.github.sd;

public interface LogListener
    {
        LogListener Blank = new LogListener() { };

        default void serverResponded(int code, long delta)
            { }

        default void contentRead(boolean gzip, long delta, int read)
            { }
    }
