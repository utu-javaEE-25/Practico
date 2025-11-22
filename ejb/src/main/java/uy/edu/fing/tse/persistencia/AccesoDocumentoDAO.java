package uy.edu.fing.tse.persistencia;

import java.util.List;

import jakarta.ejb.LocalBean;
import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;

@Stateless
@LocalBean
public class AccesoDocumentoDAO {

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

   
    public List<Object[]> obtenerAccesosPorCedula(String ci) {
        String sql = "SELECT " +
                     "    al.fecha_creacion AS \"Fecha_Visualizacion\", " +
                     "    dcm.id_externa_doc AS \"Documento_Visualizado\", " +
                     "    t.nombre AS \"Clinica_Solicitante\", " +
                     // Usamos una función de agregación para evitar duplicados del JOIN
                     "    MAX(COALESCE(sa.nombre_profesional_solicitante, 'ID Profesional: ' || al.actor_id)) AS \"Profesional_Solicitante\", " +
                     "    al.resultado AS \"Resultado\" " +
                     "FROM " +
                     "    central.audit_log AS al " +
                     "JOIN " +
                     "    central.documento_clinico_metadata AS dcm ON al.recurso_id = dcm.doc_id " +
                     "JOIN " +
                     "    central.usuario_global AS ug ON dcm.user_id = ug.user_id " +
                     "LEFT JOIN " +
                     "    central.tenant AS t ON al.actor_tenant_id = t.tenant_id " +
                     "LEFT JOIN " +
                     "    central.solicitud_acceso AS sa ON sa.doc_id = al.recurso_id " +
                     "        AND sa.id_profesional_solicitante = al.actor_id " +
                     "        AND sa.requester_tenant_id = al.actor_tenant_id " +
                     "WHERE " +
                     "    ug.ci = :ci " +
                     "    AND al.accion = 'VISUALIZAR_DOCUMENTO_EXTERNO' " +
                     "GROUP BY " +
                     "    al.audit_id, " + // Agrupamos por la clave primaria del evento para asegurar una fila única por evento
                     "    dcm.id_externa_doc, " +
                     "    t.nombre " +
                     "ORDER BY " +
                     "    \"Fecha_Visualizacion\" DESC";
        
        Query query = em.createNativeQuery(sql);
        query.setParameter("ci", ci);
        
        @SuppressWarnings("unchecked")
        List<Object[]> resultados = query.getResultList();
        
        return resultados;
    }
}