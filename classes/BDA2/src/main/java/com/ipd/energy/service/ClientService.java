package com.ipd.energy.service;

import com.ipd.energy.dto.CreateClientRequest;
import com.ipd.energy.dto.UserDTO;
import com.ipd.energy.entity.Client;
import com.ipd.energy.entity.Location;
import com.ipd.energy.repository.ClientRepository;
import com.ipd.energy.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public List<UserDTO> getAllClients() {
        return clientRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public UserDTO getClientByEmail(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found with email: " + email));
        return convertToDTO(client);
    }

    public UserDTO createClient(CreateClientRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("Email already exists");
        }

        Location location = new Location(
                request.getAddress(),
                request.getCity(),
                request.getPostalCode(),
                request.getCountry(),
                request.getLatitude(),
                request.getLongitude());

        Client client = new Client();
        client.setEmail(request.getEmail());
        client.setFirstName(request.getFirstName());
        client.setLastName(request.getLastName());
        client.setPassword(passwordEncoder.encode(request.getPassword()));
        client.setPhoneNumber(request.getPhoneNumber());
        client.setBirthDate(request.getBirthDate());
        client.setLocation(location);
        client.setRole("CLIENT");

        Client savedClient = clientRepository.save(client);
        return convertToDTO(savedClient);
    }

    public void deleteClient(String email) {
        Client client = clientRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Client not found with email: " + email));
        clientRepository.delete(client);
    }

    private UserDTO convertToDTO(Client client) {
        return new UserDTO(
                client.getEmail(),
                client.getFirstName(),
                client.getLastName(),
                client.getPhoneNumber(),
                client.getRole(),
                null);
    }
}
