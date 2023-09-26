# cj.software.genetics.schedule

solve scheduling problem with a genetic algorithm

The idea behind the priority-wise breeding procedure is:

1. First breed for all tasks with priority 0.
2. Then compress them: after the breeding, the tasks are scattered through the array.
   After the compress, they occupy the first subsequent elements of the array without any
   gaps between them. For the following priorities, these first positions are not available
3. Then repeat that with the next priorities.

**Priority 0 after breeding in one worker**

| slot | task-id | prio |
|-----:|--------:|-----:|
|   15 |      47 |    0 |
|   22 |      17 |    0 |
|   49 |      49 |    0 |
|   67 |      24 |    0 |

**Priority 0 after compress**

| slot | task-id | prio |
|-----:|--------:|-----:|
|    0 |      47 |    0 |
|    1 |      17 |    0 |
|    2 |      49 |    0 |
|    3 |      24 |    0 |

From now on, slots 0 to 3 are not available anymore

**Prioriy 1 after breeding**

| slot | task-id | prio |
|-----:|--------:|-----:|
|    0 |      47 |    0 |
|    1 |      17 |    0 |
|    2 |      49 |    0 |
|    3 |      24 |    0 |
|   13 |      25 |    1 |
|   66 |      88 |    1 |
|   99 |      75 |    1 |

**Priority 1 after compress**

| slot | task-id | prio |
|-----:|--------:|-----:|
|    0 |      47 |    0 |
|    1 |      17 |    0 |
|    2 |      49 |    0 |
|    3 |      24 |    0 |
|    4 |      25 |    1 |
|    5 |      88 |    1 |
|    6 |      75 |    1 |

From now on, slots 0 to 6 are not available anymore.

Priority 2 is treated like priority 1.
