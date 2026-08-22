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
    private float price;
    private String scale;
    private Boolean dipinto;
    private int quantity;
    @ManyToOne
    private PrintFile file;
    public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getScale() {
        return scale;
    }
    public void setScale(String scale) {
        this.scale = scale;
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
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        result = prime * result + Float.floatToIntBits(price);
        result = prime * result + ((scale == null) ? 0 : scale.hashCode());
        result = prime * result + ((dipinto == null) ? 0 : dipinto.hashCode());
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
        if (Float.floatToIntBits(price) != Float.floatToIntBits(other.price))
            return false;
        if (scale == null) {
            if (other.scale != null)
                return false;
        } else if (!scale.equals(other.scale))
            return false;
        if (dipinto == null) {
            if (other.dipinto != null)
                return false;
        } else if (!dipinto.equals(other.dipinto))
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
