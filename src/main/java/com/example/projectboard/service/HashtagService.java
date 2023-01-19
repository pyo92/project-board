package com.example.projectboard.service;

import com.example.projectboard.domain.Hashtag;
import com.example.projectboard.repository.HashtagRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class HashtagService {

    private final HashtagRepository hashtagRepository;

    public Object parseHashtagNames(String content) {
        return null;
    }

    public Object findHashtagsByNames(Set<String> expectedHashtagNames) {
        return null;
    }

    @Transactional(readOnly = true)
    public List<String> getAllHashTagNames() {
        return hashtagRepository.findAll().stream().map(Hashtag::getName).toList();
    }

    public void deleteHashtagWithoutArticles(Object any) {
    }
}
