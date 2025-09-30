 document.addEventListener('DOMContentLoaded', () => {
        const pasos = document.querySelectorAll('.seccion-paso');
        const botonesSiguiente = document.querySelectorAll('.siguiente-paso');
        const botonesAnterior = document.querySelectorAll('.paso-anterior');
        const indicador = document.querySelectorAll('.paso-progreso');

        let pasoActual = 1;

        function mostrarPaso(paso) {
            pasos.forEach(sec => sec.classList.toggle('activa', Number(sec.dataset.paso) === paso));
            indicador.forEach(ind => ind.classList.toggle('activo', Number(ind.dataset.paso) <= paso));

            const wrapper = document.querySelector('.contribucion-wrapper');
            wrapper.classList.toggle('paso2-activo', paso === 2);

            const indicadorCont = document.querySelector('.indicador-progreso');
            indicadorCont.classList.toggle('paso2', paso === 2);
            indicadorCont.classList.toggle('paso3', paso === 3);
        }


        botonesSiguiente.forEach(btn => {
            btn.addEventListener('click', () => {
                if (pasoActual < pasos.length) {
                    pasoActual++;
                    mostrarPaso(pasoActual);
                }
            });
        });

        botonesAnterior.forEach(btn => {
            btn.addEventListener('click', () => {
                if (pasoActual > 1) {
                    pasoActual--;
                    mostrarPaso(pasoActual);
                }
            });
        });

        mostrarPaso(pasoActual);
    });