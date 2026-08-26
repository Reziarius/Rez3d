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
    private long id;
    private float finalPrice;
    private String size;
    private Boolean isPainted;
    private int quantity;
    @ManyToOne
    private Order order;

    @ManyToOne
    private PrintFile file;
    public long getId() {
        return id;
    }
    public void setId(long id) {
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
        return isPainted;
    }
    public void setDipinto(Boolean isPainted) {
        this.isPainted = isPainted;
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + Float.floatToIntBits(finalPrice);
        result = prime * result + ((size == null) ? 0 : size.hashCode());
        result = prime * result + ((isPainted == null) ? 0 : isPainted.hashCode());
        result = prime * result + quantity;
        result = prime * result + ((file == null) ? 0 : file.hashCode());
        return result;
    }
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        RealProduct other = (RealProduct) obj;
        if (id != other.id)
            return false;
        if (Float.floatToIntBits(finalPrice) != Float.floatToIntBits(other.finalPrice))
            return false;
        if (size == null) {
            if (other.size != null)
                return false;
        } else if (!size.equals(other.size))
            return false;
        if (isPainted == null) {
            if (other.isPainted != null)
                return false;
        } else if (!isPainted.equals(other.isPainted))
            return false;
        if (quantity != other.quantity)
            return false;
        if (file == null) {
            if (other.file != null)
                return false;
        } else if (!file.equals(other.file))
            return false;
        return true;
    }

    

}
