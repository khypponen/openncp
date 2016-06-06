/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.gnomon.epsos.model.adapter;

import com.gnomon.epsos.model.Patient;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author karkaletsis
 */
public class PatientAdapter implements JsonSerializer<Patient> {

    @Override
    public JsonElement serialize(Patient t, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("root", t.getRoot());       
        jsonObject.addProperty("extension", t.getExtension());       
        jsonObject.addProperty("familyname", t.getFamilyName());
        jsonObject.addProperty("firstname", t.getName());
        jsonObject.addProperty("address", t.getAddress());
        jsonObject.addProperty("postalcode", t.getPostalCode());
        jsonObject.addProperty("city", t.getCity());
        jsonObject.addProperty("country", t.getCountry());
        return jsonObject;          
    }
    
}
