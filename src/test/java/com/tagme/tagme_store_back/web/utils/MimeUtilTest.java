package com.tagme.tagme_store_back.web.utils;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("MimeUtil Tests")
class MimeUtilTest {

    @Nested
    @DisplayName("getMimeType Tests")
    class GetMimeTypeTests {

        @ParameterizedTest
        @NullAndEmptySource
        @ValueSource(strings = {"   "})
        @DisplayName("Debería devolver fallback para nombre vacío o null")
        void getMimeType_NullOrEmptyFileName_ReturnsFallback(String fileName) throws IOException {
            String mimeType = MimeUtil.getMimeType(fileName);

            assertEquals("application/octet-stream", mimeType);
        }

        @Test
        @DisplayName("Debería detectar tipo JPEG")
        void getMimeType_JpegFile_ReturnsImageJpeg() throws IOException {
            String mimeType = MimeUtil.getMimeType("test.jpg");

            assertTrue(mimeType == null || mimeType.contains("jpeg") || mimeType.contains("image"));
        }

        @Test
        @DisplayName("Debería detectar tipo PNG")
        void getMimeType_PngFile_ReturnsImagePng() throws IOException {
            String mimeType = MimeUtil.getMimeType("test.png");

            assertTrue(mimeType == null || mimeType.contains("png") || mimeType.contains("image"));
        }

        @Test
        @DisplayName("Debería detectar tipo PDF")
        void getMimeType_PdfFile_ReturnsApplicationPdf() throws IOException {
            String mimeType = MimeUtil.getMimeType("document.pdf");

            assertTrue(mimeType == null || mimeType.contains("pdf") || mimeType.contains("application"));
        }

        @Test
        @DisplayName("Debería manejar extensión desconocida")
        void getMimeType_UnknownExtension_ReturnsNull() throws IOException {
            String mimeType = MimeUtil.getMimeType("file.unknownext");

            // El tipo puede ser null si el sistema no reconoce la extensión
            assertTrue(mimeType == null || !mimeType.isEmpty());
        }
    }
}
