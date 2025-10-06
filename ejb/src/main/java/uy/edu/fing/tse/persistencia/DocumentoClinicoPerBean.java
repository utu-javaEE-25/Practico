package uy.edu.fing.tse.persistencia;

import java.util.*;
import jakarta.ejb.Singleton;
import uy.edu.fing.tse.api.DocumentoClinicoPerLocal;
import uy.edu.fing.tse.api.DocumentoClinicoPerRemote;
import uy.edu.fing.tse.entidades.DocumentoClinico;
import uy.edu.fing.tse.util.TenantContext;

@Singleton
public class DocumentoClinicoPerBean implements DocumentoClinicoPerLocal, DocumentoClinicoPerRemote {

    private final Map<Long, DocumentoClinico> data = new LinkedHashMap<>();
    private long sequence = 1L;

    @Override
    public DocumentoClinico crear(DocumentoClinico doc) {
        if (doc.getId() == 0)
            doc.setId(sequence++);
        data.put(doc.getId(), doc);
        return doc;
    }

    @Override
    public DocumentoClinico obtenerPorCodigo(String codigo) {
        if (codigo == null)
            return null;
        String tenantRUT = TenantContext.getCurrentTenant();
        for (DocumentoClinico d : data.values()) {
            if (codigo.equalsIgnoreCase(d.getCodigo())) {
                if (tenantRUT == null || tenantRUT.equals(d.getPrestadorRUT())) {
                    return d;
                }
            }
        }
        return null;
    }

    @Override
    public List<DocumentoClinico> listar() {
        String tenantRUT = TenantContext.getCurrentTenant();
        if (tenantRUT == null) {
            return new ArrayList<>(data.values());
        }
        List<DocumentoClinico> result = new ArrayList<>();
        for (DocumentoClinico d : data.values()) {
            if (tenantRUT.equals(d.getPrestadorRUT())) {
                result.add(d);
            }
        }
        return result;
    }

    @Override
    public void actualizar(DocumentoClinico doc) {
        if (!data.containsKey(doc.getId())) {
            throw new IllegalArgumentException("No existe el documento con ID " + doc.getId());
        }
        data.put(doc.getId(), doc);
    }

    @Override
    public void eliminarPorCodigo(String codigo) {
        DocumentoClinico d = obtenerPorCodigo(codigo);
        if (d != null)
            data.remove(d.getId());
    }

    @Override
    public boolean existeCodigo(String codigo) {
        return obtenerPorCodigo(codigo) != null;
    }
}
