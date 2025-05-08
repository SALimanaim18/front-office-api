//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package com.example.frontofficeapi.controller;

import com.example.frontofficeapi.entity.Request;
import com.example.frontofficeapi.service.RequestService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping({"/SangConnect/api/demandes"})

public class RequestController {
    @Autowired
    private RequestService requestService;

    public RequestController() {
    }

    @PostMapping
    public ResponseEntity<Request> create(@RequestBody Request request) {
        Request savedRequest = this.requestService.saveDemande(request);
        return ResponseEntity.ok(savedRequest);
    }

    @GetMapping
    public ResponseEntity<List<Request>> getAll() {
        List<Request> requests = this.requestService.getAll();
        return ResponseEntity.ok(requests);
    }

    @GetMapping({"/{id}"})
    public ResponseEntity<Request> getById(@PathVariable Long id) {
        return (ResponseEntity)this.requestService.getById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping({"/{id}"})
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        this.requestService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/blood-types")
    public ResponseEntity<List<String>> getAllBloodTypes() {
        List<String> bloodTypes = List.of("A+", "A-", "B+", "B-", "AB+", "AB-", "O+", "O-");
        return ResponseEntity.ok(bloodTypes);
    }

}
