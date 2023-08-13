package cj.software.genetics.schedule.util;

import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Service
public class RandomService {
    private final Random random = new Random();

    public int nextRandom(int max) {
        int result = random.nextInt(max);
        return result;
    }

    @SuppressWarnings("squid:S5413")    // the remove invocation of the list is ok
    public int[] shuffledUpTo(int max) {
        int[] result = new int[max];
        List<Integer> values = new ArrayList<>();
        for (int i = 0; i < max; i++) {
            values.add(i);
        }

        for (int i = 0; i < max; i++) {
            int index = nextRandom(values.size());
            int value = values.remove(index);
            result[i] = value;
        }
        return result;
    }


}
