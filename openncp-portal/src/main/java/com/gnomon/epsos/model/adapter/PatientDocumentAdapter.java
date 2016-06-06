/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.gnomon.epsos.model.adapter;

import com.gnomon.epsos.model.PatientDocument;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import java.lang.reflect.Type;

/**
 *
 * @author karkaletsis
 */
public class PatientDocumentAdapter implements JsonSerializer<PatientDocument> {

    @Override
    public JsonElement serialize(PatientDocument t, Type type, JsonSerializationContext jsc) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("repositoryid", t.getRepositoryId());
        jsonObject.addProperty("hcid", t.getHcid());
        jsonObject.addProperty("uuid", t.getUuid());
        jsonObject.addProperty("title", t.getTitle());
        String fileType = "xml";
        String formatCode = t.getFormatCode().getValue();
        if (formatCode.contains("pdf")) {
            fileType = "pdf";
        }
        jsonObject.addProperty("fileType", fileType);
        return jsonObject;
    }

}
