package uy.edu.fing.tse.rest;

import jakarta.ws.rs.ApplicationPath;
import jakarta.ws.rs.core.Application;

/**
 * JAX-RS configuration class for the REST API.
 * All REST endpoints will be available under /api/*
 */
@ApplicationPath("/api")
public class RestApplication extends Application {
    // No additional configuration needed - all REST resources will be auto-discovered
}
