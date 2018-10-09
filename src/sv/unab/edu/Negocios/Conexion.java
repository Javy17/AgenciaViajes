package sv.unab.edu.Negocios;

import org.apache.commons.dbcp2.BasicDataSource;

import javax.sound.sampled.Line;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Conexion
{
    private static final Logger LOG = Logger.getLogger("sv.unab.edu.ageciaViajes");
    private static BasicDataSource dataSource = new BasicDataSource();
    static
    {
        LOG.log(Level.INFO, "[Conexion][Init] -> Aperturando Conexion");
        try
        {
            dataSource.setDriverClassName("org.postgresql.Driver");
            dataSource.setUrl("jdbc:postgresql://localhost:5432/agencia_vuelo");
            dataSource.setUsername("postgres");
            dataSource.setPassword("trance4ever");
            dataSource.setMinIdle(5);
            dataSource.setMaxIdle(10);
            dataSource.setInitialSize(5);

        }
        catch (Exception e)
        {
            LOG.log(Level.SEVERE, "[Conexion][INIT][Exception] -> ", e);
        }
    }
    public  Connection getConexion() throws SQLException
    {
        return dataSource.getConnection();
    }
}
