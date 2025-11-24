package tp;

import java.util.List;
import java.util.Scanner;

public class MenuRecital {
    private Discografica discografica;
    private Recital recital;
    private GestorArchivo gestorArchivo;
    private Scanner scanner;
    
    public MenuRecital() {
        this.gestorArchivo = new GestorArchivo();
        this.scanner = new Scanner(System.in);
    }

    public void iniciar() {
        System.out.println("=== Sistema de Gestión de Recital ===");

        cargarDatosIniciales();

        int opcion;
        do {
            mostrarMenu();
            opcion = leerEntero("Ingrese una opción: ");
            ejecutarOpcion(opcion);
        } while (opcion != 8);
        
        System.out.println(recital.getArtistas());
        System.out.println(" Saliendo del sistema...");
    }

    private void cargarDatosIniciales() {
    	// 1) Leer artistas desde archivo
        List<Artista> artistas = gestorArchivo.cargarArtistas("artistas.json");

        // 2) Crear discografica separando base y externos
        discografica = new Discografica("Mi sello");
        recital = new Recital();

        for (Artista a : artistas) {
            if (a.getCostoPorCancion() == 0) {
                discografica.agregarArtistaDelSello(a);
                recital.agregarArtista(a);
            } else {
                discografica.agregarArtistaExterno(a);
            }
        }

        // 3) Leer canciones desde archivo
        List<Cancion> canciones = gestorArchivo.cargarCanciones("recital.json");

        // 4) Crear Recital
        recital = new Recital();
        recital.setCanciones(canciones);

        System.out.println("✔ Datos iniciales cargados correctamente.");

        System.out.println(" Datos cargados correctamente.\n");
    }

    private void mostrarMenu() {
        System.out.println("\n===== MENÚ PRINCIPAL =====");
        System.out.println("1. Listar artistas del sello");
        System.out.println("2. Listar artistas externos");
        System.out.println("3. Listar canciones del recital");
        System.out.println("4. Mostrar roles faltantes de una canción");
        System.out.println("5. Asignar y contratar artistas para canción");
        System.out.println("6. Asignar y contratar artistas para todas las canciones");
        System.out.println("7. Entrenar artista");
        System.out.println("8. Salir");
    }

    private void ejecutarOpcion(int opcion) {
        switch (opcion) {
            case 1 -> {
                discografica.listarArtistasDelSello();
                pausarParaContinuar();
            }
            case 2 -> {
                discografica.listarArtistasExternos();
                pausarParaContinuar();
            }
            case 3 -> {
                recital.listarCanciones();
                pausarParaContinuar();
            }
            case 4 -> {
                verRolesFaltantes();
                pausarParaContinuar();
            }
            case 5 -> {
                contratarArtistaParaCancion();
                pausarParaContinuar();
            }
            case 6 -> {
                contratarTodas();
                pausarParaContinuar();
            }
            case 7 -> {
                entrenarArtista();
                pausarParaContinuar();
            }
            case 8 -> System.out.println("Saliendo del sistema...");
            default -> {
                System.out.println("Opción inválida.");
                pausarParaContinuar();
            }
        }
    }
    
    private void pausarParaContinuar() {
        System.out.println("\n--- Presione ENTER para volver al menú ---");
        scanner.nextLine();
    }

    private void verRolesFaltantes() {
        System.out.print("Canción: ");
        String titulo = scanner.nextLine();

        List<Rol> faltan = recital.obtenerRolesFaltantesPorCancion(titulo);

        if (faltan == null) {
            System.out.println("❌ La canción no existe.");
            return;
        }

        if (faltan.isEmpty()) {
            System.out.println("✔ No faltan roles.");
        } else {
            System.out.println("Roles faltantes:");
            for (Rol r : faltan) {
                System.out.println(" - " + r.getNombre());
            }
        }
    }
    /*
    private void contratarParaCancion() {
        System.out.print("Canción: ");
        String titulo = sc.nextLine();

        recital.contratarArtistasParaCancion(titulo);

        System.out.println("✔ Contratación automática hecha.");
    }
	*/
    
    private void contratarArtistaParaCancion() {
        System.out.print("Ingrese título de la canción: ");
        String titulo = scanner.nextLine();

        recital.contratarArtistasParaCancion(titulo,discografica);
    }
    
    private void contratarTodas() {
        recital.contratarArtistasParaTodasLasCanciones(discografica);
    }


    private void entrenarArtista() {
        System.out.print("Ingrese nombre del artista a entrenar: ");
        String nombre = scanner.nextLine();

        // Buscar artista externo (los unicos entrenables)
        Artista artista = discografica.buscarArtistaExternoPorNombre(nombre);

        if (artista == null) {
            System.out.println("❌ El artista no existe o es del sello (no se puede entrenar).");
            return;
        }

        // Verificar si el artista ya fue contratado para alguna cancion
        if (recital.estaAsignado(artista)) {
            System.out.println("❌ No se puede entrenar un artista que ya está contratado.");
            return;
        }

        System.out.print("Ingrese nuevo rol a agregar: ");
        String nombreRolNuevo = scanner.nextLine();
        Rol rolNuevo = new Rol(nombreRolNuevo);

        if (artista.puedeDesempenar(rolNuevo)) {
            System.out.println("⚠ El artista ya puede desempeñar ese rol.");
            return;
        }

        // Entrenar
        artista.entrenar(rolNuevo);

        System.out.println("✔ " + artista.getNombre() +
                " ahora puede desempeñar el rol '" + rolNuevo + "' (costo aumentado).");
    }


    private int leerEntero(String msg) {
        while (true) {
            try {
                System.out.print(msg);
                return Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("⚠ Ingrese un número válido.");
            }
        }
    }

}
