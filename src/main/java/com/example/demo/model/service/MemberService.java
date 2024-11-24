package com.example.demo.model.service;

import jakarta.validation.Valid; //입력값 검증 Valid
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import com.example.demo.model.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import com.example.demo.model.domain.Member;
import org.springframework.security.crypto.password.PasswordEncoder;

//import com.example.demo.config.SecurityConfig;
//import java.util.List;
//import lombok.*; // 어노테이션 자동 생성
//import java.util.Optional;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.data.domain.Pageable;
//import org.springframework.data.domain.Page;
//import org.apache.el.stream.Optional;
//import org.springframework.data.domain.PageImpl;
//import java.util.Map;
//import com.example.demo.model.domain.Article;
//import com.example.demo.model.service.AddArticleRequest;
//import com.ibm.dtfj.corereaders.PageCache.Page;
//import org.springframework.web.bind.annotation.ModelAttribute;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.PutMapping;
//import com.example.demo.model.domain.Board;
//import com.example.demo.model.repository.BlogRepository;
//import com.example.demo.model.repository.BoardRepository;
//import org.springframework.data.web.PageableDefault;

@Service
@Validated
@Transactional // 트랜잭션 처리(클래스 내 모든 메소드 대상)
@RequiredArgsConstructor
public class MemberService {
    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder; // 스프링 버전 5 이후, 단방향 해싱 알고리즘 지원

    private void validateDuplicateMember(AddMemberRequest request){
        Member findMember = memberRepository.findByEmail(request.getEmail()); // 이메일 존재 유무
    if(findMember != null){
        throw new IllegalStateException("이미 가입된 회원입니다."); // 예외처리
    }
}
public Member saveMember(@Valid AddMemberRequest request){
    validateDuplicateMember(request); // 이메일 체크

    String encodedPassword = passwordEncoder.encode(request.getPassword());
    request.setPassword(encodedPassword); // 암호화된 비밀번호 설정
    return memberRepository.save(request.toEntity());
    }

public Member loginCheck(String email, String rawPassword) {
    Member member = memberRepository.findByEmail(email); // 이메일 조회
    if (member == null) {
        throw new IllegalArgumentException("등록되지 않은 이메일입니다.");
    }

    if (!passwordEncoder.matches(rawPassword, member.getPassword())) { // 비밀번호 확인
        throw new IllegalArgumentException("비밀번호가 일치하지 않습니다.");
    }
    return member; // 인증 성공 시 회원 객체 반환
    }
}