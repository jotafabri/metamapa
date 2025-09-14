document.addEventListener('DOMContentLoaded', () => {
    const pasos = document.querySelectorAll('.seccion-paso');
    const botonesSiguiente = document.querySelectorAll('.siguiente-paso');
    const botonesAnterior = document.querySelectorAll('.paso-anterior');
    const indicador = document.querySelectorAll('.paso-progreso');

    let pasoActual = 1;
    let mapa;       // variable global para el mapa
    let marcador;   // variable global para el marcador

    function mostrarPaso(paso) {
        pasos.forEach(sec => sec.classList.toggle('activa', Number(sec.dataset.paso) === paso));
        indicador.forEach(ind => ind.classList.toggle('activo', Number(ind.dataset.paso) <= paso));

        const wrapper = document.querySelector('.contribucion-wrapper');
        wrapper.classList.toggle('paso2-activo', paso === 2);

        // Inicializar o recalcular mapa cuando se muestre el paso 2
        if (paso === 2) {
            if (!mapa) {
                inicializarMapa();
            } else {
                setTimeout(() => {
                    mapa.invalidateSize();
                }, 100);
            }
        }
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

    // Función para inicializar mapa
    function inicializarMapa() {
        mapa = L.map('mapa').setView([-34.6037, -58.3816], 5); // ejemplo: Argentina

        L.tileLayer('https://{s}.basemaps.cartocdn.com/dark_all/{z}/{x}/{y}{r}.png', {
            attribution: '&copy; <a href="https://www.openstreetmap.org/copyright">OSM</a> &copy; <a href="https://carto.com/">CARTO</a>',
            subdomains: 'abcd',
            maxZoom: 19
        }).addTo(mapa);

        // Evento click en mapa
        mapa.on('click', async function(e) {
            const { lat, lng } = e.latlng;

            // Colocar o mover marcador
            if (marcador) {
                marcador.setLatLng([lat, lng]);
            } else {
                marcador = L.marker([lat, lng]).addTo(mapa);
            }

            // Llenar campos latitud y longitud
            document.getElementById('latitud').value = lat.toFixed(6);
            document.getElementById('longitud').value = lng.toFixed(6);

            // Reverse geocoding para país y región
            const address = await reverseGeocode(lat, lng);
            if (address) {
                if (address.country_code) {
                    const paisSelect = document.getElementById('pais');
                    const code = address.country_code.toLowerCase();
                    if (Array.from(paisSelect.options).some(o => o.value === code)) {
                        paisSelect.value = code;
                    }
                }
                document.getElementById('region').value = address.state || address.region || '';
            }
        });
    }

    // Función reverse geocoding
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