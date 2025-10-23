package uy.edu.fing.tse.servicios;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.regex.Pattern;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.PrestadorSaludServiceLocal;
import uy.edu.fing.tse.entidades.PrestadorSalud;
import uy.edu.fing.tse.api.PrestadorSaludPerLocal;

@Stateless
public class PrestadorSaludServiceBean implements PrestadorSaludServiceLocal {

    @EJB
    private PrestadorSaludPerLocal per;

    @EJB
    private TenantProvisioningBean tenantProvisioning;

    // Limitaciones para nombres de schema
    private static final Pattern SCHEMA_PATTERN = Pattern.compile("^[a-z_][a-z0-9_]{1,30}$");

    @Override
    public PrestadorSalud crear(PrestadorSalud prestador) {
        validarCampos(prestador);

        if (per.existeRut(prestador.getRut())) {
            throw new IllegalArgumentException("El RUT ya existe en el sistema.");
        }

        PrestadorSalud creado = per.crear(prestador);

        tenantProvisioning.provisionarTenant(creado.getNombreSchema());

        return creado;
    }

    @Override
    public PrestadorSalud obtener(String rut) {
        return per.obtenerPorRut(rut);
    }

    @Override
    public void actualizar(PrestadorSalud prestador) {
        validarCampos(prestador);

        PrestadorSalud existente = per.obtenerPorRut(prestador.getRut());

        if (existente == null) {
            throw new IllegalArgumentException("No existe el prestador a actualizar.");
        }

        prestador.setTenantId(existente.getTenantId());
        if (prestador.getFechaCreacion() == null) {
            prestador.setFechaCreacion(existente.getFechaCreacion());
        }

        per.actualizar(prestador);
    }

    @Override
    public void eliminar(String rut) {
        desactivar(rut);
    }

    @Override
    public void activar(String rut) {
        per.actualizarEstado(rut, true);
    }

    @Override
    public void desactivar(String rut) {
        per.actualizarEstado(rut, false);
    }

    @Override
    public List<PrestadorSalud> listar() {
        return per.listar();
    }

    private void validarCampos(PrestadorSalud prestador) {
        if (prestador.getNombre() == null || prestador.getNombre().isEmpty()) {
            throw new IllegalArgumentException("El nombre del prestador de salud no puede ser nulo o vacio.");
        }
        if (prestador.getRut() == null || prestador.getRut().isEmpty()) {
            throw new IllegalArgumentException("El RUT del prestador de salud no puede ser nulo o vacio.");
        }

        String schema = prestador.getNombreSchema();
        if (schema == null || schema.isBlank()) {
            throw new IllegalArgumentException("El nombre del schema no puede ser nulo o vacio.");
        }

        String schemaNormalizado = schema.trim().toLowerCase(Locale.ROOT);
        if (!SCHEMA_PATTERN.matcher(schemaNormalizado).matches()) {
            throw new IllegalArgumentException("Nombre de schema invalido. Use [a-z_][a-z0-9_]{1,30}");
        }

        prestador.setNombreSchema(schemaNormalizado);
    }

    @Override
    public void altaDesdeJms(String rut, String nombre, String fecha) {

        LocalDate fechaLocal = LocalDate.parse(fecha);
        LocalDateTime fechaCreacion = fechaLocal.atStartOfDay();

        PrestadorSalud prestador = new PrestadorSalud();
        
        prestador.setRut(rut);
        prestador.setNombre(nombre);
        prestador.setNombreSchema(generarNombreSchema(rut, nombre));
        prestador.setFechaCreacion(fechaCreacion);
        prestador.setFechaModificacion(fechaCreacion);
        crear(prestador);
    }

    private String generarNombreSchema(String rut, String nombre) {
        String base = rut != null ? rut.replaceAll("[^a-zA-Z0-9]", "") : "";

        if (base.isBlank() && nombre != null) {
            base = nombre.replaceAll("[^a-zA-Z0-9]", "");
        }

        if (base.isBlank()) {
            throw new IllegalArgumentException("No se pudo derivar un nombre de schema valido para el tenant.");
        }

        String schema = ("tenant_" + base).toLowerCase(Locale.ROOT).replaceAll("[^a-z0-9_]", "_");

        if (schema.length() > 31) {
            schema = schema.substring(0, 31);
        }

        if (!SCHEMA_PATTERN.matcher(schema).matches()) {
            throw new IllegalArgumentException("Nombre de schema derivado invalido: " + schema);
        }

        return schema;
    }



}
