document.addEventListener('DOMContentLoaded', function() {
    console.log('DOM cargado, inicializando mapa...');

    const mapElement = document.getElementById('map');
    if (!mapElement) {
        console.error('Error: Elemento #map no encontrado');
        return;
    }
    
    try {

        let map = L.map('map').setView([-34.4221, -58.8468], 12);
        
        let osmLayer = L.tileLayer('https://{s}.tile.openstreetmap.org/{z}/{x}/{y}.png', {
            attribution: '© OpenStreetMap contributors',
            maxZoom: 19
        });
    
        let satelliteLayer = L.tileLayer('https://server.arcgisonline.com/ArcGIS/rest/services/World_Imagery/MapServer/tile/{z}/{y}/{x}', {
            attribution: '© Esri',
            maxZoom: 18
        });
        
        osmLayer.addTo(map);
        
        L.marker([-34.4221, -58.8468])
            .addTo(map)
            .bindPopup('¡Mapa funcionando!<br>Tigre, Buenos Aires')
            .openPopup();
        
        
        window.toggleSatellite = function() {
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

