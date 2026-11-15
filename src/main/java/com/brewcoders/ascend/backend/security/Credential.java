package com.brewcoders.ascend.backend.security;

import java.security.GeneralSecurityException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.security.MessageDigest;
import java.security.SecureRandom;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

/* Purpose: Keep a password verifier instead of keeping the original password.
 */
public class Credential {
    private static final int ITERATIONS = 210000;
    private final byte[] salt;
    private final byte[] passwordHash;

    /* Purpose: Validate the password and create a different random salt for each account.
     */
    public Credential(String password) {
        validatePassword(password);
        salt = new byte[16];
        new SecureRandom().nextBytes(salt);
        passwordHash = calculateHash(password);
    }

    /* Purpose: Apply the existing frontend's 8-64 character, no-whitespace rules.
     */
    private void validatePassword(String password) {
        if (password == null || password.length() < 8 || password.length() > 64) {
            throw new IllegalArgumentException("Password must contain 8-64 characters.");
        }
        for (int i = 0; i < password.length(); i++) {
            if (Character.isWhitespace(password.charAt(i))) {
                throw new IllegalArgumentException("Password must not contain whitespace.");
            }
        }
    }

    /* Replaces current in-memory missions.
     */
    public boolean matches(String password) {
        if (password == null || password.length() < 8 || password.length() > 64) {
            return false;
        }
        return MessageDigest.isEqual(passwordHash, calculateHash(password));
    }

    /* Use Java's built-in PBKDF2 password algorithm
     */
    private byte[] calculateHash(String password) {
        PBEKeySpec specification = new PBEKeySpec(password.toCharArray(), salt, ITERATIONS, 256);
        try {
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            return factory.generateSecret(specification).getEncoded();
        } catch (GeneralSecurityException e) {
            throw new IllegalStateException("Password verification is unavailable. Please try again.", e);
        } finally {
            specification.clearPassword();
        }
    }

    /* Purpose: Restore a verifier without needing or exposing the original password.
     */
    private Credential(byte[] salt, byte[] passwordHash) {
        this.salt = salt;
        this.passwordHash = passwordHash;
    }

    /* Purpose: Write only the calculation version, salt and hash to the account file.
     */
    public void writeTo(DataOutputStream output) throws IOException {
        output.writeInt(ITERATIONS);
        output.write(salt);
        output.write(passwordHash);
    }

    /* Purpose: Read a fixed-size verifier and reject incompatible calculation settings.
     */
    public static Credential readFrom(DataInputStream input) throws IOException {
        if (input.readInt() != ITERATIONS) throw new IOException("Unsupported password settings.");
        byte[] salt = new byte[16];
        byte[] hash = new byte[32];
        input.readFully(salt);
        input.readFully(hash);
        return new Credential(salt, hash);
    }
}
