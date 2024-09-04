package com.colak.springtutorial.stringvalue.config;

import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

@UtilityClass
public class DecompressUtil {

    private static final int BUFFER_SIZE = 4096;

    public static byte[] decompress(byte[] bytes) {
        byte[] decoded = Base64.getDecoder().decode(new String(bytes));

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(decoded);
             GZIPInputStream gzipInputStream = new GZIPInputStream(byteArrayInputStream)) {
            byte[] buff = new byte[BUFFER_SIZE];
            int n;

            // decompress
            while ((n = gzipInputStream.read(buff, 0, BUFFER_SIZE)) > 0) {
                byteArrayOutputStream.write(buff, 0, n);
            }
            // deserialize
            return byteArrayOutputStream.toByteArray();
        } catch (Exception exception) {
            throw new SerializationException("Gzip deserialization error", exception);
        }
    }
}
