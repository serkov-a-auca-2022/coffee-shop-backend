package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/news")
public class NewsController {

    @Autowired
    private NewsRepository newsRepository;

    // 1) Получить список всех новостей
    @GetMapping
    public List<News> getAllNews() {
        return newsRepository.findAll();
    }

    // 2) Создать новость (POST)
    @PostMapping
    public News createNews(@RequestBody News news) {
        // Если вы хотите автоматически ставить дату, можно делать это в конструкторе
        // или вот так (если дата не передаётся с клиента):
        if (news.getDateTime() == null) {
            news.setDateTime(java.time.LocalDateTime.now());
        }
        return newsRepository.save(news);
    }

    // 3) Получить новость по ID (при желании)
    @GetMapping("/{id}")
    public News getNewsById(@PathVariable Long id) {
        return newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));
    }

    // 4) Обновить существующую новость (PUT)
    @PutMapping("/{id}")
    public News updateNews(@PathVariable Long id, @RequestBody News updatedNews) {
        News existing = newsRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("News not found with id: " + id));

        existing.setTitle(updatedNews.getTitle());
        existing.setShortDescription(updatedNews.getShortDescription());
        existing.setContent(updatedNews.getContent());
        existing.setImageUrl(updatedNews.getImageUrl());
        // дату при желании тоже обновляем
        existing.setDateTime(updatedNews.getDateTime());

        return newsRepository.save(existing);
    }

    // 5) Удалить новость
    @DeleteMapping("/{id}")
    public void deleteNews(@PathVariable Long id) {
        newsRepository.deleteById(id);
    }
}
