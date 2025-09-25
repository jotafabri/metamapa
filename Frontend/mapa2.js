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

const customIcon = L.icon({
    iconUrl: `data:image/svg+xml;utf8,${encodeURIComponent(`
        <svg xmlns="http://www.w3.org/2000/svg" width="36" height="44" viewBox="0 0 36 32">
            <!-- Pin relleno respetando contorno y dejando el círculo transparente -->
            <path d="M18 0 
                     C28 0 36 14 18 32 
                     C0 14 8 0 18 0 Z
                     M18 6
                     A5 5 0 1 1 17.999 6 Z"
                  fill="#E88827" stroke="#E88827" stroke-width="3" fill-rule="evenodd"/>
            <!-- Círculo interior transparente (agujero GPS) -->
            <circle cx="18" cy="11" r="5" fill="none" stroke="#E88827" stroke-width="2"/>
        </svg>
    `)}`,
    iconSize: [36, 44],
    iconAnchor: [18, 42],
    popupAnchor: [0, -28],
    shadowUrl: 'https://unpkg.com/leaflet@1.9.4/dist/images/marker-shadow.png',
    shadowSize: [36, 44]
});

        // Evento click en mapa
        mapa.on('click', async function(e) {
            const { lat, lng } = e.latlng;

            // Colocar o mover marcador
if (marcador) {
    marcador.setLatLng([lat, lng]);
} else {
    marcador = L.marker([lat, lng], { icon: customIcon }).addTo(mapa);
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