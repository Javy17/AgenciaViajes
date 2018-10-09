package sv.unab.edu.Dominio;

import java.util.Objects;
import java.util.StringJoiner;

public class Empleado
{
    private Long id;
    private String seguro;
    private String afp;
    private Persona datosPersonales;

    public Empleado() {
    }



    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSeguro() {
        return seguro;
    }

    public void setSeguro(String seguro) {
        this.seguro = seguro;
    }

    public String getAfp() {
        return afp;
    }

    public void setAfp(String afp) {
        this.afp = afp;
    }

    public Persona getDatosPersonales() {
        return datosPersonales;
    }

    public void setDatosPersonales(Persona datosPersonales) {
        this.datosPersonales = datosPersonales;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;

        Empleado empleado = (Empleado) o;

        return id.equals(empleado.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(" ")
                .add(datosPersonales.getNombre())
                .add(datosPersonales.getApellidoPaterno())
                .add(datosPersonales.getApellidoMaterno())
                .toString();
    }
}
