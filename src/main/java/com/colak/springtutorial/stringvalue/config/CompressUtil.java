package com.colak.springtutorial.stringvalue.config;

import lombok.experimental.UtilityClass;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.GZIPOutputStream;

@UtilityClass
public class CompressUtil {

    public static byte[] compress(byte[] bytes) {

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {

            gzipOutputStream.write(bytes);
            gzipOutputStream.finish();

            byte[] result = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encode(result);
        } catch (Exception exception) {
            throw new SerializationException("Gzip Serialization Error", exception);
        }
    }
}
