package com.hackathon.testcreator.utils;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class JsonFormatter {

  public static String format (String jsonString) {
    ObjectMapper objectMapper = new ObjectMapper();
    String content = "";
    StringBuilder resultBuilder = new StringBuilder();
    try {
      JsonNode jsonNode = objectMapper.readTree(jsonString);
      content = jsonNode.get("content").asText();
      content = content.replace("\n", "");
      content = content.replace("```", "\n\n");
      String[] parts = content.split("(Story Prompt:|Code Challenge:|Instructions:|Response Example:)");
      for (String part : parts) {
        resultBuilder.append(part.trim()).append("\n");
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return resultBuilder.toString();
  }
}
