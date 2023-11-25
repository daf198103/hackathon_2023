package com.hackathon.testcreator.controller;

import com.hackathon.testcreator.model.TestResponse;
import com.hackathon.testcreator.service.TestServiceImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "api/v1")
public class TestcreatorController {

  private static final Logger LOGGER = LoggerFactory.getLogger(TestResponse.class);

  @Autowired
  private TestServiceImpl service;


  @PostMapping(value = "/create")
  public ResponseEntity<String> createTest(
      @RequestParam String language,
      @RequestParam String version,
      @RequestParam String apiKey) {
    if(!language.isEmpty() && !version.isEmpty()) {
      LOGGER.info("Requesting a test for {} , version {}",language,version);
      String response = service.getTest(language, version, apiKey);
      return new ResponseEntity<>(response, HttpStatus.CREATED);
    }
    LOGGER.info("Something went wrong");
    return new ResponseEntity<>("bad request", HttpStatus.BAD_REQUEST);
  }

}
