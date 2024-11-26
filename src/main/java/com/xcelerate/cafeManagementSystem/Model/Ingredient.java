package com.xcelerate.cafeManagementSystem.Model;
import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

@Entity
@Table(name = "ingredients")
public class Ingredient {
        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        @Column(unique = true)
        private long id;

        private String name;

        @ManyToMany(mappedBy = "ingredients")
        @JsonIgnore
        private Set<Product> products = new HashSet<>();


        public long getId() {
                return id;
        }

        public void setId(long id) {
                this.id = id;
        }

        public String getName() {
                return name;
        }

        public void setName(String name) {
                this.name = name;
        }

        public Set<Product> getProducts() {
                return products;
        }

        public void setProducts(Set<Product> products) {
                this.products = products;
        }
}
