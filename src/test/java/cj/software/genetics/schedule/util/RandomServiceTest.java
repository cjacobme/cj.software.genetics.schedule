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

    @Test
    void shuffledIndices10() {
        int[] indices = randomService.shuffledUpTo(10);
        assertThat(indices).as("indices").containsExactlyInAnyOrder(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    }

    @Test
    void shuffledIndices4() {
        int[] indices = randomService.shuffledUpTo(4);
        assertThat(indices).as("indices").containsExactlyInAnyOrder(0, 1, 2, 3);
    }
}
