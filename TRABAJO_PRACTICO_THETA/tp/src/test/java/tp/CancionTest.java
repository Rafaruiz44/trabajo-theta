package tp;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

public class CancionTest {

    private Rol voz;
    private Rol guitarra;
    private Rol bajo;

    private Artista aVoz;
    private Artista aGuitarra;
    private Artista aBajo;

    private Cancion cancion;

    @BeforeEach
    void setUp() {
        voz = new Rol("voz principal");
        guitarra = new Rol("guitarra eléctrica");
        bajo = new Rol("bajo");

        aVoz = new Artista("Cantante",
                List.of(voz), List.of(), 100, 5);

        aGuitarra = new Artista("Guitarrista",
                List.of(guitarra), List.of(), 100, 5);

        aBajo = new Artista("Bajista",
                List.of(bajo), List.of(), 100, 5);

        cancion = new Cancion(
                "Test Song",
                List.of(voz, guitarra, bajo)
        );
    }

    // -----------------------------------------------------
    // 1) Constructor y getters
    // -----------------------------------------------------
    @Test
    void testConstructorYGetters() {
        assertEquals("Test Song", cancion.getTitulo());
        assertEquals(3, cancion.getRolesRequeridos().size());
        assertTrue(cancion.getArtistasAsignados().isEmpty());
    }

    // -----------------------------------------------------
    // 2) asignarArtista()
    // -----------------------------------------------------
    @Test
    void testAsignarArtista() {
        cancion.asignarArtista(aVoz);
        assertEquals(1, cancion.getArtistasAsignados().size());
        assertTrue(cancion.estaAsignado(aVoz));
    }

    // -----------------------------------------------------
    // 3) estaAsignado()
    // -----------------------------------------------------
    @Test
    void testEstaAsignado() {
        assertFalse(cancion.estaAsignado(aVoz));
        cancion.asignarArtista(aVoz);
        assertTrue(cancion.estaAsignado(aVoz));
    }

    // -----------------------------------------------------
    // 4) tieneContrataciones()
    // -----------------------------------------------------
    @Test
    void testTieneContrataciones() {
        assertFalse(cancion.tieneContrataciones());
        cancion.asignarArtista(aVoz);
        assertTrue(cancion.tieneContrataciones());
    }

    // -----------------------------------------------------
    // 5) obtenerRolesFaltantes() - sin artistas asignados
    // -----------------------------------------------------
    @Test
    void testObtenerRolesFaltantesSinArtistas() {
        List<Rol> faltantes = cancion.obtenerRolesFaltantes();
        assertEquals(3, faltantes.size());
        assertTrue(faltantes.contains(voz));
        assertTrue(faltantes.contains(guitarra));
        assertTrue(faltantes.contains(bajo));
    }

    // -----------------------------------------------------
    // 6) obtenerRolesFaltantes() - artistas cubren todos los roles
    // -----------------------------------------------------
    @Test
    void testObtenerRolesFaltantesCompleto() {
        cancion.asignarArtista(aVoz);
        cancion.asignarArtista(aGuitarra);
        cancion.asignarArtista(aBajo);

        List<Rol> faltantes = cancion.obtenerRolesFaltantes();
        assertTrue(faltantes.isEmpty());
    }

    // -----------------------------------------------------
    // 7) obtenerRolesFaltantes() - caso parcial
    // -----------------------------------------------------
    @Test
    void testObtenerRolesFaltantesParcial() {
        // Solo asigno guitarrista
        cancion.asignarArtista(aGuitarra);

        List<Rol> faltantes = cancion.obtenerRolesFaltantes();
        assertEquals(2, faltantes.size());
        assertTrue(faltantes.contains(voz));
        assertTrue(faltantes.contains(bajo));
    }

    // -----------------------------------------------------
    // 8) setArtistasAsignados()
    // -----------------------------------------------------
    @Test
    void testSetArtistasAsignados() {
        List<Artista> lista = new ArrayList<>(List.of(aVoz, aGuitarra));
        cancion.setArtistasAsignados(lista);

        assertEquals(2, cancion.getArtistasAsignados().size());
        assertTrue(cancion.estaAsignado(aVoz));
    }

    // -----------------------------------------------------
    // 9) setRolRequerido()
    // -----------------------------------------------------
    @Test
    void testSetRolesRequeridos() {
        cancion.setRolRequerido(List.of(voz));
        assertEquals(1, cancion.getRolesRequeridos().size());
    }
}

