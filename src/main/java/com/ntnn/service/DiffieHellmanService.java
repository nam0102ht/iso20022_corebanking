package com.ntnn.service;


import com.ntnn.dto.DiffieHellmanResponse;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.springframework.stereotype.Service;

import javax.crypto.KeyAgreement;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.Base64;

@Service
public class DiffieHellmanService {

  public DiffieHellmanResponse diffieHellman() throws NoSuchAlgorithmException, NoSuchProviderException, InvalidAlgorithmParameterException, InvalidKeyException {
    ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("secp256r1");

    KeyPairGenerator keyGen = KeyPairGenerator.getInstance("ECDH", "BC");
    keyGen.initialize(parameterSpec);
    KeyPair keyPair = keyGen.generateKeyPair();

    KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
    Key privateKey = keyPair.getPrivate();
    keyAgreement.init(privateKey);

    byte[] privateCodeBytes = privateKey.getEncoded();
    String privateKeyString = Base64.getEncoder().encodeToString(privateCodeBytes);

    // Return the public key to the client
    byte[] publicKeyBytes = keyPair.getPublic().getEncoded();
    String publicKeyString = Base64.getEncoder().encodeToString(publicKeyBytes);
    return new DiffieHellmanResponse(publicKeyString);
  }
}
