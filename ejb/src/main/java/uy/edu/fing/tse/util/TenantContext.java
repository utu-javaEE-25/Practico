package uy.edu.fing.tse.util;

/**
 * Utility class to manage tenant context in a multi-tenant application.
 * Stores the current tenant (prestadorRUT) in a ThreadLocal variable.
 */
public class TenantContext {
    
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    
    public static void setCurrentTenant(String prestadorRUT) {
        currentTenant.set(prestadorRUT);
    }
    
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
    
    public static void clear() {
        currentTenant.remove();
    }
}
