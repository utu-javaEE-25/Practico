package uy.edu.fing.tse.servicios;

import java.sql.Connection;
import java.sql.Statement;
import java.util.regex.Pattern;
import jakarta.annotation.Resource;
import jakarta.ejb.Stateless;
import jakarta.ejb.TransactionAttribute;
import jakarta.ejb.TransactionAttributeType;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;

@Stateless
public class TenantProvisioningBean {

    @Resource(lookup = "java:/jdbc/DS_MASTER")
    private DataSource dsMaster;

    //Estandarizo los nombres de schema a minusculas sin espacios ni caracteres raros
    private static final Pattern SCHEMA_PATTERN = Pattern.compile("^[a-z_][a-z0-9_]{1,30}$");

    private String validarSchema(String input) {
        String s = input == null ? "" : input.trim().toLowerCase();
        if (!SCHEMA_PATTERN.matcher(s).matches()) {
            throw new IllegalArgumentException("Nombre de esquema inválido. Use [a-z_][a-z0-9_]{1,30}");
        }
        return s;
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void crearSchemaTenant(String nombreEsquema) throws Exception {
        final String schema = validarSchema(nombreEsquema);

        try (Connection c = dsMaster.getConnection();
             Statement st = c.createStatement()) {
            st.executeUpdate("CREATE SCHEMA IF NOT EXISTS " + schema);
            st.executeUpdate("REVOKE ALL ON SCHEMA " + schema + " FROM PUBLIC");
            st.executeUpdate("GRANT USAGE ON SCHEMA " + schema + " TO hcen_tenant");
            st.executeUpdate("GRANT CREATE ON SCHEMA " + schema + " TO hcen_tenant");
            st.executeUpdate("GRANT SELECT,INSERT,UPDATE,DELETE ON ALL TABLES IN SCHEMA " + schema + " TO hcen_tenant");
            st.executeUpdate("GRANT ALL ON ALL SEQUENCES IN SCHEMA " + schema + " TO hcen_tenant");
            st.executeUpdate("ALTER DEFAULT PRIVILEGES IN SCHEMA " + schema + " "
                + "GRANT SELECT,INSERT,UPDATE,DELETE ON TABLES TO hcen_tenant");
            st.executeUpdate("ALTER DEFAULT PRIVILEGES IN SCHEMA " + schema + " "
                + "GRANT ALL ON SEQUENCES TO hcen_tenant");
            st.executeUpdate("INSERT INTO " + nombreEsquema + ".admin_tenant (nombre_usuario, password_hash, nombre, apellido, email, estado)\r\n" + //
                                "VALUES ('admin', '$2a$10$Ws4d3QITf3d4gZMFiRIu8.PF2th8.6O516MvVn09Il35lM9lulc0G', 'Admin', 'Tenant', 'admin@tenant', 'ACTIVE');");
        }
  }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void migrarSchemaTenant(String schema) {
        schema = validarSchema(schema);

        Flyway flyway = Flyway.configure()
            .dataSource("jdbc:postgresql://10.1.3.13:5432/hcen", "hcen_tenant", "hcen_pass")
            .schemas(schema)
            .locations("classpath:database")
            .baselineOnMigrate(true)
            .load();

        flyway.migrate();
    }

    @TransactionAttribute(TransactionAttributeType.NOT_SUPPORTED)
    public void provisionarTenant(String nombreEsquema) {
        try {
            crearSchemaTenant(nombreEsquema);
        } catch (Exception e) {
            throw new IllegalStateException("Error al crear el schema del tenant", e);
        }

        migrarSchemaTenant(nombreEsquema);
  }

}
