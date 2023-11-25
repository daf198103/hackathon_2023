package com.hackathon.testcreator.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;

import java.net.URL;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class TestServiceImpl implements TestService{

  @Override
  public String getChatGPT(String language, String version, String apiKey_, String seniority) {
      String url = "https://api.openai.com/v1/chat/completions";
      String apiKey = apiKey_;
      String model = "gpt-3.5-turbo";

      String message = "create me only one " + seniority +" code challenge algorithm in "
          + language + " " + version
          + " with a story prompt of 50 words contextualizing the test and 80% parts "
          + " in blank to be filled by the user and without the main "
          + " and separate the story the code challenge  and the instructions"
          + " into distinct paragraphs in the response";

      StringBuffer response = new StringBuffer();

      try {

        URL url_ = new URL(url);
        HttpURLConnection con = (HttpURLConnection) url_.openConnection();
        con.setRequestMethod("POST");
        con.setRequestProperty("Authorization", "Bearer " + apiKey);
        con.setRequestProperty("Content-Type", "application/json");

        String body = "{\"model\": \"" + model + "\", \"messages\": [{\"role\": \"user\", \"content\": \"" + message + "\"}]}";
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

      //return response.toString();
    return extractMessage(response.toString());
  }

//  @Override
//  public BardAnswer getBard(String language, String version, String seniority) {
//
////    String message = "create me only one " + seniority +" code challenge algorithm in "
////        + language + " " + version
////        + " with a story prompt of 50 words contextualizing the test and 80% parts "
////        + " in blank to be filled by the user and without the main "
////        + " and separate the story and the code challenge into distinct paragraphs in the response";
//
////    String token = "dQj5qAlvj5TuHL6w3AE_BK-vn1jJfDPe_JcbesIPRrEiAbgWJvBBb86l2mMGsBwWhcp5Xw";
////    AIClient client = new GoogleBardClient(token, Duration.ofMinutes(10));
////
////    Answer answer = client.ask("create me only one mid code challenge algorithm in Java 17 with a story prompt of 50 words contextualizing the test and 80% parts in blank to be filled by the user and without the main and separate the story and the code challenge into distinct paragraphs in the response\n");
////
////    return new BardAnswer(answer.chosenAnswer(), answer.draftAnswers());
//
//    HashMap<String, String> headers = new HashMap<>();
//    headers.put("x-goog-api-key", System.getEnv("PALM_API_KEY"));
//
//    InstantiatingGrpcChannelProvider provider = InstantiatingGrpcChannelProvider.newBuilder()
//        .setHeaderProvider(FixedHeaderProvider.create(headers))
//        .build();
//
//    DiscussServiceSettings settings = DiscussServiceSettings.newBuilder()
//        .setTransportChannelProvider(provider)
//        .setCredentialsProvider(FixedCredentialsProvider.create(null))
//        .build();
//
//    DiscussServiceClient client = DiscussServiceClient.create(settings);
//
//
//
//  }


  public static String extractMessage(String jsonResponse) {
    try {
      ObjectMapper objectMapper = new ObjectMapper();
      JsonNode rootNode = objectMapper.readTree(jsonResponse);

      JsonNode messageNode = rootNode
          .path("choices")
          .path(0)  // Assuming there is always at least one choice
          .path("message");

      return messageNode.isMissingNode() ? "" : messageNode.toString();
    } catch (Exception e) {
      throw new RuntimeException("Error parsing JSON response", e);
    }
  }
}
