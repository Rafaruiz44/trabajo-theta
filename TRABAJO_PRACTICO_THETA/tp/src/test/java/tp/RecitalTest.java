package tp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class RecitalTest {

    private Recital recital;
    private Discografica discografica;

    private Rol voz = new Rol("voz principal");
    private Rol guitarra = new Rol("guitarra eléctrica");
    private Rol bajo = new Rol("bajo");

    private Artista baseVoz;
    private Artista baseGuitarra;
    private Artista externoBarato;
    private Artista externoCaro;

    @BeforeEach
    void setup() {

        recital = new Recital();
        discografica = new Discografica("Universal");

        // Artistas Base
        baseVoz = new Artista("Juan",
                List.of(voz),
                List.of("Queen"),
                0, 10);

        baseGuitarra = new Artista("Pedro",
                List.of(guitarra),
                List.of("Beatles"),
                0, 10);

        discografica.agregarArtistaDelSello(baseVoz);
        discografica.agregarArtistaDelSello(baseGuitarra);

        // Artistas externos
        externoBarato = new Artista("Luis",
                List.of(bajo),
                List.of("Queen"),   // comparte banda → descuento
                1000, 5);

        externoCaro = new Artista("Carlos",
                List.of(bajo),
                List.of("Algo"),
                2000, 5);

        discografica.agregarArtistaExterno(externoBarato);
        discografica.agregarArtistaExterno(externoCaro);
    }

    // ============================================================
    // TEST 1: agregar canciones y buscarlas
    // ============================================================
    @Test
    void testAgregarYBuscarCancion() {
        Cancion c = new Cancion("Song1", List.of(voz, guitarra));
        recital.agregarCancion(c);

        assertEquals(1, recital.getCanciones().size());
        assertNotNull(recital.buscarCancionPorTitulo("song1"));
        assertNull(recital.buscarCancionPorTitulo("no existe"));
    }

    // ============================================================
    // TEST 2: roles faltantes en una canción sin artistas
    // ============================================================
    @Test
    void testRolesFaltantesPorCancion() {
        Cancion c = new Cancion("TemaX", List.of(voz, guitarra));
        recital.agregarCancion(c);

        List<Rol> faltantes = recital.obtenerRolesFaltantesPorCancion("TemaX");

        assertEquals(2, faltantes.size());
        assertTrue(faltantes.contains(voz));
        assertTrue(faltantes.contains(guitarra));
    }

    // ============================================================
    // TEST 3: contratación de artistas base
    // ============================================================
    @Test
    void testContratarArtistasBase() {
        Cancion c = new Cancion("TemaBase", List.of(voz, guitarra));
        recital.agregarCancion(c);

        recital.contratarArtistasParaCancion("TemaBase", discografica);

        assertEquals(2, c.getArtistasAsignados().size());
        assertTrue(c.estaAsignado(baseVoz));
        assertTrue(c.estaAsignado(baseGuitarra));
    }

    // ============================================================
    // TEST 4: contratación de externo más barato
    // ============================================================
    @Test
    void testContratarExternoMasBarato() {
        Cancion c = new Cancion("TemaExterno", List.of(bajo));
        recital.agregarCancion(c);

        recital.contratarArtistasParaCancion("TemaExterno", discografica);

        Artista asignado = c.getArtistasAsignados().get(0);

        // Debe elegir externoBarato porque externoCaro es más caro
        assertEquals("Luis", asignado.getNombre());
    }

    // ============================================================
    // TEST 5: descuento por compartir banda
    // ============================================================
    @Test
    void testDescuentoPorCompartirBanda() {
        Cancion c = new Cancion("TemaDescuento", List.of(bajo));
        recital.agregarCancion(c);

        recital.contratarArtistasParaCancion("TemaDescuento", discografica);

        Artista asignado = c.getArtistasAsignados().get(0);

        // Como Luis comparte banda con Juan (Queen) → mitad del costo
        assertEquals(500, asignado.getCostoPorCancion(), 0.01);
    }

    // ============================================================
    // TEST 6: contratación global
    // ============================================================
    @Test
    void testContratarParaTodasLasCanciones() {
        Cancion c1 = new Cancion("C1", List.of(voz));
        Cancion c2 = new Cancion("C2", List.of(bajo));

        recital.agregarCancion(c1);
        recital.agregarCancion(c2);

        recital.contratarArtistasParaTodasLasCanciones(discografica);

        assertEquals(1, c1.getArtistasAsignados().size());
        assertEquals(1, c2.getArtistasAsignados().size());
    }

    // ============================================================
    // TEST 7: No debe reasignar si ya tenía contratados
    // ============================================================
    @Test
    void testNoRecontratarCancionYaAsignada() {
        Cancion c = new Cancion("Song", List.of(voz));
        recital.agregarCancion(c);

        // Primera contratación
        recital.contratarArtistasParaCancion("Song", discografica);
        int antes = c.getArtistasAsignados().size();

        // Segunda contratación global (debe omitir la canción)
        recital.contratarArtistasParaTodasLasCanciones(discografica);

        int despues = c.getArtistasAsignados().size();

        assertEquals(antes, despues);
    }

}
