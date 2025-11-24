package tp;

public class Rol {
	private String nombre;
	
	public Rol() {}
	
	public Rol(String nombre) {
		this.nombre = nombre;
	}
	
	public String getNombre() {
		return nombre;
	}
	
	@Override
    public String toString() {
        return nombre;
    }
	
}
