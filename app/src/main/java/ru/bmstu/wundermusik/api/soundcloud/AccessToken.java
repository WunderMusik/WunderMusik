package ru.bmstu.wundermusik.api.soundcloud;

/**
 * Created by max on 22.03.16.
 */
public class AccessToken {
    private String token;
    private String scope;

    public AccessToken(String token, String scope) {
        this.token = token;
        this.scope = scope;
    }

    public String getToken() {
        return this.token;
    }

    public String getScope() {
        return this.scope;
    }
}
