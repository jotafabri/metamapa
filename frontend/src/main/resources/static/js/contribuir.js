document.addEventListener('DOMContentLoaded', () => {
    const pasos = document.querySelectorAll('.seccion-paso');
    const botonesSiguiente = document.querySelectorAll('.siguiente-paso');
    const botonesAnterior = document.querySelectorAll('.paso-anterior');
    const indicador = document.querySelectorAll('.paso-progreso');

    let pasoActual = 1;
    let mapa;
    let marcador;

    function validarPasoActual() {
        const pasoDiv = document.querySelector(`.seccion-paso[data-paso="${pasoActual}"]`);
        const inputsRequeridos = pasoDiv.querySelectorAll('input[required], select[required], textarea[required]');

        const todosValidos = [...inputsRequeridos].every(input => input.checkValidity());

        if (pasoActual === 2) {
            const lat = document.getElementById('latitud');
            const lng = document.getElementById('longitud');
            return todosValidos && lat.value.trim() !== '' && lng.value.trim() !== '';
        }

        return todosValidos;
    }

    function actualizarBotonSiguiente() {
        const botonActual = document.querySelector(`.seccion-paso[data-paso="${pasoActual}"] .siguiente-paso`);
        if (botonActual) {
            botonActual.disabled = !validarPasoActual();
        }
    }

    function mostrarPaso(paso) {
        pasos.forEach(sec => sec.classList.toggle('activa', Number(sec.dataset.paso) === paso));
        indicador.forEach(ind => ind.classList.toggle('activo', Number(ind.dataset.paso) <= paso));

        const wrapper = document.querySelector('.contribucion-wrapper');
        wrapper.classList.toggle('paso2-activo', paso === 2);

        const indicadorCont = document.querySelector('.indicador-progreso');
        indicadorCont.classList.toggle('paso2', paso === 2);
        indicadorCont.classList.toggle('paso3', paso === 3);

        if (paso === 2) {
            if (!mapa) {
                inicializarMapa();
            } else {
                setTimeout(() => {
                    mapa.invalidateSize();
                }, 100);
            }
        }

        actualizarBotonSiguiente();
    }

    pasos.forEach(pasoDiv => {
        pasoDiv.addEventListener('input', actualizarBotonSiguiente);
        pasoDiv.addEventListener('change', actualizarBotonSiguiente);
    });

    botonesSiguiente.forEach(btn => {
        btn.addEventListener('click', () => {
            if (pasoActual < pasos.length) {
                pasoActual++;
                mostrarPaso(pasoActual);
                window.scrollTo({
                    top: 0,
                    left: 0,
                    behavior: 'smooth'
                });
                document.documentElement.scrollTop = 0;
                document.body.scrollTop = 0;
            }
        });
    });

    botonesAnterior.forEach(btn => {
        btn.addEventListener('click', () => {
            if (pasoActual > 1) {
                pasoActual--;
                mostrarPaso(pasoActual);
                window.scrollTo({
                    top: 0,
                    left: 0,
                    behavior: 'smooth'
                });
                document.documentElement.scrollTop = 0;
                document.body.scrollTop = 0;
            }
        });
    });

    mostrarPaso(pasoActual);

    function inicializarMapa() {
        mapa = L.map('mapa').setView([-34.6037, -58.3816], 5);

        L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OSM</a> &copy; <a href="https://carto.com/">CARTO</a>',
            subdomains: 'abcd',
            maxZoom: 19
        }).addTo(mapa);

        const customIcon = L.icon({
            iconUrl: `data:image/svg+xml;utf8,${encodeURIComponent(`
                <svg xmlns="http://www.w3.org/2000/svg" width="36" height="44" viewBox="0 0 36 32">
                    <path d="M18 0 C28 0 36 14 18 32 C0 14 8 0 18 0 Z M18 6 A5 5 0 1 1 17.999 6 Z"
                          fill="#E88827" stroke="#E88827" stroke-width="3" fill-rule="evenodd"/>
                    <circle cx="18" cy="11" r="5" fill="none" stroke="#E88827" stroke-width="2"/>
                </svg>
            `)}`,
            iconSize: [36, 44],
            iconAnchor: [18, 42],
            popupAnchor: [0, -28],
            shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
            shadowSize: [36, 44]
        });

        mapa.on('click', async function (e) {
            const {lat, lng} = e.latlng;

            if (marcador) {
                marcador.setLatLng([lat, lng]);
            } else {
                marcador = L.marker([lat, lng], {icon: customIcon}).addTo(mapa);
            }

            const latInput = document.getElementById('latitud');
            const lngInput = document.getElementById('longitud');

            latInput.value = lat.toFixed(6);
            lngInput.value = lng.toFixed(6);

            latInput.dispatchEvent(new Event('input', {bubbles: true}));
            lngInput.dispatchEvent(new Event('input', {bubbles: true}));

            const paisInput = document.getElementById('pais');
            const provinciaInput = document.getElementById('provincia');
            const localidadInput = document.getElementById('localidad');

            paisInput.value = '';
            provinciaInput.value = '';
            localidadInput.value = '';

            paisInput.dispatchEvent(new Event('input', {bubbles: true}));
            provinciaInput.dispatchEvent(new Event('input', {bubbles: true}));
            localidadInput.dispatchEvent(new Event('input', {bubbles: true}));

            const address = await reverseGeocode(lat, lng);
            if (address) {
                paisInput.value = address.country || '';
                provinciaInput.value = address.state || address.province || '';
                localidadInput.value = address.city || address.town || address.village || address.municipality || '';

                paisInput.dispatchEvent(new Event('input', {bubbles: true}));
                provinciaInput.dispatchEvent(new Event('input', {bubbles: true}));
                localidadInput.dispatchEvent(new Event('input', {bubbles: true}));
            }

            actualizarBotonSiguiente();
        });
    }

    async function reverseGeocode(lat, lon) {
        try {
            const url = `https://nominatim.openstreetmap.org/reverse?format=jsonv2&lat=${lat}&lon=${lon}`;
            const resp = await fetch(url);
            if (!resp.ok) return null;
            const data = await resp.json();
            return data.address || null;
        } catch (err) {
            console.error('Error en reverse geocoding:', err);
            return null;
        }
    }
});
function mostrarToast(mensaje, tipo = 'success', duracion = 5000) {
    const contenedor = document.getElementById('toast-container');
    if (!contenedor) return;

    const toast = document.createElement('div');
    toast.className = `toast ${tipo}`;
    toast.innerHTML = `<span>${mensaje}</span><button>&times;</button>`;

    const btnCerrar = toast.querySelector('button');
    btnCerrar.addEventListener('click', () => {
        contenedor.removeChild(toast);
    });

    contenedor.appendChild(toast);

    // Animación de aparición
    setTimeout(() => toast.classList.add('show'), 100);

    // Desaparecer automáticamente
    setTimeout(() => {
        if (contenedor.contains(toast)) {
            contenedor.removeChild(toast);
        }
    }, duracion);
}

// Manejo del envío de formulario
// Manejo del envío de formulario
const formulario = document.getElementById('formulario-contribucion');
formulario.addEventListener('submit', async (e) => {
    e.preventDefault();

    const formData = new FormData(formulario);
    let exitoMostrado = false; // flag para evitar mostrar error si ya hubo éxito

    try {
        const respuesta = await fetch(formulario.action, {
            method: 'POST',
            body: formData
        });

        // Siempre intentamos leer JSON
        const data = await respuesta.json();

        if (respuesta.ok) {
            // Mostrar toast de éxito
            mostrarToast(data.mensaje || 'El hecho se envió correctamente', 'success');
            exitoMostrado = true;

            formulario.reset();
            pasoActual = 1;
            mostrarPaso(pasoActual);

            // Redirigir según el JSON del backend
            if (data.redirect) {
                setTimeout(() => {
                    window.location.href = data.redirect;
                }, 2000);
            }
        } else {
            // Mostrar toast de error según el JSON
            if (!exitoMostrado) {
                mostrarToast(data.error || data.mensaje || 'No se pudo reportar el hecho', 'error');
            }
        }

    } catch (err) {
        console.error('Error al enviar el hecho:', err);
        if (!exitoMostrado) {
            mostrarToast('Error al enviar el hecho', 'error');
        }
    }
});
