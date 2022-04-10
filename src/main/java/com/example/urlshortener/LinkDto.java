package com.example.urlshortener;

import org.hibernate.validator.constraints.URL;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

public class LinkDto {

    @NotNull
    @NotBlank(message = "Pole nie może być puste.")
    @URL(message = "Link niepoprawny.")
    private String longLink;
    private String shortLink;
    private String password;
    private long enters;

    public LinkDto(String longLink, String shortLink, String password, long enters) {
        this.longLink = longLink;
        this.shortLink = shortLink;
        this.password = password;
        this.enters = enters;
    }

    public LinkDto() {
    }

    public String getLongLink() {
        return longLink;
    }

    public void setLongLink(String longLink) {
        this.longLink = longLink;
    }

    public String getShortLink() {
        return shortLink;
    }

    public void setShortLink(String shortLink) {
        this.shortLink = shortLink;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public long getEnters() {
        return enters;
    }

    public void setEnters(long enters) {
        this.enters = enters;
    }
}
