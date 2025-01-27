/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package simulación.cpu;

/**
 *
 * @author mello
 */
public class SimulaciónCPU {

    /**
     * @param args the command line arguments
     */
        // TODO code application logic here
        public class Main {

    public static void main(String args) {

        // 1. Cargar la configuración inicial (desde un archivo TXT, CSV o JSON).
        Configuracion configuracion = cargarConfiguracion("config.json"); // Ajusta el nombre del archivo según tu implementación.

        // 2. Crear la interfaz gráfica.
        InterfazGrafica interfazGrafica = new InterfazGrafica(configuracion);

        // 3. Inicializar las colas de listos y bloqueados.
        Cola colaListos = new Cola();
        Cola colaBloqueados = new Cola();

        // 4. Crear los procesadores.
        Procesador procesadores = new Procesador[configuracion.getNumProcesadores()];
        for (int i = 0; i < configuracion.getNumProcesadores(); i++) {
            procesadores[i] = new Procesador(i, colaListos, colaBloqueados, interfazGrafica);
        }

        // 5. Crear el planificador.
        Planificador planificador = new Planificador(colaListos, colaBloqueados, configuracion.getPoliticaInicial());

        // 6. Iniciar los hilos de los procesadores.
        for (Procesador procesador: procesadores) {
            procesador.start();
        }

        // 7. Manejar eventos de la interfaz gráfica.
        interfazGrafica.setOyente(new OyenteInterfaz() {
            @Override
            public void cambiarPolitica(String nuevaPolitica) {
                planificador.cambiarPolitica(nuevaPolitica);
            }

            @Override
            public void cambiarDuracionCiclo(int nuevaDuracion) {
                // Ajustar la duración del ciclo de los procesadores.
            }

            //... otros eventos de la interfaz gráfica...
        });

        // 8. (Opcional) Iniciar un hilo para el recolector de métricas.
        //...

    }

    //... otros métodos auxiliares (cargar configuración, etc.)...

    }
    
}
