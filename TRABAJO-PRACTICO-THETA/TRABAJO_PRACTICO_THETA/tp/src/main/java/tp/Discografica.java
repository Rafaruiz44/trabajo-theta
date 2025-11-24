package tp;

import java.util.ArrayList;
import java.util.List;

public class Discografica {
    private String nombre;
    private List<Artista> artistasDelSello;
    private List<Artista> artistasExternos;

    // Constructor
    public Discografica(String nombre) {
        this.nombre = nombre;
        this.artistasDelSello = new ArrayList<>();
        this.artistasExternos = new ArrayList<>();
    }

    // Getters
    public String getNombre() {
        return nombre;
    }

    public List<Artista> getArtistasDelSello() {
        return artistasDelSello;
    }

    public List<Artista> getArtistasExternos() {
        return artistasExternos;
    }

    // Métodos principales
    public void agregarArtistaDelSello(Artista artista) {
        artistasDelSello.add(artista);
    }

    public void agregarArtistaExterno(Artista artista) {
        artistasExternos.add(artista);
    }

    public boolean esArtistaDelSello(Artista artista) {
        return artistasDelSello.contains(artista);
    }
    
    public Artista buscarArtistaBaseQuePueda(Rol rol, List<Artista> yaAsignados) {
        for (Artista a : artistasDelSello) {
            if (!yaAsignados.contains(a) && a.puedeDesempenar(rol)) {
                return a;
            }
        }
        return null;
    }
    
    public Artista buscarArtistaExternoPorNombre(String nombre) {
        for (Artista a : artistasExternos) {
            if (a.getNombre().equalsIgnoreCase(nombre)) {
                return a;
            }
        }
        return null;
    }

    
    public Artista buscarArtistaExternoMasBarato(Rol rol, List<Artista> asignados) {
        Artista mejor = null;

        for (Artista a : artistasExternos) {

            if (asignados.contains(a)) continue;
            if (!a.puedeDesempenar(rol)) continue;

            if (mejor == null || a.getCostoPorCancion() < mejor.getCostoPorCancion()) {
                mejor = a;
            }
        }
        return mejor;
    }
    


    /**
     * Aplica el descuento del 50% si el artista externo comparte una banda
     * con algún artista base (del sello).
     */
    public double calcularCostoConDescuento(Artista candidato) {
        if (compartenBandaConBase(candidato)) {
            return candidato.getCostoContratacion() * 0.5;
        }
        return candidato.getCostoContratacion();
    }

    private boolean compartenBandaConBase(Artista candidato) {
        for (Artista base : artistasDelSello) {
            for (String bandaBase : base.getBandasHistoricas()) {
                if (candidato.getBandasHistoricas().contains(bandaBase)) {
                    return true;
                }
            }
        }
        return false;
    }


    public void listarArtistasDelSello() {
        System.out.println(" Artistas del sello " + nombre + ":");
        for (Artista a : artistasDelSello) {
            System.out.println("- " + a);
        }
    }

    public void listarArtistasExternos() {
        System.out.println(" Artistas externos registrados:");
        for (Artista a : artistasExternos) {
            System.out.println("- " + a);
        }
    }
}
