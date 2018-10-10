package org.vp.test;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;

public class ModTest {
    public static void main(String[] args) throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {
        String st = "value";
        String mod = st.replace("\"", "");
        System.out.println(mod);

    }
}

