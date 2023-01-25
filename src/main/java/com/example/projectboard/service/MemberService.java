package com.example.projectboard.service;

import com.example.projectboard.domain.Member;
import com.example.projectboard.dto.MemberDto;
import com.example.projectboard.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Transactional
@Service
public class MemberService {

    private final MemberRepository memberRepository;

    @Transactional(readOnly = true)
    public Optional<MemberDto> searchMember(String userId) {
        return memberRepository.findById(userId)
                .map(MemberDto::from);
    }

    public MemberDto saveMember(String userId, String password, String email, String nickname, String memo) {
        return MemberDto.from(memberRepository.save(Member.of(userId, password, email, nickname, memo, userId)));
    }
}
