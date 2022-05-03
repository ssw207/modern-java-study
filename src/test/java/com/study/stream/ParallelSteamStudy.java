package com.study.stream;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

@Slf4j
public class ParallelSteamStudy {

    @Test
    public void 병렬로_처리한다() throws Exception {
        //given
        Stream<Long> iterate = Stream.iterate(1L, i -> i + 1);

        //when
        iterate.limit(5)
                .parallel()
                .forEach(i -> log.info("i:{}", i));
    }
    
    @Test
    public void 병렬을_순차로_바꾼다() throws Exception {
        //given
        Stream<Long> iterate = Stream.iterate(1L, i -> i + 1);

        //when
        iterate.limit(5)
                .parallel()
                .filter(i -> i > 1 && i < 4)
                .sequential()
                .forEach(i -> log.info("i:{}",i));
    }
}
