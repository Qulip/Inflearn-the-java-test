package me.whiteship.inflearnthejavatest.member;

import java.util.Optional;

import me.whiteship.inflearnthejavatest.domain.Member;

public interface MemberService {
    void validate(Long memberId) throws InvalidMemberException;

    Optional<Member> findById(Long memberId) throws MemberNotFoundException;
}
