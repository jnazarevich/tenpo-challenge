package com.tenpo.challenge.configuration.logging;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

public class CachedServletOutputStream extends ServletOutputStream {
    private final static Logger LOGGER = LoggerFactory.getLogger(CachedServletOutputStream.class);
    private final OutputStream cachedOutputStream;
    private final ByteArrayOutputStream byteArrayOutputStream;

    public CachedServletOutputStream(OutputStream cachedOutputStream) {
        this.cachedOutputStream = cachedOutputStream;
        this.byteArrayOutputStream = new ByteArrayOutputStream();
    }

    @Override
    public boolean isReady() {
        return true;
    }

    @Override
    public void setWriteListener(WriteListener writeListener) {

    }

    @Override
    public void write(int b) throws IOException {
        this.cachedOutputStream.write(b);
        this.byteArrayOutputStream.write(b);
    }

    public byte[] getCopy() {
        return this.byteArrayOutputStream.toByteArray();
    }
}
