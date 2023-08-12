package cj.software.genetics.schedule.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = RandomService.class)
class RandomServiceTest {

    @Autowired
    private RandomService randomService;

    @Test
    void to10() {
        int randomValue = randomService.nextRandom(10);
        assertThat(randomValue).as("%d", randomValue).isNotNegative().isLessThan(10);
    }

    @Test
    void to100() {
        int randomValue = randomService.nextRandom(100);
        assertThat(randomValue).as("%d", randomValue).isNotNegative().isLessThan(100);
    }
}
