
package com.example.Marketplace.Entity;

// import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;


@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario implements UserDetails {  // Implementa UserDetails
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String nombre;

    @Column(nullable = false)
    private String apellido;

    @Enumerated(EnumType.STRING)
    private Rol role;

    @OneToMany(mappedBy = "usuario")
    private List<Orden> orders;

    @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL, orphanRemoval = true)
    private Carrito carrito;

    // Métodos requeridos por UserDetails
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.getAuthority()));
    }

    @Override
    public String getUsername() {
        return this.username;  // O this.email si prefieres usar email como username
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    // Constructor sin el Carrito
    public Usuario(String username, String email, String password, String nombre, String apellido, Rol role) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.nombre = nombre;
        this.apellido = apellido;
        this.role = role;
        this.carrito = null;
    }

    public void setCarrito(Carrito carrito) {
        if (carrito == null) {
            if (this.carrito != null) {
                this.carrito.setUsuario(null);
            }
        } else {
            carrito.setUsuario(this);
        }
        this.carrito = carrito;
    }
}

// package com.example.Marketplace.Entity;

// // import com.fasterxml.jackson.annotation.JsonManagedReference;
// import jakarta.persistence.*;
// import lombok.*;

// import java.util.List;

// @Entity
// @Data
// @Builder
// @NoArgsConstructor
// @AllArgsConstructor
// public class Usuario {

//     @Id
//     @GeneratedValue(strategy = GenerationType.IDENTITY)
//     private Long id;

//     @Column(nullable = false, unique = true)
//     private String username;

//     @Column(nullable = false, unique = true)
//     private String email;

//     @Column(nullable = false)
//     private String password;

//     @Column(nullable = false)
//     private String nombre;

//     @Column(nullable = false)
//     private String apellido;

//     @Enumerated(EnumType.STRING)
//     private Rol role;

//     @OneToMany(mappedBy = "usuario")
//     // @JsonManagedReference // evita el ciclo
//     private List<Orden> orders;

//     @OneToOne(mappedBy = "usuario", cascade = CascadeType.ALL)
//     private Carrito carrito;

//     // Constructor sin el Carrito
//     public Usuario(String username, String email, String password, String nombre, String apellido, Rol role) {
//         this.username = username;
//         this.email = email;
//         this.password = password;
//         this.nombre = nombre;
//         this.apellido = apellido;
//         this.role = role;
//         this.carrito = null;  // El carrito se establece a null por defecto
//     }
// }
