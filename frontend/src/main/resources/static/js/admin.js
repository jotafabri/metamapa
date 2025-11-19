document.addEventListener("DOMContentLoaded", () => {
    const totalHechos = document.getElementById("totalHechos");
    const pendientesRevision = document.getElementById("pendientesRevision");
    const usuariosActivos = document.getElementById("usuariosActivos");
    const spamSpan = document.getElementById("solicitudesSpam");
    const btnActualizar = document.getElementById("btnActualizar");

    const API_BASE = "http://localhost:8080"; // <-- backend
    const coleccionHandle = "default"; // Cambialo por una colección real si existe

    async function cargarEstadisticas() {
        try {
            // 🔹 Categoría con más hechos (la usamos para "totalHechos")
            const categoria = await fetch(`${API_BASE}/estadisticas/categoria-mas-hechos`);
            if (categoria.redirected) {
                throw new Error("Redirigido al login. Verificá autenticación o CORS.");
            }
            const categoriaData = await categoria.json();
            totalHechos.textContent = categoriaData.categoria || "Sin datos";

            // 🔹 Provincia con más hechos (la usamos como “pendientes” simbólicos)
            const provincia = await fetch(`${API_BASE}/estadisticas/provincia-mas-hechos-coleccion?coleccionHandle=${coleccionHandle}`);
            const provinciaData = await provincia.json();
            pendientesRevision.textContent = provinciaData.provincia || "Sin datos";

            // 🔹 Usuarios activos (temporalmente mostramos el mismo valor)
            usuariosActivos.textContent = "N/D";

            // 🔹 Solicitudes spam
            const spam = await fetch(`${API_BASE}/estadisticas/solicitudes-spam`);
            const spamData = await spam.json();
            spamSpan.textContent = spamData.cantidad ?? "0";

        } catch (error) {
            console.error("Error cargando estadísticas:", error);
        }
    }

    btnActualizar.addEventListener("click", async () => {
        try {
            await fetch(`${API_BASE}/estadisticas/actualizar`);
            await cargarEstadisticas();
        } catch (err) {
            console.error("Error actualizando estadísticas:", err);
        }
    });

    cargarEstadisticas();
});
