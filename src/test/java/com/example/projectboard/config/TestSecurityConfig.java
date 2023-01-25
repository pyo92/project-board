package com.example.projectboard.config;

import com.example.projectboard.domain.Member;
import com.example.projectboard.repository.MemberRepository;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.event.annotation.BeforeTestMethod;

import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@Import(SecurityConfig.class)
public class TestSecurityConfig {

    @MockBean private MemberRepository memberRepository;

    @BeforeTestMethod
    public void securitySetUp() {
        given(memberRepository.findById(anyString())).willReturn(Optional.of(Member.of(
                "test",
                "test",
                "test@email.com",
                "test",
                "test"
        )));
    }
}
