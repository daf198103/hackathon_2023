package com.hackathon.testcreator.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.hackathon.testcreator.model.TestResponse;
import com.hackathon.testcreator.service.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class TestcreatorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestResponse.class);

  @Autowired
  private TestServiceImpl service;

  @PostMapping(value = "/create/chatgpt")
  public ResponseEntity<String> createTestChatGpt(
      @RequestParam String language,
      @RequestParam String version,
      @RequestParam String seniority,
      @RequestParam String idiom) {
    if(!language.isEmpty() && !version.isEmpty() && !seniority.isEmpty()) {
      LOGGER.info("Requesting a test for {} , version {}, seniority {} ",language,version, seniority);
      String response = service.getChatGPT(language, version, seniority, idiom);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    LOGGER.info("Something went wrong");
    return new ResponseEntity<>("bad request", HttpStatus.BAD_REQUEST);
  }

  @PostMapping(value = "/evaluate")
  public ResponseEntity<String> createTestChatGpt(
      @RequestBody String resolution,
      @RequestParam String apiKey
  ) throws JsonProcessingException {
    if(!resolution.isEmpty()) {
      LOGGER.info("Evaluating the Test");
      String response = service.evaluateResolution(resolution);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    LOGGER.info("Something went wrong");
    return new ResponseEntity<>("bad request", HttpStatus.BAD_REQUEST);
  }
}
