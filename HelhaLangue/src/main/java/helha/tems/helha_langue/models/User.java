package helha.tems.helha_langue.models;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;
import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "utilisateur")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "est_professeur")
public class User {
    @Id
    @Column(name = "email")
    private String email;

    @Column(name = "nom")
    private String LastName;

    @Column(name = "prenom")
    private String FirstName;

    @JsonIgnore
    @Column(name = "est_professeur",insertable = false, updatable = false)
    private String est_professeur;


    @ManyToMany(cascade = {CascadeType.ALL, CascadeType.REMOVE})
    @JoinTable(
            name = "utilisateur_sequence",
            joinColumns = @JoinColumn(name = "utilisateur_email", referencedColumnName = "email"),
            inverseJoinColumns = @JoinColumn(name = "sequence_id", referencedColumnName = "sequence_id")
    )
    private Set<Sequence> sequences = new HashSet<>();

}
