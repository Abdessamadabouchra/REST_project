package com.project1.project1.filters;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.*;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

/**
 * PRO Compression filter:
 * - full Accept-Encoding q negotiation (gzip/deflate)
 * - Vary: Accept-Encoding
 * - buffer small responses to decide compression
 * - skip 204/304 and responses with no body
 * - compress only specific MIME types
 */
@Component
public class CompressionFilter implements Filter {

    // Minimum size (bytes) to consider compression
    private static final int MIN_COMPRESS_SIZE = 1024; // 1 KB

    // MIME types we allow to compress (simple subset)
    private static final Set<String> COMPRESSIBLE_MIME_PREFIXES = Set.of(
            "text/",
            "application/json",
            "application/xml",
            "application/javascript",
            "application/xhtml+xml"
    );

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        if (!(request instanceof HttpServletRequest) || !(response instanceof HttpServletResponse)) {
            chain.doFilter(request, response);
            return;
        }

        HttpServletRequest  httpReq = (HttpServletRequest) request;
        HttpServletResponse httpRes = (HttpServletResponse) response;

        // Only compress safe HTTP methods (GET, HEAD)
        String method = httpReq.getMethod();
        if (!"GET".equalsIgnoreCase(method) && !"HEAD".equalsIgnoreCase(method)) {
            chain.doFilter(request, response);
            return;
        }

        String acceptEncoding = Optional.ofNullable(httpReq.getHeader("Accept-Encoding")).orElse("");
        // If client explicitly says identity or empty -> no compression
        if (acceptEncoding.isBlank() || headerContainsToken(acceptEncoding, "identity")) {
            chain.doFilter(request, response);
            return;
        }

        // Buffer response
        BufferingResponseWrapper buffered = new BufferingResponseWrapper(httpRes);
        try {
            chain.doFilter(request, buffered);
        } finally {
            // After controller processed, examine response
            int status = buffered.getStatus();
            if (status == HttpServletResponse.SC_NO_CONTENT || status == HttpServletResponse.SC_NOT_MODIFIED) {
                // No body expected
                buffered.commitToResponse();
                return;
            }

            byte[] content = buffered.getContentAsBytes();
            if (content == null || content.length == 0) {
                buffered.commitToResponse();
                return;
            }

            // Only compress certain mime types
            String contentType = Optional.ofNullable(buffered.getContentType()).orElse("");
            if (!isCompressibleMime(contentType)) {
                buffered.commitToResponse();
                return;
            }

            // Evaluate client preferences (gzip vs deflate)
            double gzipQ = getQuality(acceptEncoding, "gzip");
            double deflateQ = getQuality(acceptEncoding, "deflate");

            // if both zero => client doesn't accept these
            if (gzipQ == 0 && deflateQ == 0) {
                buffered.commitToResponse();
                return;
            }

            // If content small -> don't compress
            if (content.length < MIN_COMPRESS_SIZE) {
                buffered.commitToResponse();
                return;
            }

            // Choose algo by q
            String chosen;
            if (gzipQ >= deflateQ) chosen = "gzip"; else chosen = "deflate";

            // Prepare response headers
            httpRes.setHeader("Vary", "Accept-Encoding");
            httpRes.setHeader("Content-Encoding", chosen);
            httpRes.setHeader("Content-Length", null); // remove content-length, will be chunked

            // copy status and content-type and other headers (except content-length and content-encoding)
            httpRes.setStatus(buffered.getStatus());
            if (buffered.getContentType() != null) httpRes.setContentType(buffered.getContentType());
            buffered.copyHeadersTo(httpRes);

            // Write compressed bytes
            try (ServletOutputStream out = httpRes.getOutputStream()) {
                if ("gzip".equalsIgnoreCase(chosen)) {
                    try (GZIPOutputStream gz = new GZIPOutputStream(out)) {
                        gz.write(content);
                    }
                } else {
                    try (DeflaterOutputStream def = new DeflaterOutputStream(out)) {
                        def.write(content);
                    }
                }
                out.flush();
            }
        }
    }

    // --- helpers ---

    private static boolean headerContainsToken(String headerValue, String token) {
        String[] parts = headerValue.split(",");
        for (String p : parts) {
            if (p.trim().startsWith(token)) return true;
        }
        return false;
    }

    /**
     * Parse Accept-Encoding and return q-value for given algo (0..1).
     * If algo not present returns 0.
     * If present without q, returns 1.0.
     * Supports patterns like: gzip;q=1.0, deflate;q=0.5, *;q=0.1
     */
    private static double getQuality(String acceptEncodingHeader, String algo) {
        double bestWildcard = -1.0;
        double bestAlgo = -1.0;
        String[] parts = acceptEncodingHeader.split(",");
        for (String raw : parts) {
            String p = raw.trim();
            if (p.isEmpty()) continue;
            String[] tok = p.split(";");

            String encoding = tok[0].trim().toLowerCase(Locale.ROOT);
            double q = 1.0;
            if (tok.length > 1) {
                for (int i = 1; i < tok.length; i++) {
                    String part = tok[i].trim();
                    if (part.startsWith("q=")) {
                        try {
                            q = Double.parseDouble(part.substring(2));
                        } catch (NumberFormatException ignored) { q = 0.0; }
                    }
                }
            }

            if ("*".equals(encoding)) {
                bestWildcard = Math.max(bestWildcard, q);
            } else if (encoding.equalsIgnoreCase(algo)) {
                bestAlgo = Math.max(bestAlgo, q);
            }
        }
        if (bestAlgo >= 0) return bestAlgo;
        if (bestWildcard >= 0) return bestWildcard;
        return 0.0;
    }

    private static boolean isCompressibleMime(String contentType) {
        if (contentType == null) return false;
        String ct = contentType.toLowerCase(Locale.ROOT);
        for (String prefix : COMPRESSIBLE_MIME_PREFIXES) {
            if (ct.startsWith(prefix)) return true;
        }
        return false;
    }
}

