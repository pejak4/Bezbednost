package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
public class Role {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Enumerated(EnumType.STRING)
    private UserRole role;

    @ManyToMany
    @JoinTable(
            name = "role_authorities",
            joinColumns = @JoinColumn(name = "role_name", referencedColumnName = "role"),
            inverseJoinColumns = @JoinColumn(name = "authority_name", referencedColumnName = "name")
    )
    private List<Authority> authorityList;
}
