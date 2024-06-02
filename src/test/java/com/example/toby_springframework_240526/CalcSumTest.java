package com.example.toby_springframework_240526;

import lombok.extern.slf4j.Slf4j;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

@Slf4j
public class CalcSumTest {

    private Calculator calculator;
    private String path;

    @Before
    public void setUp() throws Exception {
        calculator = new Calculator(new BufferedReaderContext());
        path = getClass().getResource("/numbers.txt").getPath(); // 슬러쉬 꼭 붙여야 함.
        log.info(">>>>>>>> path: {}", path);
    }

    @Test
    public void calcSumClassPath() {
        Integer sum = calculator.calcSum(path);
        assertThat(sum, is(10));
    }

    @Test
    public void calcMultiply() {
        Integer multiply = calculator.calcMultiply(path);
        assertThat(multiply, is(24));
    }
}
