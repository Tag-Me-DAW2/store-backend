package com.tagme.tagme_store_back.web.utils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.io.IOException;

public class MimeUtil {
    public static String getMimeType(String fileName) throws IOException {
        if (fileName == null || fileName.isBlank()) {
            return "application/octet-stream"; // fallback
        }
        return Files.probeContentType(Paths.get(fileName));
    }
}

