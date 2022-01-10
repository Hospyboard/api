package com.hospyboard.api.notification;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializer;
import org.springframework.lang.Nullable;

public class ResponseMapper {
    public JsonObject generateResponse(Boolean success, String message) {
        JsonObject obj = new JsonObject();
        obj.addProperty("response", success? "ok": "ko");
        obj.addProperty("message", message);
        return obj;
    }
}
