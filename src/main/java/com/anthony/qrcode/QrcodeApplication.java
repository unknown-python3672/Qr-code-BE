package com.anthony.qrcode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.WriterException;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.qrcode.QRCodeWriter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

@SpringBootApplication
@RestController
@RequestMapping("/api/qrcode")
@CrossOrigin(origins = "*")
public class QrcodeApplication {

    public static void main(String[] args) {
        SpringApplication.run(QrcodeApplication.class, args);
    }

    @GetMapping(value = "qr", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> generate(
            @RequestParam String url,
            @RequestParam(defaultValue = "300") int size,
            @RequestParam(defaultValue = "false") boolean download) {

        if (!isValidUrl(url)) {
            return ResponseEntity.badRequest().build();
        }

        int safeSize = Math.max(100, Math.min(size, 1000));

        try {
            BitMatrix matrix = new QRCodeWriter()
                    .encode(url, BarcodeFormat.QR_CODE, safeSize, safeSize);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            MatrixToImageWriter.writeToStream(matrix, "PNG", outputStream);

            ResponseEntity.BodyBuilder response = ResponseEntity.ok();
            if (download) {
                response.header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"qrcode.png\"");
            }
            return response.body(outputStream.toByteArray());
        } catch (IOException | WriterException e) {
            return ResponseEntity.status(500).build();
        }
    }

    private boolean isValidUrl(String url) {
        if (url == null || url.isBlank() || url.length() > 2000) {
            return false;
        }
        try {
            URI uri = URI.create(url);
            String scheme = uri.getScheme();
            return uri.getHost() != null
                    && ("http".equalsIgnoreCase(scheme) || "https".equalsIgnoreCase(scheme));
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}