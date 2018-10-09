package sv.unab.edu.Infraestructura;

import sv.unab.edu.Dominio.Cliente;
import sv.unab.edu.Negocios.dtsEmpleado;
import sv.unab.edu.Negocios.Util.Filtro;
import sv.unab.edu.Dominio.Empleado;

import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import static java.util.logging.Level.INFO;

public class logEmpleado {

    private dtsEmpleado dtsEmpleado = new dtsEmpleado();
    private static final Logger LOG = Logger.getLogger("sv.edu.unab.agenciaviajes");

    public List<Empleado> obtenerListado(List<Filtro> filtros){
        LOG.log(INFO, "[logEmpleado][obtenerListado]");
        return dtsEmpleado.obtenerListadoEmpleados.apply(filtros);
    }

    public void registrarEmpleado(Empleado empleado){
        LOG.log(INFO, "[logEmpleado][registrarCliente] -> {0}", new Object[]{empleado});
        dtsEmpleado.registrar.accept(empleado);
    }

    public void actualizarEmpleado(Empleado empleado){
        LOG.log(INFO, "[logEmpleado][actualizarCliente] -> {0}", new Object[]{empleado});
        dtsEmpleado.actualizar.accept(empleado);
    }

    public void eliminarEmpleado(Empleado empleado){
        LOG.log(INFO, "[logEmpleado][eliminarCliente] -> {0}", new Object[]{empleado});
        dtsEmpleado.eliminar.accept(empleado);
    }

}
