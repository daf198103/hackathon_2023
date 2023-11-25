package com.hackathon.testcreator.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TestService {

  String getChatGPT(String language, String version, String apiKey_, String seniority);

  String evaluateResolution(String resolution, String apiKey_) throws JsonProcessingException;

}
