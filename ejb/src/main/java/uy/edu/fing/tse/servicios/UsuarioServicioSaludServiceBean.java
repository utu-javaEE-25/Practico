package uy.edu.fing.tse.servicios;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.UsuarioServicioSaludPerLocal;
import uy.edu.fing.tse.api.UsuarioServicioSaludServiceLocal;
import uy.edu.fing.tse.api.UsuarioServicioSaludServiceRemote;
import uy.edu.fing.tse.entidades.UsuarioServicioSalud;

import java.util.List;

@Stateless

public class UsuarioServicioSaludServiceBean implements UsuarioServicioSaludServiceLocal, UsuarioServicioSaludServiceRemote {

    @EJB
    private UsuarioServicioSaludPerLocal per;

    @Override
    public UsuarioServicioSalud crear(UsuarioServicioSalud usuario) {
        validarCampos(usuario);

        // REGLA DE NEGOCIO: No pueden existir dos usuarios con la misma cédula
        if (per.existeCI(usuario.getCedulaIdentidad())) {
            throw new IllegalArgumentException("La cédula de identidad '" + usuario.getCedulaIdentidad() + "' ya existe en el sistema.");
        }

        return per.crear(usuario);
    }

    @Override
    public UsuarioServicioSalud obtenerPorCI(String cedulaIdentidad) {
        return per.obtenerPorCI(cedulaIdentidad);
    }

    @Override
    public void actualizar(UsuarioServicioSalud usuario) {
        validarCampos(usuario);

        UsuarioServicioSalud existente = per.obtenerPorCI(usuario.getCedulaIdentidad());
        if (existente == null) {
            throw new IllegalArgumentException("No se encontró el usuario a actualizar con CI: " + usuario.getCedulaIdentidad());
        }

        // Asignamos el ID del objeto existente para asegurar que actualizamos el correcto
        usuario.setId(existente.getId());
        per.actualizar(usuario);
    }

    @Override
    public void eliminar(String cedulaIdentidad) {
        per.eliminar(cedulaIdentidad);
    }

    @Override
    public List<UsuarioServicioSalud> listar() {
        return per.listar();
    }

    // Método privado para validaciones básicas
    private void validarCampos(UsuarioServicioSalud usuario) {
        if (usuario.getNombreCompleto() == null || usuario.getNombreCompleto().trim().isEmpty()) {
            throw new IllegalArgumentException("El nombre del usuario no puede ser vacío.");
        }
        if (usuario.getCedulaIdentidad() == null || usuario.getCedulaIdentidad().trim().isEmpty()) {
            throw new IllegalArgumentException("La cédula de identidad no puede ser vacía.");
        }
        if (usuario.getFechaNacimiento() == null) {
            throw new IllegalArgumentException("La fecha de nacimiento es obligatoria.");
        }
    }
}