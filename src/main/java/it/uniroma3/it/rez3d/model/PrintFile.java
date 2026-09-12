package it.uniroma3.it.rez3d.model;

import java.util.List;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
public class PrintFile {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank(message = "Il nome del modello è obbligatorio")
    @Size(max = 100, message = "Il nome non può superare 100 caratteri")
    private String name;

    @NotBlank(message = "Il nome dell'artista è obbligatorio")
    private String artist;

    @NotNull(message = "Il prezzo è obbligatorio")
    @DecimalMin(value = "0.0", message = "Il prezzo non può essere negativo")
    private float price;

    @NotBlank(message = "La descrizione è obbligatoria")
    @Size(max = 2000, message = "La descrizione non può superare 2000 caratteri")
    private String description;

    @NotBlank(message = "La categoria è obbligatoria")
    private String category;

    private String image;

    private String stlPath;

    @OneToMany(mappedBy = "file")
    private List<RealProduct> prodotti;
    
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
    public String getArtist() {
        return artist;
    }
    public void setArtist(String artist) {
        this.artist = artist;
    }
    public float getPrice() {
        return price;
    }
    public void setPrice(float price) {
        this.price = price;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getCategory() {
        return category;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getStlPath() {
        return stlPath;
    }
    public void setStlPath(String stlPath) {
        this.stlPath = stlPath;
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
        PrintFile other = (PrintFile) obj;
        if (id == null || other.id == null)
            return false;
        return id.equals(other.id);
    }

    

}
