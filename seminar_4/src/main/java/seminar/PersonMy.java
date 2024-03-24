package seminar;

import jakarta.persistence.*;


@Entity
@Table(name = "persons")
public class PersonMy {
    @Id
    @Column(name = "id_person")
    private Long id;
    @Column(name = "name")
    private String name;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
