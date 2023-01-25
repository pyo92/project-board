package com.example.projectboard.dto.security;

import com.example.projectboard.dto.MemberDto;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public record BoardPrincipal(
        String username,
        String password,
        Collection<? extends GrantedAuthority> authorities,
        String email,
        String nickname,
        String memo,
        Map<String, Object> oAuth2Attribute
) implements UserDetails, OAuth2User {

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo) {
        return BoardPrincipal.of(
                username,
                password,
                email,
                nickname,
                memo,
                Map.of()
        );
    }

    public static BoardPrincipal of(String username, String password, String email, String nickname, String memo, Map<String, Object> oAuth2Attribute) {
        Set<RoleType> roleTypes = Set.of(RoleType.USER);

        return new BoardPrincipal(
                username,
                password,
                roleTypes.stream()
                        .map(RoleType::getName)
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toUnmodifiableSet()),
                email,
                nickname,
                memo,
                oAuth2Attribute
        );
    }

    public static BoardPrincipal from(MemberDto dto) {
        return BoardPrincipal.of(
                dto.userId(),
                dto.userPw(),
                dto.email(),
                dto.nickName(),
                dto.memo()
        );
    }

    public MemberDto toDto() {
        return MemberDto.of(
                username,
                password,
                email,
                nickname,
                memo,
                null,
                null,
                null,
                null
        );
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2Attribute;
    }

    @Override
    public String getName() {
        return username;
    }

    public enum RoleType {
        USER("ROLE_USER"); //ROLE_ 이라고 명명해야 하는 규칙이 있다.

        @Getter
        private final String name;

        RoleType(String name) {
            this.name = name;
        }
    }
}
