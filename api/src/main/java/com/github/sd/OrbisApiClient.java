package com.github.sd;

import java.nio.*;
import org.java_websocket.handshake.*;

/**
 * Created by Daniil Sosonkin
 * 4/4/2018 9:25 PM
 */
public interface OrbisApiClient
    {
        void onOpen(ServerHandshake handshakedata);

        void onMessage(String message);

        void onClose(int code, String reason, boolean remote);

        void onError(Exception ex);

        default void onMessage(ByteBuffer bytes)
            { }

        default String getEndpoint()
            {
                return "/stream";
            }
    }
