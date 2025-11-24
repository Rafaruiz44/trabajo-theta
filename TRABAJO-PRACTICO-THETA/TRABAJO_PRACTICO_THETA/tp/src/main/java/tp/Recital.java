package tp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
//import java.util.Objects;

public class Recital {
    private List<Cancion> canciones;  // Canciones que se interpretarán
    private List<Artista> artistas;   // Artistas base y contratados

    // Constructor
    public Recital() {
        this.canciones = new ArrayList<>();
        this.artistas = new ArrayList<>();
    }

   
    public List<Cancion> getCanciones() {
        return canciones;
    }
    
    public void setCanciones(List<Cancion> canciones) {
    	this.canciones = canciones;
    }
    
    public void listarCanciones() {
    	
    	for(Cancion i : canciones) {
    		System.out.println(i.getTitulo());
    	}
    	
    }

    public List<Artista> getArtistas() {
        return artistas;
    }
    
    public boolean estaAsignado(Artista artista) {
        for (Cancion c : canciones) {
            if (c.estaAsignado(artista)) {
                return true;
            }
        }
        return false;
    }


    // Métodos de negocio
    
    public Cancion buscarCancionPorTitulo(String titulo) {
        for (Cancion c : canciones) {
            if (c.getTitulo().equalsIgnoreCase(titulo)) {
                return c;
            }
        }
        return null; // no encontrada
    }
    
    public List<Rol> obtenerRolesFaltantesPorCancion(String titulo) {
        Cancion cancion = buscarCancionPorTitulo(titulo);
        if (cancion == null) return Collections.emptyList();
        return cancion.obtenerRolesFaltantes();
    }

    
    public List<Rol> obtenerRolesFaltantesTotales() {
        List<Rol> faltantes = new ArrayList<>();

        for (Cancion cancion : canciones) {
            for (Rol requerido : cancion.getRolesRequeridos()) {

                boolean cubierto = false;

                // Verificar si algún artista del recital puede desempeñar ese rol
                for (Artista artista : artistas) {
                    if (artista.puedeDesempenar(requerido)) {
                        cubierto = true;
                        break;
                    }
                }

                // Si el rol no está cubierto y aún no lo agregamos → agregarlo
                if (!cubierto && !faltantes.contains(requerido)) {
                    faltantes.add(requerido);
                }
            }
        }
        return faltantes;
    }
    
    
    
    public void contratarArtistasParaCancion(String titulo,Discografica discografica) {
    	
    	Cancion cancion = buscarCancionPorTitulo(titulo);
    	
        if (cancion == null) {
            System.out.println("❌ La canción no existe.");
            return;
        }

        List<Rol> faltantes = cancion.obtenerRolesFaltantes();

        if (faltantes.isEmpty()) {
            System.out.println("✔ La canción ya tiene todos los roles cubiertos.");
            return;
        }

        System.out.println("🔎 Contratando artistas para: " + cancion.getTitulo());

        // Inicializar lista si está null
        if (cancion.getArtistasAsignados() == null) {
            cancion.setArtistasAsignados(new ArrayList<>());
        }

        List<Artista> asignados = cancion.getArtistasAsignados();

        // ===== PRIMERA PASADA: ARTISTAS BASE =====
        for (Rol rol : faltantes) {

            Artista elegido = discografica.buscarArtistaBaseQuePueda(rol, asignados);

            if (elegido != null) {
                asignados.add(elegido);
                System.out.println(" → Asignado (BASE) " + elegido.getNombre() + " para rol " + rol.getNombre());
                elegido.asignarCancion();
            }
        }

        // Recalcular faltantes
        faltantes = cancion.obtenerRolesFaltantes();

        // ===== SEGUNDA PASADA: ARTISTAS EXTERNOS =====
        for (Rol rol : faltantes) {

            Artista elegido = discografica.buscarArtistaExternoMasBarato(rol, asignados);

            if (elegido != null) {

                double costo = elegido.getCostoPorCancion();

                // Si comparte banda con base ⇒ mitad de precio
                if (compartenBandaConBase(elegido, discografica.getArtistasDelSello())) {
                    costo /= 2;
                }
                
                elegido.setCosto(costo);

                asignados.add(elegido);
                this.artistas.add(elegido);
                System.out.println(" → Contratado (EXTERNO) " + elegido.getNombre() +
                                   " para " + rol.getNombre() +
                                   " | costo: " + costo);
            }else {

                System.out.println(" ❌ No hay artista disponible que pueda cubrir el rol: "
                                   + rol.getNombre());
            }
            
        }

        System.out.println("✔ Contratación finalizada para: " + cancion.getTitulo());
    }
    
    public void contratarArtistasParaTodasLasCanciones(Discografica discografica) {

        for (Cancion c : canciones) {

            // Si ya tiene artistas contratados, NO hacer nada
            if (c.tieneContrataciones()) {
                System.out.println("✔ La canción '" + c.getTitulo() + "' ya tiene artistas contratados. Se omite.");
                continue;
            }

            System.out.println("\n=== Contratando artistas para '" + c.getTitulo() + "' ===");
            contratarArtistasParaCancion(c.getTitulo(), discografica);
        }

        System.out.println("\n✔ Contratación global finalizada.");
    }



    private boolean compartenBandaConBase(Artista externo, List<Artista> asignados) {
        for (Artista a : asignados) {
            for (String banda : externo.getBandas()) {
                if (a.getBandas().contains(banda)) {
                    return true;
                }
            }
        }
        return false;
    }

    
    public Artista buscarArtistaBaseQuePueda(Rol rol, List<Artista> asignados) {
        for (Artista a : artistas) {
            if (!asignados.contains(a) &&
                a.puedeDesempenar(rol)) {
                return a;
            }
        }
        return null;
    }




    public void agregarCancion(Cancion cancion) {
        if (!canciones.contains(cancion)) {
            canciones.add(cancion);
        }
    }

    public void agregarArtista(Artista artista) {
        if (!artistas.contains(artista)) {
            artistas.add(artista);
        }
    }

    public void eliminarArtista(Artista artista) {
        artistas.remove(artista);
    }

    public void mostrarInformacion() {
        System.out.println(" Canciones:");
        for (Cancion c : canciones) {
            System.out.println("   - " + c.getTitulo());
        }
        System.out.println(" Artistas participantes:");
        for (Artista a : artistas) {
            System.out.println("   - " + a.getNombre());
        }
    }

    @Override
    public String toString() {
        return "Recital{" +
                ", canciones=" + canciones.size() +
                ", artistas=" + artistas.size() +
                '}';
    }
}
