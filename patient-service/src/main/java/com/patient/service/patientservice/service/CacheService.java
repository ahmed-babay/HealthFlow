package com.patient.service.patientservice.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

/**
 * Cache management service for HealthFlow
 * Provides utilities for cache operations and monitoring
 */
@Service
public class CacheService {

    private final CacheManager cacheManager;

    @Autowired
    public CacheService(CacheManager cacheManager) {
        this.cacheManager = cacheManager;
    }

    /**
     * Clear all patient-related caches
     */
    public void clearPatientCaches(UUID patientId) {
        String patientIdStr = patientId.toString();
        
        // Clear patient cache
        clearCacheKey("patients", patientIdStr);
        clearCacheKey("patients", "all-patients");
        
        // Clear allergy caches
        clearCacheKey("allergies", patientIdStr + "-allergies");
        clearCacheKey("allergies", patientIdStr + "-active-allergies");
        clearCacheKey("allergies", patientIdStr + "-severe-allergies");
        
        // Clear medical records cache
        clearCacheKey("medical-records", patientIdStr + "-records");
        
        System.out.println("Cleared all caches for patient: " + patientId);
    }

    /**
     * Clear medical record related caches
     */
    public void clearMedicalRecordCaches(UUID medicalRecordId, UUID patientId) {
        String recordIdStr = medicalRecordId.toString();
        String patientIdStr = patientId.toString();
        
        // Clear medical record cache
        clearCacheKey("medical-records", recordIdStr);
        clearCacheKey("medical-records", patientIdStr + "-records");
        
        // Clear diagnoses and prescriptions cache
        clearCacheKey("diagnoses", recordIdStr + "-diagnoses");
        clearCacheKey("prescriptions", recordIdStr + "-prescriptions");
        
        System.out.println("Cleared medical record caches for record: " + medicalRecordId);
    }

    /**
     * Clear all caches (use with caution)
     */
    public void clearAllCaches() {
        cacheManager.getCacheNames().forEach(cacheName -> {
            Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        });
        System.out.println("Cleared all caches");
    }

    /**
     * Clear specific cache by name
     */
    public void clearCache(String cacheName) {
        Objects.requireNonNull(cacheManager.getCache(cacheName)).clear();
        System.out.println("Cleared cache: " + cacheName);
    }

    /**
     * Clear specific cache key
     */
    public void clearCacheKey(String cacheName, String key) {
        var cache = cacheManager.getCache(cacheName);
        if (cache != null) {
            cache.evict(key);
        }
    }

    /**
     * Get cache statistics (for monitoring)
     */
    public String getCacheStatistics() {
        StringBuilder stats = new StringBuilder("Cache Statistics:\n");
        
        cacheManager.getCacheNames().forEach(cacheName -> {
            var cache = cacheManager.getCache(cacheName);
            if (cache != null) {
                stats.append("Cache: ").append(cacheName).append(" - Active\n");
            }
        });
        
        return stats.toString();
    }

    /**
     * Check if cache contains a specific key
     */
    public boolean isCached(String cacheName, String key) {
        var cache = cacheManager.getCache(cacheName);
        return cache != null && cache.get(key) != null;
    }

    /**
     * Warm up critical caches (pre-load frequently accessed data)
     */
    public void warmUpCaches() {
        System.out.println("Cache warm-up initiated - this would pre-load frequently accessed patient data");
        // In a real implementation, this would pre-load:
        // - Most frequently accessed patients
        // - Active severe allergies
        // - Recent medical records
    }
}

