package sv.unab.edu.Dominio;

import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

public class Personas
{
    private Long id;
    private String nombre;
    private String apellidoMaterno;
    private String apellidoPaterno;
    private String dui;
    private String nit;
    private String fechaNacimiento;
    private String telefono;
    private String direccion;
    private String email;

    public Personas() {
    }

    public Personas(Long id) {
        this.id = id;
    }

    public Personas(Long id, String nombre, String apellidoMaterno, String apellidoPaterno, String dui, String nit, String fechaNacimiento, String telefono, String direccion, String email) {
        this.id = id;
        this.nombre = nombre;
        this.apellidoMaterno = apellidoMaterno;
        this.apellidoPaterno = apellidoPaterno;
        this.dui = dui;
        this.nit = nit;
        this.fechaNacimiento = fechaNacimiento;
        this.telefono = telefono;
        this.direccion = direccion;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public String getDui() {
        return dui;
    }

    public String getNit() {
        return nit;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Personas)) return false;
        Personas personas = (Personas) o;
        return Objects.equals(id, personas.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(" ")
                .add("id=" + id)
                .add(nombre)
                .add(apellidoPaterno)
                .add(apellidoMaterno)
                .toString();
    }
}
