package cj.software.genetics.schedule.util;

import org.springframework.stereotype.Service;

import java.util.Random;

@Service
public class RandomService {
    private final Random random = new Random();

    public int nextRandom(int max) {
        int result = random.nextInt(max);
        return result;
    }
}
