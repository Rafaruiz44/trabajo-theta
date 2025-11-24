package tp;

import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

public class GestorArchivo {

    private Gson gson;

    public GestorArchivo() {
        this.gson = new Gson();
    }

    /**
     * Lee una lista de artistas desde un archivo JSON.
     */
    public List<Artista> cargarArtistas(String rutaArchivo) {
        try (FileReader reader = new FileReader(rutaArchivo)) {
            Type tipoLista = new TypeToken<ArrayList<Artista>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        } catch (IOException e) {
            System.err.println("Error al leer archivo de artistas: " + e.getMessage());
            return new ArrayList<>();
        }
    }

    /**
     * Lee una lista de canciones desde archivo JSON.
     */
    public List<Cancion> cargarCanciones(String rutaArchivo) {
        try (FileReader reader = new FileReader(rutaArchivo)) {
            Type tipoLista = new TypeToken<ArrayList<Cancion>>() {}.getType();
            return gson.fromJson(reader, tipoLista);
        } catch (IOException e) {
            System.err.println("Error al leer archivo de canciones: " + e.getMessage());
            return new ArrayList<>();
        }
    }
}
