package com.example.projectboard.service;

import com.example.projectboard.domain.Member;
import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.repository.MemberRepository;
import org.junit.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@DisplayName("비즈니스 로직 - 회원")
@ExtendWith(MockitoExtension.class)
class MemberServiceTest {

    @InjectMocks
    private MemberService sut;

    @Mock
    private MemberRepository userAccountRepository;

    @DisplayName("존재하는 회원 ID를 검색하면, 회원 데이터를 Optional로 반환한다.")
    @Test
    void givenExistentUserId_whenSearching_thenReturnsOptionalUserData() {
        // Given
        String userId = "pyo";
        given(userAccountRepository.findById(userId)).willReturn(Optional.of(createMember(userId)));

        // When
        Optional<MemberDto> result = sut.searchMember(userId);

        // Then
        assertThat(result).isPresent();
        then(userAccountRepository).should().findById(userId);
    }

    @DisplayName("존재하지 않는 회원 ID를 검색하면, 비어있는 Optional을 반환한다.")
    @Test
    void givenNonexistentUserId_whenSearching_thenReturnsOptionalUserData() {
        // Given
        String userId = "wrong-user";
        given(userAccountRepository.findById(userId)).willReturn(Optional.empty());

        // When
        Optional<MemberDto> result = sut.searchMember(userId);

        // Then
        assertThat(result).isEmpty();
        then(userAccountRepository).should().findById(userId);
    }

    @DisplayName("회원 정보를 입력하면, 새로운 회원 정보를 저장하여 가입시키고 해당 회원 데이터를 리턴한다.")
    @Test
    void givenUserParams_whenSaving_thenSavesMember() {
        // Given
        Member userAccount = createMember("pyo");
        Member savedMember = createSigningUpMember("pyo");
        given(userAccountRepository.save(userAccount)).willReturn(savedMember);

        // When
        MemberDto result = sut.saveMember(
                userAccount.getUserId(),
                userAccount.getUserPw(),
                userAccount.getEmail(),
                userAccount.getNickName(),
                userAccount.getMemo()
        );

        // Then
        assertThat(result)
                .hasFieldOrPropertyWithValue("userId", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("userPassword", userAccount.getUserPw())
                .hasFieldOrPropertyWithValue("email", userAccount.getEmail())
                .hasFieldOrPropertyWithValue("nickname", userAccount.getNickName())
                .hasFieldOrPropertyWithValue("memo", userAccount.getMemo())
                .hasFieldOrPropertyWithValue("createdBy", userAccount.getUserId())
                .hasFieldOrPropertyWithValue("modifiedBy", userAccount.getUserId());
        then(userAccountRepository).should().save(userAccount);
    }


    private Member createMember(String username) {
        return createMember(username, null);
    }

    private Member createSigningUpMember(String username) {
        return createMember(username, username);
    }

    private Member createMember(String username, String createdBy) {
        return Member.of(
                username,
                "password",
                "e@mail.com",
                "nickname",
                "memo",
                createdBy
        );
    }
}