package sv.unab.edu.Dominio;

import sv.unab.edu.Dominio.*;
import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(schema = "avr", name = "persona")
@SequenceGenerator(schema = "avr", sequenceName = "persona_id_seq", name = "Persona_seq_id", allocationSize = 1)
@NamedQueries({
        @NamedQuery(name = "Persona.finTodas", query = "SELECT p FROM Persona p"),
        @NamedQuery(name = "Persona.finNombre", query = "SELECT p FROM Persona p WHERE p.nombre=:nombre")
})


public class Persona implements Serializable {
    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Persona_seq_id")
    @Column(name = "Id")
    private Long id;
    @Column(name = "nombre")
    private String nombre;
    @Column(name = "apellidoMaterno")
    private String apellidoMaterno;
    @Column(name = "apellidoPaterno")
    private String apellidoPaterno;
    @NotNull
    @Size(min = 5, max = 50)
    @Column(name = "dui")
    //@NotNull
    private String dui;
    private String nit;
    @Temporal(TemporalType.DATE)
    @NotNull
    @Column(name = "fechaNacimiento")
    private LocalDate fechaNacimiento;
    @NotNull
    @Size(min = 9)
    @Column(name = "telefono")
    private String telefono;
    @Lob
    @NotNull
    @Column(name = "direccion")
    private String direccion;
    @Pattern(regexp = "[A-Za-z0-9+_.-]+@(.+)$")
    @NotNull
    @Column(name = "email")
    private String email;

    @OneToOne(mappedBy = "datosPersonales", fetch = FetchType.LAZY, targetEntity = Empleado2.class)
    private Empleado2 empleado;

    public Persona() {
    }

    public Persona(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getDui() {
        return dui;
    }

    public void setDui(String dui) {
        this.dui = dui;
    }

    public String getNit() {
        return nit;
    }

    public void setNit(String nit) {
        this.nit = nit;
    }

    public LocalDate getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(LocalDate fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Persona)) return false;

        Persona persona = (Persona) o;

        return id.equals(persona.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }

    @Override
    public String toString() {
        return new StringJoiner(" ")
                .add(nombre)
                .add(apellidoPaterno)
                .add(apellidoMaterno)
                .toString();
    }
}
