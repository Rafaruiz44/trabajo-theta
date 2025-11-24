package tp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class ArtistaTest {

    private Rol voz;
    private Rol guitarra;
    private Rol bateria;

    private Artista artista;

    @BeforeEach
    void setUp() {
        voz = new Rol("Voz Principal");
        guitarra = new Rol("Guitarra");
        bateria = new Rol("Batería");

        artista = new Artista(
                "Freddie",
                Arrays.asList(voz, guitarra),
                Arrays.asList("Queen"),
                1000.0,
                3
        );
    }

    // ------------------------------------------------------------
    // TEST 1: puedeDesempenar
    // ------------------------------------------------------------

    @Test
    void testPuedeDesempenar_RolExistente() {
        assertTrue(artista.puedeDesempenar(new Rol("Voz Principal")));
    }

    @Test
    void testPuedeDesempenar_RolInexistente() {
        assertFalse(artista.puedeDesempenar(bateria));
    }

    // ------------------------------------------------------------
    // TEST 2: disponibilidad
    // ------------------------------------------------------------

    @Test
    void testDisponible_CuandoNoAlcanzoElMaximo() {
        assertTrue(artista.disponible());
    }

    @Test
    void testDisponible_CuandoLlegaAlMaximo() {
        artista.asignarCancion();
        artista.asignarCancion();
        artista.asignarCancion();
        assertFalse(artista.disponible());
    }

    // ------------------------------------------------------------
    // TEST 3: asignarCancion
    // ------------------------------------------------------------

    @Test
    void testAsignarCancion_IncrementaCorrectamente() {
        artista.asignarCancion();
        assertEquals(1, artista.getCancionesAsignadas());
    }

    @Test
    void testAsignarCancion_SuperaMaximo_LanzaExcepcion() {
        artista.asignarCancion();
        artista.asignarCancion();
        artista.asignarCancion();

        assertThrows(IllegalStateException.class, () -> artista.asignarCancion());
    }

    // ------------------------------------------------------------
    // TEST 4: entrenar
    // ------------------------------------------------------------

    @Test
    void testEntrenar_AgregaRolNuevo() {
        artista.entrenar(bateria);

        assertTrue(artista.puedeDesempenar(bateria));
    }

    @Test
    void testEntrenar_NoDuplicaRolExistente() {
        artista.entrenar(voz);
        long countVoz = artista.getRoles()
                .stream()
                .filter(r -> r.getNombre().equalsIgnoreCase("Voz Principal"))
                .count();

        assertEquals(1, countVoz);
    }

    @Test
    void testEntrenar_AumentaCosto50Porciento() {
        double costoInicial = artista.getCostoPorCancion();

        artista.entrenar(new Rol("Saxo"));

        assertEquals(costoInicial * 1.5, artista.getCostoPorCancion());
    }

    // ------------------------------------------------------------
    // TEST 5: costo con descuento
    // ------------------------------------------------------------

    @Test
    void testCalcularCostoConDescuento_CuandoComparteBanda() {
        Artista base = new Artista(
                "Brian",
                Arrays.asList(guitarra),
                Arrays.asList("Queen"),
                500,
                3
        );

        double costo = artista.calcularCostoConDescuento(List.of(base));

        assertEquals(1000 * 0.5, costo);
    }

    @Test
    void testCalcularCostoConDescuento_SinBandaEnComun() {
        Artista base = new Artista(
                "Otro",
                Arrays.asList(bateria),
                Arrays.asList("The Beatles"),
                500,
                3
        );

        double costo = artista.calcularCostoConDescuento(List.of(base));

        assertEquals(1000, costo);
    }

    // ------------------------------------------------------------
    // TEST 6: getCostoContratacion
    // ------------------------------------------------------------

    @Test
    void testGetCostoContratacion() {
        artista.asignarCancion();
        artista.asignarCancion();

        assertEquals(2 * 1000.0, artista.getCostoContratacion());
    }
}
