package com.github.sd;

public interface LogListener
    {
        LogListener Blank = new LogListener() { };

        default void serverResponded(int code, long delta)
            { }

        default void contentRead(boolean compressed, long delta, int read, String encoding)
            { }

        default void sent(long delta)
            { }
    }
