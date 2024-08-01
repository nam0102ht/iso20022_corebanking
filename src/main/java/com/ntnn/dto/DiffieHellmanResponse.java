package com.ntnn.dto;

public class DiffieHellmanResponse {
  private String publicKey;
  public DiffieHellmanResponse(String publicKey) {
    this.publicKey = publicKey;
  }
  public String getPublicKey() {
    return publicKey;
  }
  public void setPublicKey(String publicKey) {
    this.publicKey = publicKey;
  }
}