package cj.software.genetics.schedule.util;

import cj.software.genetics.schedule.entity.Worker;
import cj.software.genetics.schedule.entity.WorkerChain;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class WorkerChainService {

    @Autowired
    private WorkerService workerService;

    public int calcDuration(WorkerChain workerChain) {
        int result = 0;
        for (int iPrio = 0; iPrio < 3; iPrio++) {
            Worker worker = workerChain.getWorkerForPriority(iPrio);
            int workerDuration = workerService.calcDuration(worker);
            result += workerDuration;
        }
        return result;
    }
}
