package com.example.customerticketingportal.Controller;

import com.example.customerticketingportal.Dto.KbArticleCreateRequest;
import com.example.customerticketingportal.Dto.KbArticleSearchResponse;
import com.example.customerticketingportal.Model.KbArticle;
import com.example.customerticketingportal.Service.KbService;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/kb/articles")
public class KbController {
    private final KbService kbService;
    public KbController(KbService kb){
        this.kbService = kb;
    }
    @PostMapping
    public KbArticle create(@RequestBody @Valid KbArticleCreateRequest  req){
        return kbService.create(req);
    }
    @GetMapping("/search")
    public List<KbArticleSearchResponse> search(@RequestParam String category){
        return kbService.search(category);
    }
    @GetMapping("/searchAll")
    public List<KbArticle> searchAllResponses(){
        return kbService.searchAll();
    }
    @GetMapping("/{id}")
    public KbArticle getById(@PathVariable Long id) {
        return kbService.getById(id);
    }
}
