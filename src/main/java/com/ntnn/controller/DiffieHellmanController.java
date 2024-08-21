package com.ntnn.controller;

import com.ntnn.dto.DiffieHellmanRequest;
import com.ntnn.dto.DiffieHellmanResponse;
import com.ntnn.helper.XmlParser;
import com.ntnn.service.DiffieHellmanService;
import io.github.resilience4j.ratelimiter.annotation.RateLimiter;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.Callable;

@RestController
@RequestMapping("/diffieHellman")
@RequiredArgsConstructor
@CrossOrigin
public class DiffieHellmanController {
  private static final Logger LOG = LoggerFactory.getLogger(TransferMoneyController.class);

  private final DiffieHellmanService diffieHellmanService;

  @RateLimiter(name = "externalService")
  @PostMapping(value = "/exchangeKey", produces = MediaType.APPLICATION_JSON_VALUE)
  public Callable<ResponseEntity<DiffieHellmanResponse>> diffieHellman(@RequestBody DiffieHellmanRequest diffieHellmanRequest) {
    return () -> {
      return new ResponseEntity<>(diffieHellmanService.diffieHellman(diffieHellmanRequest), HttpStatus.OK);
    };
  }

}
