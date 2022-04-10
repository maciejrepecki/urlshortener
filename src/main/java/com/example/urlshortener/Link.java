package com.example.urlshortener;

import org.hibernate.validator.constraints.URL;

import javax.persistence.*;

@Entity
public class Link {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @URL
    private String longLink;
    @Column(unique = true)
    private String shortLink;
    @Column(unique = true)
    private String password;
    private long enters;

    public Link() {
    }

    public Link(String longLink, String shortLink, String password) {
        this.longLink = longLink;
        this.shortLink = shortLink;
        this.password = password;
        this.enters = 0;
    }

    public String getLongLink() {
        return longLink;
    }

    public String getShortLink() {
        return shortLink;
    }

    public String getPassword() {
        return password;
    }

    public long getEnters() {
        return enters;
    }

    public void plusEnters() {
        enters++;
    }
}
