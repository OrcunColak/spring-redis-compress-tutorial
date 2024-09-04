package com.colak.springtutorial.stringvalue.config;

import com.colak.springtutorial.model.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Base64;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

// Custom RedisSerializer
public class CompressRedis extends GenericJackson2JsonRedisSerializer {
    public static final int BUFFER_SIZE = 4096;
    private final JacksonRedisSerializer<User> jacksonRedisSerializer;

    public CompressRedis() {
        this.jacksonRedisSerializer = getValueSerializer();
    }

    @Override
    public byte[] serialize(Object graph) throws SerializationException {
        if (graph == null) {
            return new byte[0];
        }

        // serialize
        byte[] bytes = jacksonRedisSerializer.serialize(graph);
        assert bytes != null;

        try (ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
             GZIPOutputStream gzipOutputStream = new GZIPOutputStream(byteArrayOutputStream)) {
            // compress

            gzipOutputStream.write(bytes);
            gzipOutputStream.finish();

            byte[] result = byteArrayOutputStream.toByteArray();

            return Base64.getEncoder().encode(result);
        } catch (Exception exception) {
            throw new SerializationException("Gzip Serialization Error", exception);
        }
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

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
            return jacksonRedisSerializer.deserialize(byteArrayOutputStream.toByteArray());
        } catch (Exception exception) {
            throw new SerializationException("Gzip deserialization error", exception);
        }
    }

    private static JacksonRedisSerializer<User> getValueSerializer() {
        JacksonRedisSerializer<User> jackson2JsonRedisSerializer = new JacksonRedisSerializer<>(User.class);

        ObjectMapper mapper = new ObjectMapper();
        jackson2JsonRedisSerializer.setObjectMapper(mapper);
        return jackson2JsonRedisSerializer;
    }
}
