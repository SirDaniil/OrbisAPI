package com.github.sd;

import java.io.*;

public class CountingInputStream extends InputStream
    {
        private InputStream in;
        private int bytes;
        private int marked;

        public CountingInputStream(InputStream in)
            {
                this.in = in;
            }

        public int available() throws IOException {
            return in.available();
        }

        public boolean markSupported() {
            return in.markSupported();
        }

        public int read() throws IOException {
            int r = in.read();
            if (r > 0) {
                bytes++;
            }
            return r;
        }

        public int read(byte[] b, int off, int len) throws IOException {
            int r = in.read(b, off, len);
            if (r > 0) {
                bytes += r;
            }
            return r;
        }

        public long skip(long skipped) throws IOException {
            long l = in.skip(skipped);
            if (l > 0) {
                bytes += l;
            }
            return l;
        }

        public void mark(int readlimit) {
            in.mark(readlimit);
            marked = bytes;
        }

        public void reset() throws IOException {
            in.reset();
            bytes = marked;
        }

        public void close() throws IOException {
            in.close();
        }

        public int getBytes()
            {
                return bytes;
            }
    }
