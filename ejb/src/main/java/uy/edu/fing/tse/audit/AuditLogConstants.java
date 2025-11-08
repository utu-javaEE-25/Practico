package uy.edu.fing.tse.audit;

public final class AuditLogConstants {

    private AuditLogConstants() {
    }

    public static final class Acciones {
        public static final String PRESTADOR_ALTA = "PRESTADOR_ALTA";
        public static final String PRESTADOR_MODIFICACION = "PRESTADOR_MODIFICACION";
        public static final String PRESTADOR_BAJA = "PRESTADOR_BAJA";
        public static final String PRESTADOR_ACTIVACION = "PRESTADOR_ACTIVACION";
        public static final String ENDPOINT_ALTA = "ENDPOINT_ALTA";
        public static final String ENDPOINT_MODIFICACION = "ENDPOINT_MODIFICACION";
        public static final String ENDPOINT_BAJA = "ENDPOINT_BAJA";
        public static final String LOGIN = "LOGIN";

        private Acciones() {
        }
    }

    public static final class Resultados {
        public static final String SUCCESS = "SUCCESS";
        public static final String FAILURE = "FAILURE";

        private Resultados() {
        }
    }

    public static final class TipoActor {
        public static final String ADMIN_GLOBAL = "ADMIN_GLOBAL";
        public static final String USUARIO = "USUARIO";
        public static final String SYSTEM = "SYSTEM";

        private TipoActor() {
        }
    }
}
