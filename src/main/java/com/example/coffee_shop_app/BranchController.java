package com.example.coffee_shop_app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/branches")
public class BranchController {

    @Autowired
    private BranchRepository branchRepository;

    // Получить все филиалы
    @GetMapping
    public List<Branch> getAllBranches() {
        return branchRepository.findAll();
    }

    // Создать новый филиал
    @PostMapping
    public Branch createBranch(@RequestBody Branch branch) {
        return branchRepository.save(branch);
    }

    // Получить филиал по ID
    @GetMapping("/{id}")
    public ResponseEntity<Branch> getBranchById(@PathVariable Long id) {
        return branchRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Обновить филиал
    @PutMapping("/{id}")
    public ResponseEntity<Branch> updateBranch(@PathVariable Long id, @RequestBody Branch branchDetails) {
        return branchRepository.findById(id).map(branch -> {
            branch.setName(branchDetails.getName());
            branch.setAddress(branchDetails.getAddress());
            branch.setOpenTime(branchDetails.getOpenTime());
            branch.setCloseTime(branchDetails.getCloseTime());
            branch.setImageUrl(branchDetails.getImageUrl());

            Branch updated = branchRepository.save(branch);
            return ResponseEntity.ok(updated);
        }).orElse(ResponseEntity.notFound().build());
    }

    // Удалить филиал
    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteBranch(@PathVariable Long id) {
        return branchRepository.findById(id).map(branch -> {
            branchRepository.delete(branch);
            return ResponseEntity.noContent().build();
        }).orElse(ResponseEntity.notFound().build());
    }
}
