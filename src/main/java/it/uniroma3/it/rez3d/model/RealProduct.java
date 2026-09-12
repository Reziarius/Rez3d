package it.uniroma3.it.rez3d.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class RealProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private float finalPrice;

    @NotBlank(message = "Selezionare una dimensione")
    private String size = "Piccola";

    @NotNull(message = "Specificare la finitura")
    private Boolean dipinto = false;

    @Min(value = 1, message = "La quantità deve essere di almeno 1")
    private int quantity = 1;

    @ManyToOne
    private PrintFile file;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public float getFinalPrice() {
        return finalPrice;
    }

    public void setFinalPrice(float finalPrice) {
        this.finalPrice = finalPrice;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public Boolean getDipinto() {
        return dipinto;
    }

    public void setDipinto(Boolean dipinto) {
        this.dipinto = dipinto;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public PrintFile getFile() {
        return file;
    }

    public void setFile(PrintFile file) {
        this.file = file;
    }

    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;

        if (obj == null || getClass() != obj.getClass())
            return false;

        RealProduct other = (RealProduct) obj;

        if (id == null || other.id == null)
            return false;

        return id.equals(other.id);
    }


}
