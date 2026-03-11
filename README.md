# Queues Management Simulation
A Java-based discrete-event simulation application designed to optimize queue management by distributing clients across multiple servers using various selection policies. This project demonstrates advanced multithreading, concurrency control, and real-time visualization using Java Swing.

# Overview
The application simulates a scenario where clients arrive at random intervals and are assigned to one of several queues (servers). The system calculates key performance indicators such as average waiting time, average service time, and peak hour to evaluate the efficiency of different distribution strategies.

# Key Features
- Multithreading Architecture: Each server runs in a dedicated thread, ensuring concurrent processing
- Dynamic Selection Policies:
  - Shortest Queue: Assigns clients to the queue with the fewest waiting customers
  - Shortest Time: Assigns clients to the queue with the minimum estimated waiting time
- Real-time Visualization: Interactive GUI showing queue status, waiting clients, and server loads
- Performance Analytics: Calculation and logging of:
  - Average waiting time per client
  - Average service time
  - Peak hour identification 

# Technologies Used
- Language: Java 
- GUI Framework: Java Swing
- Concurrency: Thread, AtomicInteger, BlockingQueue
- Logging: java.util.logging.Logger
- Design Patterns: Strategy Pattern (for selection policies)

# Architecture
The project follows a layered architecture:
- Model: Task, Server 
- Business Logic: Scheduler, SimulationManager 
- Presentation: SimulationFrame
