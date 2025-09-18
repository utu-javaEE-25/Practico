package uy.edu.fing.tse.api;

import java.util.List;
import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.DocumentoClinico;

@Local
public interface DocumentoClinicoServiceLocal {
    DocumentoClinico crear(DocumentoClinico doc);

    DocumentoClinico obtenerPorCodigo(String codigo);

    List<DocumentoClinico> listar();

    void actualizar(DocumentoClinico doc);

    void eliminarPorCodigo(String codigo);

    // Búsqueda (requisito Ej.1): por un atributo. Ej: por CI de paciente
    List<DocumentoClinico> buscarPorPacienteCI(String pacienteCI);
}
