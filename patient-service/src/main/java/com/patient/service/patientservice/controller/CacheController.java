package com.patient.service.patientservice.controller;

import com.patient.service.patientservice.service.CacheService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

/**
 * Cache management endpoints for HealthFlow
 * Used for monitoring and administrative cache operations
 */
@RestController
@RequestMapping("/cache")
@PreAuthorize("hasRole('ADMIN')") // Only admins can manage caches
public class CacheController {

    private final CacheService cacheService;

    @Autowired
    public CacheController(CacheService cacheService) {
        this.cacheService = cacheService;
    }

    /**
     * Get cache statistics
     */
    @GetMapping("/stats")
    public ResponseEntity<String> getCacheStatistics() {
        String stats = cacheService.getCacheStatistics();
        return ResponseEntity.ok(stats);
    }

    /**
     * Clear all caches (admin only)
     */
    @DeleteMapping("/all")
    public ResponseEntity<String> clearAllCaches() {
        cacheService.clearAllCaches();
        return ResponseEntity.ok("All caches cleared successfully");
    }

    /**
     * Clear specific cache by name
     */
    @DeleteMapping("/{cacheName}")
    public ResponseEntity<String> clearCache(@PathVariable String cacheName) {
        cacheService.clearCache(cacheName);
        return ResponseEntity.ok("Cache '" + cacheName + "' cleared successfully");
    }

    /**
     * Clear all patient-related caches
     */
    @DeleteMapping("/patient/{patientId}")
    public ResponseEntity<String> clearPatientCaches(@PathVariable UUID patientId) {
        cacheService.clearPatientCaches(patientId);
        return ResponseEntity.ok("Patient caches cleared for ID: " + patientId);
    }

    /**
     * Clear medical record caches
     */
    @DeleteMapping("/medical-record/{recordId}/patient/{patientId}")
    public ResponseEntity<String> clearMedicalRecordCaches(@PathVariable UUID recordId, 
                                                           @PathVariable UUID patientId) {
        cacheService.clearMedicalRecordCaches(recordId, patientId);
        return ResponseEntity.ok("Medical record caches cleared for record ID: " + recordId);
    }

    /**
     * Warm up caches
     */
    @PostMapping("/warmup")
    public ResponseEntity<String> warmUpCaches() {
        cacheService.warmUpCaches();
        return ResponseEntity.ok("Cache warm-up initiated");
    }

    /**
     * Check if specific data is cached
     */
    @GetMapping("/check/{cacheName}/{key}")
    public ResponseEntity<String> checkCache(@PathVariable String cacheName, @PathVariable String key) {
        boolean isCached = cacheService.isCached(cacheName, key);
        return ResponseEntity.ok("Cache '" + cacheName + "' contains key '" + key + "': " + isCached);
    }
}

