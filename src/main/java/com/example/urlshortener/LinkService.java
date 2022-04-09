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

        String longLink = linkDto.getLongLink();
        Link link = new Link(longLink, generateShortLink(), generatePassword());
        link = linkRepository.save(link);
        return linkToDto(link);
    }

    private LinkDto linkToDto(Link link) {
        String shortLink = link.getShortLink();
        String longLink = link.getLongLink();
        String password = link.getPassword();
        long enters = link.getEnters();
        return new LinkDto(longLink, shortLink, password, enters);
    }

    private Link dtoToLink(LinkDto linkDto) {
        String longLink = linkDto.getLongLink();
        String shortLink = linkDto.getShortLink();
        String password = linkDto.getPassword();
        return new Link(longLink, shortLink, password);
    }

    private String generatePassword() {
        String password = RandomString.make(5);
        if (linkRepository.findByShortLink(password).isPresent())
            password = generatePassword();
        return password;
    }

    private String generateShortLink() {
        String httpComponent = "http://localhost:8080/";
        String shortLink = httpComponent + RandomString.make(5);
        if (linkRepository.findByShortLink(shortLink).isPresent())
            shortLink = generateShortLink();
        return shortLink;
    }

    @Transactional
    public boolean delete(String password) {

        Optional<Link> link = linkRepository.findByPassword(password);

        if (link.isEmpty()) {
            return false;
        } else {
            linkRepository.delete(link.get());
            return true;
        }
    }

    public Optional<LinkDto> details(String shortLink) {
        return findShortLink(shortLink);
    }

    @Transactional
    public void addEnter(LinkDto linkDto) {
        String shortLink = linkDto.getShortLink();
        Optional<Link> link = linkRepository.findByShortLink(shortLink);
        link.ifPresent(Link::plusEnters);
    }
}
