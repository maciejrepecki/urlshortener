package com.example.urlshortener;

import net.bytebuddy.utility.RandomString;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Optional;


@Service
public class LinkService {

    private final LinkRepository linkRepository;

    @Autowired
    public LinkService(LinkRepository linkRepository) {
        this.linkRepository = linkRepository;
    }

    public Optional<LinkDto> shortToLong(String shortLink) {
        return findShortLink(shortLink);
    }

    private Optional<LinkDto> findShortLink(String shortLink) {
        return linkRepository.findByShortLink(shortLink).map(this::linkToDto);
    }

    public LinkDto add(LinkDto linkDto) {
        var longLink = linkDto.getLongLink();
        var link = new Link(longLink, generateShortLink(), generatePassword());
        link = linkRepository.save(link);
        return linkToDto(link);
    }

    private LinkDto linkToDto(Link link) {
        return new LinkDto(link.getLongLink(), link.getShortLink(), link.getPassword(), link.getEnters());
    }

    private String generatePassword() {
        var password = RandomString.make(5);
        if (linkRepository.findByShortLink(password).isPresent()) {
            password = generatePassword();
        }
        return password;
    }

    private String generateShortLink() {
        final var HTTP_COMPONENT = "http://localhost:8080/";
        var shortLink = HTTP_COMPONENT + RandomString.make(5);
        if (linkRepository.findByShortLink(shortLink).isPresent())
            shortLink = generateShortLink();
        return shortLink;
    }

    @Transactional
    public boolean delete(String password) {
        var link = linkRepository.findByPassword(password);
        return link.map(presentLink -> {
            linkRepository.delete(presentLink);
            return true;
        }).orElse(false);
    }

    public Optional<LinkDto> details(String shortLink) {
        return findShortLink(shortLink);
    }

    @Transactional
    public void addEnter(LinkDto linkDto) {
        var link = linkRepository.findByShortLink(linkDto.getShortLink());
        link.ifPresent(Link::plusEnters);
    }
}
