document.addEventListener('DOMContentLoaded', function () {
    console.log('DOM cargado, inicializando mapa...');

    const mapElement = document.getElementById('map');
    if (!mapElement) {
        console.error('Error: Elemento #map no encontrado');
        return;
    }

    try {
        // Coordenadas por defecto (Buenos Aires)
        let defaultLat = -34.6037;
        let defaultLng = -58.3816;
        let defaultZoom = 12;

        // Si hay hechos, usar el primero como centro
        if (window.hechosData && window.hechosData.length > 0) {
            const primerHechoConUbicacion = window.hechosData.find(h => h.latitud && h.longitud);
            if (primerHechoConUbicacion) {
                defaultLat = primerHechoConUbicacion.latitud;
                defaultLng = primerHechoConUbicacion.longitud;
            }
        }

        let map = L.map('map').setView([defaultLat, defaultLng], defaultZoom);

        let osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 19
        });

        let satelliteLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
            attribution: '© Esri',
            maxZoom: 18
        });

        osmLayer.addTo(map);

        // Agregar marcadores de los hechos
        if (window.hechosData && window.hechosData.length > 0) {
            console.log(`Agregando ${window.hechosData.length} marcadores al mapa`);

            const markers = [];
            window.hechosData.forEach(hecho => {
                if (hecho.latitud && hecho.longitud) {
                    // Mapear categoría a tipo de marcador
                    let tipoMarcador = 'alert'; // default
                    if (hecho.categoria) {
                        const cat = hecho.categoria.toLowerCase();
                        if (cat.includes('incendio')) {
                            tipoMarcador = 'fire';
                        } else if (cat.includes('inundacion') || cat.includes('inundación')) {
                            tipoMarcador = 'water';
                        }
                    }

                    const marker = L.marker([hecho.latitud, hecho.longitud], {
                        icon: crerMarcadorSegundTipo(tipoMarcador)
                    }).addTo(map);

                    // Crear popup con información del hecho
                    const popupContent = `
                        <div class="marker-popup">
                            <h4>${hecho.titulo || 'Sin título'}</h4>
                            <p>${hecho.descripcion || 'Sin descripción'}</p>
                            ${hecho.categoria ? `<span class="popup-categoria">${hecho.categoria}</span>` : ''}
                        </div>
                    `;
                    marker.bindPopup(popupContent);
                    markers.push(marker);
                }
            });

            // Ajustar zoom para mostrar todos los marcadores
            if (markers.length > 1) {
                const group = new L.featureGroup(markers);
                map.fitBounds(group.getBounds().pad(0.1));
            }
        }

        window.toggleSatellite = function () {
            if (map.hasLayer(osmLayer)) {
                map.removeLayer(osmLayer);
                satelliteLayer.addTo(map);
                console.log('Cambiado a vista satelital');
            } else {
                map.removeLayer(satelliteLayer);
                osmLayer.addTo(map);
                console.log('Cambiado a vista de calles');
            }
        };
    } catch (error) {
        console.error('Error al inicializar el mapa:', error);
    }
});

const fireMarkerIcon = L.divIcon({
    html: `
        <div class="map-marker marker-fire">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M8.5 14.5A2.5 2.5 0 0 0 11 12c0-1.38-.5-2-1-3-1.072-2.143-.224-4.054 2-6 .5 2.5 2 4.9 4 6.5 2 1.6 3 3.5 3 5.5a7 7 0 1 1-14 0c0-1.153.433-2.294 1-3a2.5 2.5 0 0 0 2.5 2.5z"></path>
            </svg>
        </div>
    `,
    className: 'custom-div-icon',
    iconSize: [24, 24],
    iconAnchor: [12, 12]
});

const waterMarkerIcon = L.divIcon({
    html: `
        <div class="map-marker marker-water">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="M7 16.3c2.2 0 4-1.83 4-4.05 0-1.16-.57-2.26-1.71-3.19S7.29 6.75 7 5.3c-.29 1.45-1.14 2.84-2.29 3.76S3 11.1 3 12.25c0 2.22 1.8 4.05 4 4.05z"></path>
                <path d="M12.56 6.6A10.97 10.97 0 0 0 14 3.02c.5 2.5 2 4.9 4 6.5s3 3.5 3 5.5a6.98 6.98 0 0 1-11.91 4.97"></path>
            </svg>
        </div>
    `,
    className: 'custom-div-icon',
    iconSize: [24, 24],
    iconAnchor: [12, 12]
});

const alertMarkerIcon = L.divIcon({
    html: `
        <div class="map-marker marker-alert">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
                <path d="m21.73 18-8-14a2 2 0 0 0-3.48 0l-8 14A2 2 0 0 0 4 21h16a2 2 0 0 0 1.73-3"></path>
                <path d="M12 9v4"></path>
                <path d="M12 17h.01"></path>
            </svg>
        </div>
    `,
    className: 'custom-div-icon',
    iconSize: [24, 24],
    iconAnchor: [12, 12]
});

function crerMarcadorSegundTipo(type) {
    const markerTypes = {
        'fire': fireMarkerIcon,
        'water': waterMarkerIcon,
        'alert': alertMarkerIcon,
        'emergencias': fireMarkerIcon,
        'medioambiente': waterMarkerIcon,
        'derechoshumanos': alertMarkerIcon
    };

    return markerTypes[type] || alertMarkerIcon; // default
}

// Habilitar toggles
document.querySelectorAll('.toggle-switch').forEach(toggle => {
    toggle.addEventListener('click', () => {
        toggle.classList.toggle('active');
    });
});