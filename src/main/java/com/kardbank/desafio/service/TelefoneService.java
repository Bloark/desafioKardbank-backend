package com.kardbank.desafio.service;

import org.apache.http.client.fluent.Content;
import org.apache.http.client.fluent.Request;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;
import java.io.IOException;

@Service
public class TelefoneService {

    private static final String API_KEY = "ab4907d4c9b04707a1243074f2254e69";
    private static final String API_URL = "https://phonevalidation.abstractapi.com/v1/";

    public boolean validarTelefone(String telefone) {
        try {
            String requestUrl = API_URL + "?api_key=" + API_KEY + "&phone=" + telefone;
            Content content = Request.Get(requestUrl).execute().returnContent();
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode jsonNode = objectMapper.readTree(content.asString());
            boolean isValid = jsonNode.get("valid").asBoolean();
            return isValid;
        } catch (IOException error) {
            System.out.println(error);
            return false;
        }
    }
}
