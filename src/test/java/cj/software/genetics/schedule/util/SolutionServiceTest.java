package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Solution;
import cj.software.genetics.schedule.entity.SolutionBuilder;
import cj.software.genetics.schedule.entity.WorkerChain;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = SolutionService.class)
class SolutionServiceTest {
    @Autowired
    private SolutionService solutionService;

    @MockBean
    private RandomService randomService;

    @MockBean
    private WorkerChainService workerChainService;

    @Test
    void duration4711() {
        Solution solution = new SolutionBuilder().build();

        when(workerChainService.calcDuration(any(WorkerChain.class))).thenReturn(0, 4711);

        int duration = solutionService.calcDuration(solution);

        assertThat(duration).isEqualTo(4711);
    }

    @Test
    void duration42() {
        Solution solution = new SolutionBuilder().build();

        when(workerChainService.calcDuration(any(WorkerChain.class))).thenReturn(42, 0);

        int duration = solutionService.calcDuration(solution);

        assertThat(duration).isEqualTo(42);
    }
}
