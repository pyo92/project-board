package com.example.projectboard.service;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비즈니스 로직 - 페이지네이션")
@SpringBootTest(classes = PaginationService.class)
class PaginationServiceTest {

    private final PaginationService sut;

    @Autowired
    public PaginationServiceTest(PaginationService sut) {
        this.sut = sut;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 전달하면, 페이징 바 리스트를 만들어서 반환한다.")
    @MethodSource
    @ParameterizedTest(name = "[{index}] 현재 페이지: {0}, 총 페이지: {1} => {2}")
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumber(int curPageNumber, int totalPages, List<Integer> expected) {
        //given

        //when
        List<Integer> actual = sut.getPaginationBarNumbers(curPageNumber, totalPages);

        //then
        assertThat(actual).isEqualTo(expected);
    }

    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnsPaginationBarNumber() {
        return Stream.of(
                arguments(0, 7, List.of(0, 1, 2, 3, 4)),
                arguments(1, 7, List.of(0, 1, 2, 3, 4)),
                arguments(2, 7, List.of(0, 1, 2, 3, 4)),
                arguments(3, 7, List.of(1, 2, 3, 4, 5)),
                arguments(4, 7, List.of(2, 3, 4, 5, 6)),
                arguments(5, 7, List.of(2, 3, 4, 5, 6)),
                arguments(6, 7, List.of(2, 3, 4, 5, 6))
        );
    }

    @DisplayName("현재 설정되어 있는 페이지네이션 바의 최대 표시 개수를 알려준다.")
    @Test
    void givenNothing_whenCalling_thenReturnsPaginationBarLength() {
        //given

        //when
        int barLength = sut.getBarLength();

        //then
        assertThat(barLength).isEqualTo(5);
    }
}