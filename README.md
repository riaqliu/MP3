# MP3
Basically need to implement an **old memory management system** which has two main interacting objects: Memory partition and Job.

Memory Partions:
- can be occupied by a Job
- has a maximum memory size
- jobs larger than maximum memory size cannot be fitted

Job:
- has a job size and usage timer
- if a memory partition has larger memory size than this job's size, this job can be allocated to that partition
- when allocated, usage timer ticks down
- when timer hits zero, job is considered done and memory is freed.

To allocate memory effectively, a memory manager must be implemented.
Memory Manager:
- keeps a queue of jobs
- keeps a list of memory partitions
- can be best fit, worst fit, or first fit
- can fit multiple jobs at the same time or just one at a time
- attempts to fit jobs at every time step

read `Machine Problem 3 (1).pdf` for more details.
