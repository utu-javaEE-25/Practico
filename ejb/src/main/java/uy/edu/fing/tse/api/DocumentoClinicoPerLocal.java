package uy.edu.fing.tse.api;

import java.util.List;
import jakarta.ejb.Local;
import uy.edu.fing.tse.entidades.DocumentoClinico;

@Local
public interface DocumentoClinicoPerLocal {
    DocumentoClinico crear(DocumentoClinico doc);

    DocumentoClinico obtenerPorCodigo(String codigo);

    List<DocumentoClinico> listar();

    void actualizar(DocumentoClinico doc);

    void eliminarPorCodigo(String codigo);

    boolean existeCodigo(String codigo);
}
