package sv.unab.edu.Dominio;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Objects;
import java.util.StringJoiner;

@Entity
@Table(schema = "avr", name = "empleado")
@SequenceGenerator(schema = "avr", sequenceName = "empleado_id_seq", name = "Empleado_seq_id", allocationSize = 1)
public class Empleado2
{
    private static final Long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Empleado_seq_id")
    @Column(name = "Id")
    private Integer Id;
    @NotNull
    @Column(name = "Seguro")
    private String seguro;
    @NotNull
    @Column(name = "afp")
    private String afp;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY, targetEntity = Persona.class, optional = true)
    @JoinColumn(name = "idpersona", referencedColumnName = "Id", unique = true)
    private Persona datosPersonles;



    public Integer getId() {
        return Id;
    }

    public void setId(Integer id) {
        Id = id;
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

    public Persona getPersona() {
        return datosPersonles;
    }

    public void setPersona(Persona persona) {
        this.datosPersonles = persona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado2)) return false;
        Empleado2 empleado2 = (Empleado2) o;
        return Objects.equals(Id, empleado2.Id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(Id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Empleado2.class.getSimpleName() + "[", "]")
                .add("Id=" + Id)
                .toString();
    }
}
