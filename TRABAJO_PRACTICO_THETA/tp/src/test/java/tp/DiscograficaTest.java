package tp;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class DiscograficaTest {

    private Discografica discografica;
    private Artista brian;
    private Artista roger;
    private Artista externo1;
    private Artista externo2;

    private Rol guitarra;
    private Rol bateria;
    private Rol vozPrincipal;

    @BeforeEach
    public void setUp() {
        discografica = new Discografica("Universal Music");

        guitarra = new Rol("guitarra eléctrica");
        bateria = new Rol("batería");
        vozPrincipal = new Rol("voz principal");

        brian = new Artista("Brian May", List.of(guitarra), List.of("Queen"), 0, 100);
        roger = new Artista("Roger Taylor", List.of(bateria), List.of("Queen"), 0, 100);

        externo1 = new Artista("George Michael", List.of(vozPrincipal), List.of("Wham!"), 1000, 3);
        externo2 = new Artista("Elton John", List.of(vozPrincipal, guitarra), List.of("Elton John Band"), 1200, 2);

        discografica.agregarArtistaDelSello(brian);
        discografica.agregarArtistaDelSello(roger);

        discografica.agregarArtistaExterno(externo1);
        discografica.agregarArtistaExterno(externo2);
    }

    // ----------------------------------------------------------
    // TEST 1: agregar y verificar artistas del sello
    // ----------------------------------------------------------
    @Test
    public void testAgregarArtistaDelSello() {
        assertTrue(discografica.esArtistaDelSello(brian));
        assertTrue(discografica.esArtistaDelSello(roger));
    }

    // ----------------------------------------------------------
    // TEST 2: buscar artista base por rol
    // ----------------------------------------------------------
    @Test
    public void testBuscarArtistaBaseQuePueda() {
        List<Artista> yaAsignados = new ArrayList<>();

        Artista encontrado1 = discografica.buscarArtistaBaseQuePueda(guitarra, yaAsignados);
        Artista encontrado2 = discografica.buscarArtistaBaseQuePueda(bateria, yaAsignados);

        assertEquals(brian, encontrado1);
        assertEquals(roger, encontrado2);
    }

    @Test
    public void testBuscarArtistaBaseSinDisponibles() {
        List<Artista> asignados = new ArrayList<>();
        asignados.add(brian);

        Artista res = discografica.buscarArtistaBaseQuePueda(guitarra, asignados);

        assertNull(res); // Brian está ocupado → no debería devolverlo
    }

    // ----------------------------------------------------------
    // TEST 3: buscar artista externo por nombre
    // ----------------------------------------------------------
    @Test
    public void testBuscarArtistaExternoPorNombre() {
        Artista encontrado = discografica.buscarArtistaExternoPorNombre("George Michael");
        assertEquals(externo1, encontrado);
    }

    @Test
    public void testBuscarArtistaExternoPorNombreInexistente() {
        assertNull(discografica.buscarArtistaExternoPorNombre("Shakira"));
    }

    // ----------------------------------------------------------
    // TEST 4: buscar externo más barato
    // ----------------------------------------------------------
    @Test
    public void testBuscarArtistaExternoMasBarato() {
        List<Artista> asignados = new ArrayList<>();

        Artista elegido = discografica.buscarArtistaExternoMasBarato(vozPrincipal, asignados);

        assertEquals(externo1, elegido); // George Michael cuesta 1000, Elton 1200
    }

    @Test
    public void testBuscarExternoMasBaratoSiEstaAsignado() {
        List<Artista> asignados = new ArrayList<>();
        asignados.add(externo1); // George ya está ocupado

        Artista elegido = discografica.buscarArtistaExternoMasBarato(vozPrincipal, asignados);

        assertEquals(externo2, elegido); // Debería quedar Elton John
    }

    @Test
    public void testBuscarExternoMasBaratoPeroNadiePuede() {
        Rol piano = new Rol("piano"); // Ninguno puede piano

        Artista elegido = discografica.buscarArtistaExternoMasBarato(piano, new ArrayList<>());

        assertNull(elegido);
    }

    // ----------------------------------------------------------
    // TEST 5: descuento por banda compartida con base
    // ----------------------------------------------------------
    @Test
    public void testCalcularCostoConDescuentoCuandoHayBandaCompartida() {
        // Creamos un candidato que comparte "Queen"
        Artista invitado = new Artista("Invitado", List.of(guitarra), List.of("Queen"), 500, 5);
        invitado.asignarCancion(); // costoContratación = 500

        double costo = discografica.calcularCostoConDescuento(invitado);

        assertEquals(250, costo); // 50% de descuento
    }

    @Test
    public void testCalcularCostoConDescuentoSinBandaCompartida() {
        externo1.asignarCancion(); // costo = 1000

        double costo = discografica.calcularCostoConDescuento(externo1);

        assertEquals(1000, costo); // sin descuento
    }
}
