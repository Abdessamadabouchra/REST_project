package com.project1.project1.filters;

import jakarta.servlet.ServletOutputStream;
import jakarta.servlet.WriteListener;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletResponseWrapper;

import java.io.*;
import java.util.*;

/**
 * Wrapper that buffers everything written to the response (bytes).
 * After filter/controller finishes, you can inspect bytes and decide to compress.
 */
public class BufferingResponseWrapper extends HttpServletResponseWrapper {

    private final ByteArrayOutputStream buffer = new ByteArrayOutputStream();
    private final CachedServletOutputStream cachedOutputStream = new CachedServletOutputStream(buffer);
    private PrintWriter cachedWriter;
    private final Map<String, List<String>> headerMap = new HashMap<>();
    private int status = HttpServletResponse.SC_OK;
    private String contentType;

    public BufferingResponseWrapper(HttpServletResponse response) {
        super(response);
    }

    @Override
    public ServletOutputStream getOutputStream() {
        return cachedOutputStream;
    }

    @Override
    public PrintWriter getWriter() throws IOException {
        if (cachedWriter == null) {
            cachedWriter = new PrintWriter(new OutputStreamWriter(buffer, getCharacterEncoding()), true);
        }
        return cachedWriter;
    }

    @Override
    public void setContentType(String type) {
        this.contentType = type;
        super.setContentType(type); // keep for downstream
    }

    @Override
    public String getContentType() {
        return this.contentType == null ? super.getContentType() : this.contentType;
    }

    @Override
    public void setHeader(String name, String value) {
        headerMap.put(name, new ArrayList<>(List.of(value)));
        super.setHeader(name, value);
    }

    @Override
    public void addHeader(String name, String value) {
        headerMap.computeIfAbsent(name, k -> new ArrayList<>()).add(value);
        super.addHeader(name, value);
    }

    public void copyHeadersTo(HttpServletResponse out) {
        // copy all headers except Content-Length and Content-Encoding (we manage them)
        for (Map.Entry<String, List<String>> e : headerMap.entrySet()) {
            String name = e.getKey();
            if ("Content-Length".equalsIgnoreCase(name) || "Content-Encoding".equalsIgnoreCase(name)) continue;
            for (String v : e.getValue()) {
                out.addHeader(name, v);
            }
        }
    }

    @Override
    public void setStatus(int sc) {
        this.status = sc;
        super.setStatus(sc);
    }

    @Override
    public int getStatus() {
        return this.status;
    }

    public byte[] getContentAsBytes() {
        try {
            if (cachedWriter != null) cachedWriter.flush();
            cachedOutputStream.flush();
        } catch (IOException ignored) {}
        return buffer.toByteArray();
    }

    /**
     * If we decide NOT to compress, write the buffered content back to the real response
     */
    public void commitToResponse() {
        try {
            HttpServletResponse resp = (HttpServletResponse) getResponse();
            resp.setStatus(this.getStatus());
            if (this.getContentType() != null) resp.setContentType(this.getContentType());
            copyHeadersTo(resp);
            // write bytes
            byte[] bytes = getContentAsBytes();
            resp.setContentLength(bytes.length);
            ServletOutputStream out = resp.getOutputStream();
            out.write(bytes);
            out.flush();
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }

    // Inner cached ServletOutputStream writing into buffer
    private static class CachedServletOutputStream extends ServletOutputStream {
        private final OutputStream target;

        public CachedServletOutputStream(OutputStream target) {
            this.target = target;
        }

        @Override
        public void write(int b) throws IOException {
            target.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            target.write(b, off, len);
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setWriteListener(WriteListener writeListener) {
            // sync only
        }
    }
}
