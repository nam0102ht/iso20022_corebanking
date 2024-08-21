package com.ntnn.service;

import com.ntnn.dto.DiffieHellmanRequest;
import com.ntnn.entity.KeyStorage;
import com.ntnn.exception.InvalidInputException;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class ValidateSignatureService {

  private final DiffieHellmanService diffieHellmanService;
  private final KeyStorageService keyStorageService;

  public void verifyRequest(HttpHeaders httpServletRequest) {
    String authorization = httpServletRequest.get("Authorization").get(0);
    String[] bearsToken = authorization.split(" ");
    if (bearsToken.length < 2) {
      throw new InvalidInputException("Input not add header and authorization");
    }
    if (!bearsToken[0].equals("Bearer")) {
      throw new InvalidInputException("Authorization must be Bear Token");
    }
    String token = bearsToken[1];
    log.info("Token: {}", token);
    JSONObject jsonObject = new JSONObject(new String(Base64.getDecoder().decode(token)));
    String apId = jsonObject.getString("apId");
    if (StringUtils.isEmpty(apId)) {
      throw new InvalidInputException("ApId is null");
    }
    DiffieHellmanRequest diffieHellmanRequest = DiffieHellmanRequest.builder()
        .apId(apId)
        .salt(jsonObject.getString("salt"))
        .pepper(jsonObject.getString("pepper"))
        .secretKey(jsonObject.getString("secretKey"))
        .build();
    KeyStorage keyStorage = keyStorageService.getKeyStorage(diffieHellmanRequest);
    if (!diffieHellmanService.verify(keyStorage, jsonObject.getString("dataSign"), diffieHellmanRequest.getSecretKey())) {
      throw new InvalidInputException("Token receive couldn't verify");
    }
  }

}
