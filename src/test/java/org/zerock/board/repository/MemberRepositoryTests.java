package org.zerock.board.repository;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Commit;
import org.zerock.board.entity.Board;
import org.zerock.board.entity.Member;

import java.util.stream.IntStream;

@SpringBootTest
public class MemberRepositoryTests {
    @Autowired
    private MemberRepository memberRepository;
    @Test
    @Commit
     public void insertMembers(){
        IntStream.rangeClosed(1,100).forEach(i->{
            Member member = Member.builder()
                    .email("user"+i+"@aaa.com")
                    .password("1111")
                    .name("USER"+i)
                    .build();
        memberRepository.save(member);
           });
    }

    @Test
    public void insertMember(){
        Member member = Member.builder().email("user3000@aaa.com")
                .password("00202")
                .name("USER3000")
                .build();
        memberRepository.save(member);
    }
}
