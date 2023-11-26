package com.hackathon.testcreator.service;

import com.fasterxml.jackson.core.JsonProcessingException;

public interface TestService {

  String getChatGPT(String language, String version, String seniority, String idiom);

  String evaluateResolution(String resolution) throws JsonProcessingException;

}
