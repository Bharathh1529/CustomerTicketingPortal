package com.example.customerticketingportal.Repository;

import com.example.customerticketingportal.Model.KbArticle;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface KbArticleRepository extends JpaRepository<KbArticle, Long> {
    List<KbArticle> findByTitleContainingIgnoreCase(String title);
    List<KbArticle> findByCategoryIgnoreCase(String category);
    List<KbArticle> findByBodyContainingIgnoreCase(String text);
}
