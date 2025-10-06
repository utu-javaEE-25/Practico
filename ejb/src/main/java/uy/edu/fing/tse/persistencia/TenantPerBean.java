package uy.edu.fing.tse.persistencia;

import jakarta.ejb.Singleton;
import uy.edu.fing.tse.api.TenantPerLocal;
import uy.edu.fing.tse.entidades.Tenant;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Singleton
public class TenantPerBean implements TenantPerLocal {

    private final Map<Long, Tenant> dato = new LinkedHashMap<>();
    private long secuencia = 1L;

    @Override
    public Tenant crear(Tenant tenant) {
        if (tenant.getId() == 0) {
            tenant.setId(secuencia++);
        }
        dato.put(tenant.getId(), tenant);
        return tenant;
    }

    @Override
    public Tenant obtenerPorCodigo(String codigo) {
        if (codigo == null) return null;
        for (Tenant t : dato.values()) {
            if (codigo.equals(t.getCodigo())) {
                return t;
            }
        }
        return null;
    }

    @Override
    public List<Tenant> listar() {
        return new ArrayList<>(dato.values());
    }

    @Override
    public void actualizar(Tenant tenant) {
        if (!dato.containsKey(tenant.getId())) {
            throw new IllegalArgumentException("El tenant con ID " + tenant.getId() + " no existe.");
        }
        dato.put(tenant.getId(), tenant);
    }

    @Override
    public void eliminar(String codigo) {
        Tenant tenant = obtenerPorCodigo(codigo);
        if (tenant != null) {
            dato.remove(tenant.getId());
        }
    }

    @Override
    public boolean existeCodigo(String codigo) {
        return obtenerPorCodigo(codigo) != null;
    }
}
