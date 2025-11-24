package tp;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cancion {
    private String titulo;
    private List<Rol> rolesRequeridos;
    private List<Artista> artistasAsignados;

    // Constructor
    public Cancion(String titulo,List<Rol> rolesRequeridos) {
        this.titulo = titulo;
        this.rolesRequeridos = new ArrayList<>(rolesRequeridos);
        this.artistasAsignados = new ArrayList<>();
    }

    // Getters y Setters
    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public List<Rol> getRolRequerido() {
        return rolesRequeridos;
    }

    public void setRolRequerido(List<Rol> rolRequerido) {
        this.rolesRequeridos = rolRequerido;
    }

    public List<Rol> getRolesRequeridos() {
        return rolesRequeridos;
    }

    public List<Artista> getArtistasAsignados() {
        return artistasAsignados;
    }
    
    public void setArtistasAsignados(List<Artista> lista) {
        this.artistasAsignados = lista;
    }

    public void asignarArtista(Artista artista) {
    	if (artistasAsignados != null) {
            artistasAsignados.add(artista);
        }
    }
    
    public boolean estaAsignado(Artista a) {
        if (artistasAsignados == null) {
            return false;
        }
        return artistasAsignados.contains(a);
    }
    
    public boolean tieneContrataciones() {
        return artistasAsignados != null && !artistasAsignados.isEmpty();
    }


    public List<Rol> obtenerRolesFaltantes() {
        List<Rol> faltantes = new ArrayList<>();

        if (artistasAsignados == null || artistasAsignados.isEmpty()) {
            faltantes.addAll(rolesRequeridos);
            return faltantes;
        }

        List<Artista> disponibles = new ArrayList<>(artistasAsignados);

        for (Rol rolReq : rolesRequeridos) {
            boolean cubierto = false;

            for (Artista artista : disponibles) {
                if (artista.puedeDesempenar(rolReq)) {
                    cubierto = true;
                    disponibles.remove(artista); // ya está usado para un rol
                    break;
                }
            }

            if (!cubierto) {
                faltantes.add(rolReq);
            }
        }
        return faltantes;
    }


    
    


    public void mostrarInformacion() {
        System.out.println(" Canción: " + titulo +
                " | Rol requerido: " + rolesRequeridos);
    }

    // equals y hashCode para poder comparar canciones por título
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Cancion)) return false;
        Cancion cancion = (Cancion) o;
        return Objects.equals(titulo, cancion.titulo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(titulo);
    }

    @Override
    public String toString() {
        return "Cancion{" +
                "titulo='" + titulo + '\'' +
                ", rolRequerido='" + rolesRequeridos + '\'' +
                '}';
    }
}
