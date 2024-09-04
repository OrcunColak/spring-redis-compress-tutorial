package com.colak.springtutorial.stringvalue.config;

import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.SerializationException;

// Custom RedisSerializer
public class CompressRedis extends GenericJackson2JsonRedisSerializer {

    @Override
    public byte[] serialize(Object graph) throws SerializationException {

        // serialize
        byte[] bytes = super.serialize(graph);

        // compress
        return CompressUtil.compress(bytes);
    }

    @Override
    public Object deserialize(byte[] bytes) throws SerializationException {
        if (bytes == null || bytes.length == 0) {
            return null;
        }

        // decompress
        byte[] decoded = DecompressUtil.decompress(bytes);

        // deserialize
        return super.deserialize(decoded);
    }
}
