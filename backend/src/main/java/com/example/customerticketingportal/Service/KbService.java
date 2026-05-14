package com.example.customerticketingportal.Service;

import com.example.customerticketingportal.Dto.KbArticleCreateRequest;
import com.example.customerticketingportal.Dto.KbArticleSearchResponse;
import com.example.customerticketingportal.Model.KbArticle;
import com.example.customerticketingportal.Repository.KbArticleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
@Service
public class KbService {
    private final KbArticleRepository kbRepo;
    public KbService(KbArticleRepository kbRepo) {
        this.kbRepo = kbRepo;
    }
    public KbArticle create(KbArticleCreateRequest request){
        var a = new KbArticle();
        a.setTitle(request.getTitle());
        a.setBody(request.getBody());
        a.setCategory(request.getCategory());
        a.setCreatedAt(LocalDateTime.now());
        a.setUpdatedAt(LocalDateTime.now());
        return kbRepo.save(a);
    }
    public List<KbArticleSearchResponse> search(String category){
        var matches = kbRepo.findByCategoryIgnoreCase(category);
        return matches.stream().map(
                a -> {
                    var r = new KbArticleSearchResponse();
                    r.setKbId(a.getId());
                    r.setCategory(a.getCategory());
                    r.setTitle(a.getTitle());
                    var body = a.getBody();
                    r.setSnippet(body.substring(0, Math.min(120, body.length())) + "...");
                    return r;
                }
        ).toList();
    }
    public List<KbArticle> searchAll(){
        return kbRepo.findAll().stream().toList();
    }
    public KbArticle getById(Long id) {
        return kbRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("KB Article not found"));
    }
}
