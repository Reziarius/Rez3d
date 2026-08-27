package it.uniroma3.it.rez3d.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;

@Entity
public class RealProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    private float finalPrice;
    private String size;
    private Boolean dipinto;
    private int quantity;
    @ManyToOne
    private Order order;

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
