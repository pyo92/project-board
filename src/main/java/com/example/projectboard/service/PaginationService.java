package com.example.projectboard.service;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.IntStream;

@Service
public class PaginationService {

    private static final int BAR_LENGTH = 5; //최대 표시할 페이지 수

    public List<Integer> getPaginationBarNumbers(int curPageNumber, int totalPages) {
        int start = Math.max(curPageNumber - BAR_LENGTH / 2, 0);
        int end = Math.min(start + BAR_LENGTH, totalPages);

        start = start > end - BAR_LENGTH ? Math.max(end - BAR_LENGTH, 0) : start;

        return IntStream.range(start, end).boxed().toList();
    }

    public int getBarLength() {
        return BAR_LENGTH;
    }
}
