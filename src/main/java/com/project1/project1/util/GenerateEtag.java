package com.project1.project1.util;



import com.fasterxml.jackson.databind.ObjectMapper;
import java.security.MessageDigest;
import java.util.Base64;
public class GenerateEtag {

    public static String generate(Object object) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        String json = mapper.writeValueAsString(object);

        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] hash = digest.digest(json.getBytes());

        return Base64.getUrlEncoder().encodeToString(hash);
    }

}
