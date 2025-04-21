//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.frontofficeapi.service;

import com.example.frontofficeapi.model.Request;
import com.example.frontofficeapi.repository.RequestRepository;
import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RequestService {
    @Autowired
    private RequestRepository requestRepository;

    public RequestService() {
    }

    public Request saveDemande(Request request) {
        return (Request)this.requestRepository.save(request);
    }

    public List<Request> getAll() {
        return this.requestRepository.findAll();
    }

    public Optional<Request> getById(Long id) {
        return this.requestRepository.findById(id);
    }

    public void deleteById(Long id) {
        this.requestRepository.deleteById(id);
    }
}
