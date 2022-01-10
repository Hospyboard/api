package com.hospyboard.api.notification;

import com.google.gson.JsonObject;

public class ResponseMapper {
    public JsonObject generateResponse(Boolean success, String message) {
        JsonObject obj = new JsonObject();
        obj.addProperty("response", success? "ok": "ko");
        obj.addProperty("message", message);
        return obj;
    }
}
