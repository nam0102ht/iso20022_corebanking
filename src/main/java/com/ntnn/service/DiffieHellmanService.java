package com.ntnn.service;


import com.ntnn.dto.AgronDto;
import com.ntnn.dto.DiffieHellmanRequest;
import com.ntnn.dto.DiffieHellmanResponse;
import com.ntnn.entity.KeyStorage;
import com.ntnn.exception.InvalidInputException;
import com.ntnn.exception.TechnicalError;
import com.ntnn.exception.TechnicalException;
import com.ntnn.helper.SecurityHelper;
import com.ntnn.repository.KeyStoreRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.KeyAgreement;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.SignatureException;
import java.security.spec.EncodedKeySpec;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

@Service
@RequiredArgsConstructor
@Slf4j
public class DiffieHellmanService {
  
  private final KeyStoreRepository keyStoreRepository;

  @Transactional
  public DiffieHellmanResponse diffieHellman(DiffieHellmanRequest request) {
    try {
      ECNamedCurveParameterSpec parameterSpec = ECNamedCurveTable.getParameterSpec("secp256r1");

      KeyPairGenerator keyGen = KeyPairGenerator.getInstance("EC", "BC");
      keyGen.initialize(parameterSpec);
      KeyPair keyPair = keyGen.generateKeyPair();

      Key privateKey = keyPair.getPrivate();
      // Derive shared secret

      byte[] privateCodeBytes = privateKey.getEncoded();
      String privateKeyString = Base64.getEncoder().encodeToString(privateCodeBytes);

      byte[] publicKeyBytes = Base64.getDecoder().decode(request.getPublicKey());
      EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(publicKeyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
      PublicKey publicKey = keyFactory.generatePublic(publicKeySpec);


      KeyAgreement keyAgreement = KeyAgreement.getInstance("ECDH", "BC");
      keyAgreement.init(privateKey);
      keyAgreement.doPhase(publicKey, true);
      byte[] sharedSecret = keyAgreement.generateSecret();
      String sharedSecretBytes = Base64.getEncoder().encodeToString(sharedSecret);

      KeyStorage keyStorage = new KeyStorage();
      keyStorage.setPrivateKey(privateKeyString);
      AgronDto agronDto = SecurityHelper.passwordAgron2Encrypt(sharedSecretBytes);
      keyStorage.setSecretKey(agronDto.getPassword());
      keyStorage.setPepper(agronDto.getPepper());
      keyStorage.setSalt(agronDto.getSalt());
      keyStorage.setPublicKeyClient(Base64.getEncoder().encodeToString(publicKey.getEncoded()));

      // Return the public key to the client
      byte[] publicKeyBytesServer = keyPair.getPublic().getEncoded();
      String publicKeyString = Base64.getEncoder().encodeToString(publicKeyBytesServer);

      keyStorage.setPublicKeyServer(publicKeyString);
      KeyStorage storage = keyStoreRepository.save(keyStorage);

      return DiffieHellmanResponse.builder()
          .apiId(storage.getId())
          .salt(agronDto.getSalt())
          .publicKeySever(publicKeyString)
          .publicKey(Base64.getEncoder().encodeToString(publicKey.getEncoded()))
          .pepper(agronDto.getPepper())
          .secretKey(sharedSecretBytes)
          .build();
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidAlgorithmParameterException exception) {
      throw new TechnicalException(TechnicalError.TECHNICAL_ERROR.getErrorCode(), TechnicalError.TECHNICAL_ERROR.getMessage());
    } catch (InvalidKeyException e) {
      throw new RuntimeException(e);
    } catch (InvalidKeySpecException e) {
      throw new RuntimeException(e);
    }
  }

  public String signature(String apId) {
    try {
      KeyStorage keyStorage = keyStoreRepository.findById(apId).orElseThrow(() -> {
        throw new InvalidInputException("Invalid input apId");
      });

      byte[] privatKeyBytes = Base64.getDecoder().decode(keyStorage.getPrivateKey());
      PrivateKey severPrivateKey = KeyFactory.getInstance("EC", "BC")
          .generatePrivate(new PKCS8EncodedKeySpec(privatKeyBytes));

      // Sign the message using shared secret
      Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
      signature.initSign(severPrivateKey);
      byte[] signatureBytes = signature.sign();
      JSONObject jsonObject = new JSONObject();
      jsonObject.put("apId", keyStorage.getId());
      jsonObject.put("keyAgreement", keyStorage.getSecretKeyAgreement());
      jsonObject.put("dataSign", Base64.getEncoder().encodeToString(signatureBytes));
      jsonObject.put("salt", keyStorage.getSalt());
      jsonObject.put("pepper", keyStorage.getPepper());
      jsonObject.put("secretKey", keyStorage.getSecretKey());

      return Base64.getEncoder().encodeToString(jsonObject.toString().getBytes(StandardCharsets.UTF_8));
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | SignatureException |
             InvalidKeyException exception) {
      log.error("Exception={}", exception.getMessage(), exception);
      throw new TechnicalException("999", exception.getMessage());
    }
  }

  public boolean verify(KeyStorage keyStorage, String dataSigned, String sharedSecret) {
    try {
      byte[] publicKeyBytes = Base64.getDecoder().decode(keyStorage.getPublicKeyClient());
      PublicKey publicKey = decodePublicKey(publicKeyBytes);

      // Verify signature
      byte[] dataSignedBytes = Base64.getDecoder().decode(dataSigned);
      return verifySignature(dataSignedBytes, Base64.getDecoder().decode(sharedSecret), publicKey);
    } catch (NoSuchAlgorithmException | NoSuchProviderException | InvalidKeySpecException | SignatureException | InvalidKeyException exception) {
      throw new TechnicalException(TechnicalError.TECHNICAL_ERROR.getErrorCode(), TechnicalError.TECHNICAL_ERROR.getMessage());
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  private static boolean verifySignature(byte[] signatureBytes, byte[] data, PublicKey publicKey) throws Exception {
    Signature signature = Signature.getInstance("SHA256withECDSA", "BC");
    signature.initVerify(publicKey);
    signature.update(data);
    return signature.verify(signatureBytes);
  }

  private static PublicKey decodePublicKey(byte[] keyBytes) throws Exception {
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
    KeyFactory keyFactory = KeyFactory.getInstance("EC", "BC");
    return keyFactory.generatePublic(keySpec);
  }


}
