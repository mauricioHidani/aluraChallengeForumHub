package br.com.alura.challenges.forum.hub.models.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "usuarios")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nome",
            columnDefinition = "VARCHAR(128)")
    private String name;

    @Column(name = "email",
            nullable = false,
            unique = true,
            columnDefinition = "VARCHAR(255)")
    private String email;

    @Column(name = "senha",
            columnDefinition = "VARCHAR(255)")
    private String password;

    @ManyToMany
    @JoinTable(name = "perfis_de_usuarios",
               joinColumns = @JoinColumn(name = "usuario_id"),
               inverseJoinColumns = @JoinColumn(name = "perfil_id"))
    private Set<Role> roles = new HashSet<>();

    protected User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public User(String name, String email, String password, Set<Role> roles) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.roles = roles;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public Set<Role> getRules() {
        return roles;
    }

    public void addRule(Role role) {
        this.roles.add(role);
    }

}
