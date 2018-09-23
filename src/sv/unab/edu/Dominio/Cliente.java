package sv.unab.edu.Dominio;

import java.util.Objects;
import java.util.StringJoiner;

public class Cliente {
    private Long id;
    private Long idPersona;

    public Cliente() {
    }

    public Cliente(Long id) {
        this.id = id;
    }

    public Cliente(Long id, Long idPersona) {
        this.id = id;
        this.idPersona = idPersona;
    }

    public Long getId() {
        return id;
    }

    public Long getIdPersona() {
        return idPersona;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setIdPersona(Long idPersona) {
        this.idPersona = idPersona;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cliente)) return false;
        Cliente cliente = (Cliente) o;
        return Objects.equals(id, cliente.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return new StringJoiner(", ", Cliente.class.getSimpleName() + "[", "]")
                .add("id=" + id)
                .toString();
    }
}
