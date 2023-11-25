package com.hackathon.testcreator.service;

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
  public String getTest(String language, String version) {
      String url = "https://api.openai.com/v1/chat/completions";
      String apiKey = "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";
      String model = "gpt-3.5-turbo";

      String message = "create me only one senior code challenge algorithm in "
          + language + " " + version
          + " with a story prompt of 50 words contextualizing the test and 80% parts "
          + " in blank to be filled by the user and without the main "
          + " and separate the story and the code challenge into json levels in the response";
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

    return response.toString();
  }
}
