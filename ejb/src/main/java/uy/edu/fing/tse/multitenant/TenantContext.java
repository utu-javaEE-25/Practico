package uy.edu.fing.tse.multitenant;

/**
 * Contexto para mantener el tenant actual en el hilo de ejecución.
 * Utiliza ThreadLocal para mantener el tenant por request/thread.
 */
public class TenantContext {
    
    private static final ThreadLocal<String> currentTenant = new ThreadLocal<>();
    
    public static void setCurrentTenant(String tenantId) {
        currentTenant.set(tenantId);
    }
    
    public static String getCurrentTenant() {
        return currentTenant.get();
    }
    
    public static void clear() {
        currentTenant.remove();
    }
}
