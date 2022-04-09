package com.example.urlshortener;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import javax.validation.Valid;
import java.util.Optional;

@Controller
public class LinkController {
    private final LinkService linkService;

    public LinkController(LinkService linkService) {
        this.linkService = linkService;
    }

    @GetMapping("/")
    String home(Model model) {
        model.addAttribute("attribute", new LinkDto());
        return "index";
    }

    @GetMapping("/{shortLink}")
    String getLongLink(@PathVariable String shortLink, Model model) {
        String httpComponent = "http://localhost:8080/";
        Optional<LinkDto> linkDto = linkService.shortToLong(httpComponent + shortLink);
        if (linkDto.isPresent()) {
            linkService.addEnter(linkDto.get());
            model.addAttribute(linkDto.get().getLongLink());
        }
        return linkDto.isPresent() ? "redirect" : "notfound";
    }

    @PostMapping("/save")
    String saveLink(@Valid @ModelAttribute("attribute") LinkDto linkDto, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            return "index";
        } else {
            linkDto = linkService.add(linkDto);
            model.addAttribute(linkDto);
            return "result";
        }

    }

    @GetMapping("/delete")
    String deletePage() {
        return "delete";
    }

    @PostMapping("/delete")
    String deleteForm(String password, Model model) {
        boolean isDeleted = linkService.delete(password);
        model.addAttribute("isDeleted", isDeleted);
        return "delete";
    }

    @GetMapping("/details")
    String detailsPage(Model model) {
        model.addAttribute("attribute", new LinkDto());
        return "details";
    }

    @PostMapping("/details")
    String detailsForm(@Valid @ModelAttribute("attribute") LinkDto linkDto, BindingResult bindingResult, Model model) {

        Optional<LinkDto> resultLinkDto = linkService.details(linkDto.getShortLink());

        if (resultLinkDto.isPresent()) {
            boolean isPresent = true;
            model.addAttribute("isPresent", isPresent);
            model.addAttribute(resultLinkDto.get());
        } else {
            boolean isPresent = false;
            model.addAttribute("isPresent", isPresent);
        }
        return "details";
    }
}
