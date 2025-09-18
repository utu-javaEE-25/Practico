package uy.edu.fing.tse.servicios;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.ejb.EJB;
import jakarta.ejb.Stateless;
import uy.edu.fing.tse.api.DocumentoClinicoPerLocal;
import uy.edu.fing.tse.api.DocumentoClinicoServiceLocal;
import uy.edu.fing.tse.entidades.DocumentoClinico;

@Stateless
public class DocumentoClinicoServiceBean implements DocumentoClinicoServiceLocal {

    @EJB
    private DocumentoClinicoPerLocal per;

    @Override
    public DocumentoClinico crear(DocumentoClinico doc) {
        validar(doc);
        if (per.existeCodigo(doc.getCodigo())) {
            throw new IllegalArgumentException("El código '" + doc.getCodigo() + "' ya existe.");
        }
        return per.crear(doc);
    }

    @Override
    public DocumentoClinico obtenerPorCodigo(String codigo) {
        return per.obtenerPorCodigo(codigo);
    }

    @Override
    public List<DocumentoClinico> listar() {
        return per.listar();
    }

    @Override
    public void actualizar(DocumentoClinico doc) {
        validar(doc);
        DocumentoClinico existente = per.obtenerPorCodigo(doc.getCodigo());
        if (existente == null)
            throw new IllegalArgumentException("No existe el documento a actualizar.");
        doc.setId(existente.getId());
        per.actualizar(doc);
    }

    @Override
    public void eliminarPorCodigo(String codigo) {
        per.eliminarPorCodigo(codigo);
    }

    @Override
    public List<DocumentoClinico> buscarPorPacienteCI(String pacienteCI) {
        List<DocumentoClinico> out = new ArrayList<>();
        for (DocumentoClinico d : per.listar()) {
            if (d.getPacienteCI() != null && d.getPacienteCI().equalsIgnoreCase(pacienteCI))
                out.add(d);
        }
        return out;
    }

    private void validar(DocumentoClinico doc) {
        if (doc.getCodigo() == null || doc.getCodigo().isBlank())
            throw new IllegalArgumentException("El código es obligatorio.");
        if (doc.getPacienteCI() == null || doc.getPacienteCI().isBlank())
            throw new IllegalArgumentException("La cédula del paciente es obligatoria.");
        if (doc.getFechaEmision() == null)
            throw new IllegalArgumentException("La fecha de emisión es obligatoria.");
        if (doc.getFechaEmision().isAfter(LocalDate.now()))
            throw new IllegalArgumentException("La fecha de emisión no puede ser futura.");
    }
}
