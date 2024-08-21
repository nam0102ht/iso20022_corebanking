package com.ntnn.service;

import com.ntnn.dto.DiffieHellmanRequest;
import com.ntnn.entity.KeyStorage;
import com.ntnn.exception.InvalidInputException;
import com.ntnn.repository.KeyStoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KeyStorageService {
  private final KeyStoreRepository keyStoreRepository;

  public KeyStorage getKeyStorage(DiffieHellmanRequest diffieHellmanRequest) {
    return keyStoreRepository.findById(diffieHellmanRequest.getApId()).orElseThrow(() -> new InvalidInputException("Your input with ApId not found"));
  }

}
