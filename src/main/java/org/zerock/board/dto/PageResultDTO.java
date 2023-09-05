package org.zerock.board.dto;

import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Data
public class PageResultDTO<DTO, EN> { //EN == ENTITY
    private List<DTO> dtoList;
    private int totalPage; //총 페이지 번호
    private int page; //현재 페이지 번호
    private int size; //목록 사이즈
    private int start, end; //시작 페이지 번호, 끝 페이지 번호
    private boolean prev,next; //이전, 다음
    private List<Integer> pageList; //페이지 번호 목록
    public PageResultDTO(Page<EN> result, Function<EN,DTO> fn){ //Function 함수형인터페이스는 apply메서드를 의미하며,
        //PageResultDTO가 사용될 때 람다를 이용하여 apply를 구현할 것이다.
        dtoList = result.stream().map(fn).collect(Collectors.toList());
        totalPage = result.getTotalPages();
        makePageList(result.getPageable());
    }
    private void makePageList(Pageable pageable){
        this.page= pageable.getPageNumber() +1;
        this.size= pageable.getPageSize();

        //temp end page
        int tempEnd = (int)(Math.ceil(page/10.0)) *10; //1페이지 당 10개씩 보여준다는 가정 하에
        start = tempEnd - 9;
        prev = start > 1; //만약 시작페이지가 1보다 크면 prev를 보여준다.
        end = totalPage > tempEnd ? tempEnd : totalPage;  //만약 토탈페이지가 34이면, tempEnd는 40일테니까, tempEnd대신 토탈페이지 값을 end로 사용한다.
        next = totalPage > tempEnd;

        pageList= IntStream.rangeClosed(start,end).boxed().collect(Collectors.toList());
    }
}
