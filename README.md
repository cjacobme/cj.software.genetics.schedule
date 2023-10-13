# cj.software.genetics.schedule

solve scheduling problem with a genetic algorithm

The idea behind the priority-wise breeding procedure is, that all what has
been implemented up to here has to be done priority-wise:

- The parameter `number of workers / solution` currently is meant for all jobs.
  But with this branch, it additionally means _per priority_. So in fact if the
  value of `number of workers / solution` has the value 4, the solution will have
  physically 12 workers, at least during the breeding process.
- When the breeding process has finished the priority-wise breeding, it links
  the 3 workers together to one, of course starting with those of priority 0.

A new instance `WorkerChain` is introduced. It basically contains a `Worker[]` array.
The key of the sorted map is the priority. The UID displays the worker chain and not the workers themselves.