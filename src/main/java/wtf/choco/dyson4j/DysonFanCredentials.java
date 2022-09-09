package wtf.choco.dyson4j;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

import org.jetbrains.annotations.NotNull;

import wtf.choco.dyson4j.util.Preconditions;

/**
 * Simple Dyson fan credentials.
 * <p>
 * This class allows the simple input of the fan's username and password as found on the
 * fan's label or in its user manual, and automatically hashes it using the fan's expected
 * hashing algorithm.
 */
public final class DysonFanCredentials {

    private final String username;
    private final String password, passwordHashed;

    /**
     * Construct a new {@link DysonFanCredentials}.
     * <p>
     * The username as found on the fan's label may or may not be prefixed with "DYSON-".
     * The {@code username} argument may include this prefix, but it will automatically
     * stripped in the value returned by {@link #getUsername()} as it is not relevant to
     * the connection credentials of the Dyson fan.
     *
     * @param username the fan's username. Typically starts with {@code "DYSON-"}
     * @param password the fan's password
     */
    public DysonFanCredentials(@NotNull String username, @NotNull String password) {
        Preconditions.checkArgument(username != null, "username must not be null");
        Preconditions.checkArgument(password != null, "password must not be null");

        if (username.startsWith("DYSON-")) {
            username = username.substring("DYSON-".length());
        }

        this.username = username;
        this.password = password;
        this.passwordHashed = hashPassword(password);
    }

    /**
     * Get the fan's username.
     *
     * @return the username
     */
    @NotNull
    public String getUsername() {
        return username;
    }

    /**
     * Get the fan's password.
     *
     * @return the password
     */
    @NotNull
    public String getPassword() {
        return password;
    }

    /**
     * Get the fan's hashed password.
     *
     * @return the hashed password
     */
    @NotNull
    public String getPasswordHashed() {
        return passwordHashed;
    }

    /*
     * According to online articles, it seems that Dyson fans require the plain text password to
     * be digested using a SHA-512 hash, then encoded in Base64 format.
     *
     * https://community.home-assistant.io/t/dyson-pure-cool-link-local-mqtt-control/217263
     */
    private String hashPassword(String password) {
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("SHA-512");
        } catch (NoSuchAlgorithmException e) {
            throw new UnsupportedOperationException("Cannot proceed. Missing SHA-512 algorithm.", e);
        }

        byte[] bytes = digest.digest(password.getBytes(StandardCharsets.UTF_8));
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

}
