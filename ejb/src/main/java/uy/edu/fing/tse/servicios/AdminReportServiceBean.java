package uy.edu.fing.tse.servicios;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Locale;
import java.util.stream.Collectors;

import jakarta.ejb.Stateless;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import uy.edu.fing.tse.api.AdminReportServiceLocal;
import uy.edu.fing.tse.dto.reportes.ActividadUsuariosDTO;
import uy.edu.fing.tse.dto.reportes.EventoActividadDTO;
import uy.edu.fing.tse.dto.reportes.ResumenGeneralDTO;
import uy.edu.fing.tse.entidades.AuditLog;

@Stateless
public class AdminReportServiceBean implements AdminReportServiceLocal {

    private static final String ACCION_LOGIN = "LOGIN";
    private static final List<String> RESULTADOS_EXITOSOS = List.of("ok", "success", "exitoso");
    private static final List<String> RESULTADOS_FALLIDOS = List.of("error", "fail", "fallido", "denegado", "rechazado");

    @PersistenceContext(unitName = "PU_CENTRAL")
    private EntityManager em;

    @Override
    public ResumenGeneralDTO obtenerResumenGeneral() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime haceUnaSemana = ahora.minusDays(7);
        LocalDateTime haceUnMes = ahora.minusDays(30);

        ResumenGeneralDTO dto = new ResumenGeneralDTO();
        dto.setPrestadoresTotales(contar("SELECT COUNT(p) FROM PrestadorSalud p"));
        dto.setPrestadoresActivos(contar("SELECT COUNT(p) FROM PrestadorSalud p WHERE p.estado = TRUE"));
        dto.setPrestadoresNuevosMes(contarConFecha(
                "SELECT COUNT(p) FROM PrestadorSalud p WHERE p.fechaCreacion >= :fecha", haceUnMes));

        dto.setUsuariosTotales(contar("SELECT COUNT(u) FROM UsuarioServicioSalud u"));
        dto.setUsuariosActivos(contar("SELECT COUNT(u) FROM UsuarioServicioSalud u WHERE u.activo = TRUE"));
        dto.setUsuariosNuevosSemana(contarConFecha(
                "SELECT COUNT(u) FROM UsuarioServicioSalud u WHERE u.fechaCreacion >= :fecha", haceUnaSemana));

        dto.setAdministradoresTotales(contar("SELECT COUNT(a) FROM AdminHcen a"));
        dto.setAdministradoresActivos(
                contarString("SELECT COUNT(a) FROM AdminHcen a WHERE UPPER(a.estado) = :estado", "ACTIVO"));
        dto.setUltimaActualizacion(ahora);
        return dto;
    }

    @Override
    public ActividadUsuariosDTO obtenerActividadUsuarios() {
        LocalDateTime ahora = LocalDateTime.now();
        LocalDateTime haceUnaSemana = ahora.minusDays(7);
        LocalDateTime haceUnMes = ahora.minusDays(30);

        ActividadUsuariosDTO dto = new ActividadUsuariosDTO();
        dto.setLoginsExitososSemana(contarLoginsPorResultado(haceUnaSemana, RESULTADOS_EXITOSOS));
        dto.setLoginsFallidosSemana(contarLoginsPorResultado(haceUnaSemana, RESULTADOS_FALLIDOS));
        dto.setLoginsExitososMes(contarLoginsPorResultado(haceUnMes, RESULTADOS_EXITOSOS));
        dto.setLoginsFallidosMes(contarLoginsPorResultado(haceUnMes, RESULTADOS_FALLIDOS));
        dto.setEventosRecientes(obtenerUltimosEventos());
        dto.setFechaCorte(ahora);
        return dto;
    }

    private long contar(String jpql) {
        return em.createQuery(jpql, Long.class).getSingleResult();
    }

    private long contarConFecha(String jpql, LocalDateTime fecha) {
        return em.createQuery(jpql, Long.class)
                .setParameter("fecha", fecha)
                .getSingleResult();
    }

    private long contarString(String jpql, String value) {
        return em.createQuery(jpql, Long.class)
                .setParameter("estado", value.toUpperCase(Locale.ROOT))
                .getSingleResult();
    }

    private long contarLoginsPorResultado(LocalDateTime desde, List<String> resultados) {
        return em.createQuery(
                        "SELECT COUNT(a) FROM AuditLog a WHERE a.accion = :accion AND a.fechaCreacion >= :desde "
                                + "AND LOWER(a.resultado) IN :resultados",
                        Long.class)
                .setParameter("accion", ACCION_LOGIN)
                .setParameter("desde", desde)
                .setParameter("resultados", resultados)
                .getSingleResult();
    }

    private List<EventoActividadDTO> obtenerUltimosEventos() {
        TypedQuery<AuditLog> query = em.createQuery("SELECT a FROM AuditLog a ORDER BY a.fechaCreacion DESC",
                AuditLog.class);
        query.setMaxResults(10);
        return query.getResultList()
                .stream()
                .map(this::mapearEvento)
                .collect(Collectors.toList());
    }

    private EventoActividadDTO mapearEvento(AuditLog log) {
        EventoActividadDTO dto = new EventoActividadDTO();
        dto.setAccion(log.getAccion());
        dto.setResultado(log.getResultado());
        dto.setTipoActor(log.getTipoActor());
        dto.setActorId(log.getActorId());
        dto.setIp(log.getIp());
        dto.setFecha(log.getFechaCreacion());
        return dto;
    }
}
