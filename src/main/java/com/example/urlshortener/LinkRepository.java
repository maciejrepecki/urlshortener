package com.example.urlshortener;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface LinkRepository extends CrudRepository<Link, Long> {
    Optional<Link> findByShortLink(String shortLink);

    Optional<Link> findByPassword(String password);
}
