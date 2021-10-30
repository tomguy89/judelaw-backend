package com.guava.judelaw.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.iv.RandomIvGenerator;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.password.Pbkdf2PasswordEncoder;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class EncryptUtil {

    private static final PooledPBEStringEncryptor aesEncoder = new PooledPBEStringEncryptor();
    private static final Pbkdf2PasswordEncoder encoder = new Pbkdf2PasswordEncoder();

    private EncryptUtil() { initEncoder(); }

    private static void initEncoder() {
        if(!aesEncoder.isInitialized()) {
            aesEncoder.setAlgorithm("PBEWithHmacSHA256AndAES_256");
            aesEncoder.setProviderName("SunJCE");
            aesEncoder.setStringOutputType("base64");
            aesEncoder.setIvGenerator(new RandomIvGenerator());
            aesEncoder.setKeyObtentionIterations(1000);
            aesEncoder.setPoolSize(1);
        }
    }

    private static PasswordEncoder getEncoder() {
        encoder.setAlgorithm(Pbkdf2PasswordEncoder.SecretKeyFactoryAlgorithm.PBKDF2WithHmacSHA256);
        return encoder;
    }

    public static String encrypt(String plainString) {
        try {
            return aesEncoder.encrypt(plainString);
        } catch (EncryptionOperationNotPossibleException e) {
            log.debug(e.getMessage());
            return plainString;
        }
    }

    public static String decrypt(String encryptedString) {
        try {
            return aesEncoder.decrypt(encryptedString);
        } catch (EncryptionOperationNotPossibleException e) {
            log.debug(e.getMessage());
            return encryptedString;
        }
    }

    public static String encryptPassword(String plainText) {
        try {
            return getEncoder().encode(plainText);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return plainText;
        }
    }

    public static boolean checkPassword(String rawPassword, String encodedPassword) {
        try {
            return getEncoder().matches(rawPassword, encodedPassword);
        } catch (Exception e) {
            log.debug(e.getMessage());
            return false;
        }
    }
}
