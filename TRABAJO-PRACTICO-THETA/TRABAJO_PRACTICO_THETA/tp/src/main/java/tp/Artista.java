package tp;

import java.util.ArrayList;
import java.util.List;

public class Artista {
    private String nombre;
    private List<Rol> roles;   // nombres de roles que puede desempeñar
    private List<String> bandas;            // bandas en las que participó
    private double costo;
    private int maxCanciones;
    private int cancionesAsignadas;         // cantidad actual de canciones asignadas

    public Artista() {}
    
    // 🔹 Constructor
    public Artista(String nombre, List<Rol> roles, List<String> bandas,
                   double costo, int maxCanciones) {
        this.nombre = nombre;
        this.roles = new ArrayList<>(roles);
        this.bandas = new ArrayList<>(bandas);
        this.costo = costo;
        this.maxCanciones = maxCanciones;
        this.cancionesAsignadas = 0;
    }

    // 🔹 Getters
    public String getNombre() {
        return nombre;
    }
    
    public List<String> getBandasHistoricas() {
    	return bandas;
    }

    public List<Rol> getRoles() {
        return roles;
    }

    public List<String> getBandas() {
        return bandas;
    }

    public double getCostoPorCancion() {
        return costo;
    }

    public int getMaxCanciones() {
        return maxCanciones;
    }

    public int getCancionesAsignadas() {
        return cancionesAsignadas;
    }
    
    public double getCostoContratacion() {
    	return cancionesAsignadas * costo;
    }
    
    public void setCosto(double costo) {
    	this.costo = costo;
    }

    // 🔹 Métodos principales

    /** Verifica si puede desempeñar el rol indicado */
    public boolean puedeDesempenar(Rol rol) {
        for (Rol r : roles) {
            if (r.getNombre().equalsIgnoreCase(rol.getNombre())) {
                return true;
            }
        }
        return false;
    }


    /** Verifica si aún tiene disponibilidad para tocar más canciones */
    public boolean disponible() {
        return cancionesAsignadas < maxCanciones;
    }

    /** Incrementa la cantidad de canciones asignadas */
    public void asignarCancion() {
        if (!disponible()) {
            throw new IllegalStateException("El artista " + nombre + " ya alcanzó su máximo de canciones permitidas.");
        }
        cancionesAsignadas++;
    }

    /** Entrena al artista para agregarle un nuevo rol (no debe ser artista base) */
    public void entrenar(Rol nuevoRol) {
        if (!puedeDesempenar(nuevoRol)) {
            roles.add(nuevoRol);
            costo *= 1.5; // aumento del 50%
        }
    }



    /** Aplica descuento del 50% si comparte alguna banda con un artista base */
    public double calcularCostoConDescuento(List<Artista> artistasBase) {
        for (Artista base : artistasBase) {
            for (String banda : bandas) {
                if (base.getBandas().contains(banda)) {
                    return costo * 0.5;
                }
            }
        }
        return costo;
    }

    @Override
    public String toString() {
        return nombre + " | Roles: " + roles +
                " | Bandas: " + bandas +
                " | Costo: $" + costo +
                " | Canciones asignadas: " + cancionesAsignadas + "/" + maxCanciones;
    }
}
