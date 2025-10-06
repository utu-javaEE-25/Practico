package uy.edu.fing.tse.util;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.AfterEach;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Test class for TenantContext functionality
 */
public class TenantContextTest {
    
    @AfterEach
    public void cleanup() {
        TenantContext.clear();
    }
    
    @Test
    public void testSetAndGetCurrentTenant() {
        String testRUT = "123456789012";
        TenantContext.setCurrentTenant(testRUT);
        
        assertEquals(testRUT, TenantContext.getCurrentTenant());
    }
    
    @Test
    public void testClearTenant() {
        String testRUT = "123456789012";
        TenantContext.setCurrentTenant(testRUT);
        
        assertNotNull(TenantContext.getCurrentTenant());
        
        TenantContext.clear();
        
        assertNull(TenantContext.getCurrentTenant());
    }
    
    @Test
    public void testThreadIsolation() throws InterruptedException {
        String tenant1 = "111111111111";
        String tenant2 = "222222222222";
        
        TenantContext.setCurrentTenant(tenant1);
        
        Thread thread = new Thread(() -> {
            TenantContext.setCurrentTenant(tenant2);
            assertEquals(tenant2, TenantContext.getCurrentTenant());
            TenantContext.clear();
        });
        
        thread.start();
        thread.join();
        
        // Main thread should still have tenant1
        assertEquals(tenant1, TenantContext.getCurrentTenant());
    }
    
    @Test
    public void testNullTenant() {
        TenantContext.setCurrentTenant(null);
        assertNull(TenantContext.getCurrentTenant());
    }
}
