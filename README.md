# cj.software.genetics.schedule

solve scheduling problem with a genetic algorithm

The idea behind the priority-wise breeding procedure is:

1. For each worker: create a copy that only holds the prio 0 tasks.
2. Mate them. The result is newly populated workers with only prio 0 tasks.
3. compress the workers. All prio 0 tasks are saved directly one after the other
   in the internal array. Additionally, a start index is determined which begins
   directly behind the last task.

**TODO: slots with gaps.**

Old values:

| slot | id | prio |
|-----:|---:|-----:|
|    0 |  0 |    0 |
|    1 | 15 |    1 |
|    2 |  1 |    2 |
|    3 | 47 |    0 |
|    4 | 16 |    2 |
|    5 | 17 |    1 |

Step 1: only prio 0

| slot | id | prio |
|-----:|---:|-----:|
|    0 |  0 |    0 |
|    1 |    |      |
|    2 |    |      |
|    3 | 47 |    0 |
|    4 |    |      |
|    5 |    |      |

Step 2: mate prio 0

| slot | id | prio |
|-----:|---:|-----:|
|    0 |    |      |
|    1 | 47 |    0 |
|    2 |    |      |
|    3 |    |      |
|    4 |  0 |    0 |
|    5 |    |      |

Step 3: compress prio 0

| slot | id | prio |
|-----:|---:|-----:|
|    0 | 47 |    0 |
|    1 |  0 |    0 |
|    2 |    |      |
|    3 |    |      |
|    4 |    |      |
|    5 |    |      |

Step 4: copy prio 1 with shift from prio 0 occupation

In the old data, prio 1 tasks are located at slots 1 and 5. Slot 1 is already
occupied by a prio 0 task. So the prio 1 task will be copied to the next available
position.

| slot | id | prio |
|-----:|---:|-----:|
|    0 | 47 |    0 |
|    1 |  0 |    0 |
|    2 | 15 |    1 |
|    3 |    |      |
|    4 |    |      |
|    5 | 17 |    1 |

Step 5: mate them. During that, the prio 0 tasks remain at their position. These
slots are not available

| slot | id | prio |
|-----:|---:|-----:|
|    0 | 47 |    0 |
|    1 |  0 |    0 |
|    2 | 15 |    1 |
|    3 |    |      |
|    4 |    |      |
|    5 | 17 |    1 |



