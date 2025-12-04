package com.tagme.tagme_store_back.domain.utils;

import com.password4j.Argon2Function;
import com.password4j.Hash;
import com.password4j.HashingFunction;
import com.password4j.Password;

public class PasswordUtils {

    public static String hashPassword(String password) {
        Hash hash = Password.hash(password)
                .addRandomSalt()
                .withArgon2();

        return hash.getResult();
    }

    public static boolean verifyPassword(String plainPassword, String dbPassword) {
        return Password.check(plainPassword, dbPassword).withArgon2();
    }

}
