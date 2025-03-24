package utils;

import org.mindrot.jbcrypt.BCrypt;

public class PasswordUtils {
    public static String getHashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt(12)); // 12 rounds (cost factor)
    }

    public static boolean verifyPassword(String password, String hashedPassword) {
        return BCrypt.checkpw(password, hashedPassword);
    }
}
