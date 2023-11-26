package com.hackathon.testcreator.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService{

  @Value("${app.api_key}")
  private String apiKey_;

  @Override
  public String getChatGPT(String language, String version, String seniority, String idiom) {
      String url = "https://api.openai.com/v1/chat/completions";
      String model = "gpt-3.5-turbo";

      String message = "create me only one " + seniority +" code challenge algorithm in "
          + language + " " + version
          + " with a story prompt of 50 words contextualizing the test with only the"
          + " method signature to be implemented by the candidate."
          + " Separate the story, the code challenge  and the instructions"
          + " into distinct paragraphs and give one response example.";

      String mensagem = "crie me somente 1 teste de algoritmo nível " + seniority
          + " em " + language + "na versão" + version + "com um enunciado de 50 palavras "
          + " contextualizando o teste e com apenas a assinatura do método que deverá ser"
          + " implementado  pelo candidato.  Separe o teste e as instruções em parágrafos"
          + " distintos e de um exemplo da saída desejada.";


      StringBuffer response = new StringBuffer();

      try {

        URL url_ = new URL(url);
        HttpURLConnection con = (HttpURLConnection) url_.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + apiKey_);
        con.setRequestProperty("Content-Type", "application/json");
        String body;
        if(idiom.isBlank() || idiom == null) {
           body =
              "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \""
                  + message + "\"}]}";
        } else {
          body =
              "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \""
                  + mensagem + "\"}]}";
        }
        con.setDoOutput(true);
        OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
        writer.write(body);
        writer.flush();
        writer.close();

        BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        while ((inputLine = reader.readLine()) != null) {
          response.append(inputLine);
        }
        reader.close();

      } catch (Exception e){

        throw new RuntimeException(e);
      }

    return formatStory(extractMessage(response.toString()));
  }

  @Override
  public String evaluateResolution(String resolution)
      throws JsonProcessingException {
    String url = "https://api.openai.com/v1/chat/completions";
    String model = "gpt-3.5-turbo";

    ObjectMapper objectMapper = new ObjectMapper();
    String message = objectMapper.writeValueAsString(
        Map.of("model", model,
            "messages", List.of(Map.of("role", "user", "content",
                "Can you evaluate this code "+ resolution + " and return a percentage of corretness?")))
    );

    StringBuffer response = new StringBuffer();
    try {

      URL url_ = new URL(url);
      HttpURLConnection con = (HttpURLConnection) url_.openConnection();
      con.setRequestMethod("POST");
      con.setRequestProperty("Authorization", "Bearer " + apiKey_);
      con.setRequestProperty("Content-Type", "application/json");
      con.setDoOutput(true);
      OutputStreamWriter writer = new OutputStreamWriter(con.getOutputStream());
      writer.write(message);
      writer.flush();
      writer.close();

      BufferedReader reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
      String inputLine;
      while ((inputLine = reader.readLine()) != null) {
        response.append(inputLine);
      }
      reader.close();

    } catch (Exception e){

      throw new RuntimeException(e);
    }

    return extractMessage(response.toString());
  }


  public static String extractMessage(String jsonResponse) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(jsonResponse);

      JsonNode messageNode = rootNode
          .path("choices")
          .path(0)
          .path("message");

      return messageNode.isMissingNode() ? "" : messageNode.toString();
    } catch (Exception e) {
      throw new RuntimeException("Error parsing JSON response", e);
    }
  }

  public static String formatStory(String input) {
    String formattedText = input.replaceAll("\n", " ")
        .replaceAll("\\s+", " ")
        .replaceAll("\n```", " ")
        .replaceAll("\"", " ");

    return formattedText.trim();
  }
}
