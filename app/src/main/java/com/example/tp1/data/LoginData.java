package com.example.tp1.data;

import java.io.Serializable;

public class LoginData implements Serializable {
    private String email;
    private String mot_de_passe;

    public LoginData(String email, String mot_de_passe) {
        this.email = email;
        this.mot_de_passe = mot_de_passe;
    }
}
