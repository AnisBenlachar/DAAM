package com.ipd.energy.config;

import com.ipd.energy.entity.*;
import com.ipd.energy.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private WorkerRepository workerRepository;

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Check if data already exists
        if (userRepository.count() > 0) {
            System.out.println("Database already initialized. Skipping data initialization.");
            return;
        }

        System.out.println("Initializing database with test data...");

        // Create Admin user
        Admin admin = new Admin();
        admin.setEmail("admin@solarhub.com");
        admin.setFirstName("Admin");
        admin.setLastName("User");
        admin.setPassword(passwordEncoder.encode("admin123"));
        admin.setBirthDate(LocalDate.of(1985, 1, 1));
        admin.setPhoneNumber("+1234567890");
        admin.setRole("ADMIN");
        admin.setPermissions("ALL");
        userRepository.save(admin);
        System.out.println("Created admin user: admin@solarhub.com / admin123");

        // Create Worker users
        Worker worker1 = new Worker();
        worker1.setEmail("worker@solarhub.com");
        worker1.setFirstName("John");
        worker1.setLastName("Worker");
        worker1.setPassword(passwordEncoder.encode("worker123"));
        worker1.setBirthDate(LocalDate.of(1990, 5, 15));
        worker1.setPhoneNumber("+1234567891");
        worker1.setRole("WORKER");
        worker1.setRating(4.5);
        workerRepository.save(worker1);
        System.out.println("Created worker user: worker@solarhub.com / worker123");

        Worker worker2 = new Worker();
        worker2.setEmail("worker2@solarhub.com");
        worker2.setFirstName("Jane");
        worker2.setLastName("Smith");
        worker2.setPassword(passwordEncoder.encode("worker123"));
        worker2.setBirthDate(LocalDate.of(1988, 8, 20));
        worker2.setPhoneNumber("+1234567892");
        worker2.setRole("WORKER");
        worker2.setRating(4.8);
        workerRepository.save(worker2);
        System.out.println("Created worker user: worker2@solarhub.com / worker123");

        // Create Client user
        Client client = new Client();
        client.setEmail("client@solarhub.com");
        client.setFirstName("Alice");
        client.setLastName("Client");
        client.setPassword(passwordEncoder.encode("client123"));
        client.setBirthDate(LocalDate.of(1992, 3, 10));
        client.setPhoneNumber("+1234567893");
        client.setRole("CLIENT");

        Location location = new Location();
        location.setAddress("123 Solar Street");
        location.setCity("Energy City");
        location.setPostalCode("12345");
        location.setCountry("USA");
        location.setLatitude(40.7128);
        location.setLongitude(-74.0060);
        client.setLocation(location);

        userRepository.save(client);
        System.out.println("Created client user: client@solarhub.com / client123");

        // Create Seller user
        Seller seller = new Seller();
        seller.setEmail("seller@solarhub.com");
        seller.setFirstName("Bob");
        seller.setLastName("Seller");
        seller.setPassword(passwordEncoder.encode("seller123"));
        seller.setBirthDate(LocalDate.of(1987, 11, 25));
        seller.setPhoneNumber("+1234567894");
        seller.setRole("SELLER");
        userRepository.save(seller);
        System.out.println("Created seller user: seller@solarhub.com / seller123");

        // Create sample products
        Product solarPanel = new Product();
        solarPanel.setName("Premium Solar Panel");
        solarPanel.setDescription("High-efficiency monocrystalline solar panel");
        solarPanel.setCategory("Panels");
        solarPanel.setBasePrice(new java.math.BigDecimal("299.99"));
        solarPanel.setUnit("panel");
        productRepository.save(solarPanel);

        // Create sample tasks
        Task task1 = new Task();
        task1.setDescription("Install solar panels on residential roof");
        task1.setStatus(Task.TaskStatus.PENDING);
        task1.setCreatedAt(LocalDateTime.now().minusDays(5));
        task1.setClient(client);
        task1.setWorker(worker1);
        task1.setProduct(solarPanel);
        taskRepository.save(task1);

        Task task2 = new Task();
        task2.setDescription("Maintenance check on existing solar installation");
        task2.setStatus(Task.TaskStatus.CONFIRMED);
        task2.setCreatedAt(LocalDateTime.now().minusDays(3));
        task2.setConfirmedAt(LocalDateTime.now().minusDays(2));
        task2.setClient(client);
        task2.setWorker(worker1);
        taskRepository.save(task2);

        Task task3 = new Task();
        task3.setDescription("Replace inverter unit");
        task3.setStatus(Task.TaskStatus.IN_PROGRESS);
        task3.setCreatedAt(LocalDateTime.now().minusDays(1));
        task3.setConfirmedAt(LocalDateTime.now().minusHours(12));
        task3.setClient(client);
        task3.setWorker(worker2);
        taskRepository.save(task3);

        System.out.println("Created 3 sample tasks");
        System.out.println("Database initialization complete!");
    }
}
