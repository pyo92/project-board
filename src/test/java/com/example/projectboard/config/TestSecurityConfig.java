package com.example.projectboard.config;

import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.service.MemberService;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private MemberService memberService;

    @BeforeTestMethod
    public void securitySetUp() {
        given(memberService.searchMember(anyString()))
                .willReturn(Optional.of(createMemberDto()));
        given(memberService.saveMember(anyString(), anyString(), anyString(), anyString(), anyString()))
                .willReturn(createMemberDto());
    }

    private MemberDto createMemberDto() {
        return MemberDto.of(
                "test",
                "test",
                "test@email.com",
                "test",
                "test",
                null,
                null,
                null,
                null
        );
    }
}
