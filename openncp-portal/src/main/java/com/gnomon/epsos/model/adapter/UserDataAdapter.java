/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gnomon.epsos.model.adapter;

import com.gnomon.epsos.model.UserData;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author karkaletsis
 */
public class UserDataAdapter implements JsonSerializer< UserData> {

    @Override
    public JsonElement serialize(UserData t, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
 
        jsonObject.addProperty("userid", t.getUserId());       
        jsonObject.addProperty("usertype", t.getUsertype());       
        jsonObject.addProperty("emailaddress", t.getEmailaddress());       
        jsonObject.addProperty("screenname", t.getScreenname());    
        jsonObject.addProperty("status", t.getStatus());       
        jsonObject.addProperty("ret", t.getRet());       
        return jsonObject;          
    }
    
}
