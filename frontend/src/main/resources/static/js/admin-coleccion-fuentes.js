document.addEventListener("DOMContentLoaded", () => {
    const API_BASE = "http://localhost:8080";
    const tabla = document.getElementById("tablaFuentes").querySelector("tbody");
    const modal = document.getElementById("modalFuente");
    const form = document.getElementById("formFuente");
    const btnNueva = document.getElementById("btnNuevaFuente");
    const btnCancelar = document.getElementById("btnCancelarFuente");
    const btnVolver = document.getElementById("btnVolver");

    // Obtener el handle de la colección desde la URL
    const pathParts = window.location.pathname.split("/");
    const handleColeccion = pathParts[pathParts.length - 2];

    // 🔹 Mostrar y ocultar modal
    const abrirModal = (editar = false, fuente = {}) => {
        modal.classList.remove("hidden");
        document.getElementById("modalTituloFuente").textContent = editar ? "Editar Fuente" : "Nueva Fuente";
        form.reset();
        document.getElementById("fuenteId").value = fuente.id || "";
        document.getElementById("tipo").value = fuente.tipo || "ESTATICA";
        document.getElementById("nombre").value = fuente.nombre || "";
        document.getElementById("url").value = fuente.url || "";
    };

    const cerrarModal = () => modal.classList.add("hidden");

    btnNueva.addEventListener("click", () => abrirModal());
    btnCancelar.addEventListener("click", cerrarModal);
    btnVolver.addEventListener("click", () => window.location.href = "/admin/colecciones");

    // 🔹 Cargar todas las fuentes de la colección
    async function cargarFuentes() {
        const resp = await fetch(`${API_BASE}/colecciones/${handleColeccion}/fuentes`);
        if (!resp.ok) {
            alert("Error al cargar las fuentes.");
            return;
        }
        const data = await resp.json();
        tabla.innerHTML = "";
        data.forEach(f => {
            const fila = `
                <tr>
                    <td>${f.tipo}</td>
                    <td>${f.nombre}</td>
                    <td>${f.url}</td>
                    <td>${f.ultimaActualizacion || "-"}</td>
                    <td>
                        <button class="btn btn-secondary" onclick='editarFuente(${JSON.stringify(f)})'>Editar</button>
                        <button class="btn btn-danger" onclick='eliminarFuente(${f.id})'>Eliminar</button>
                    </td>
                </tr>`;
            tabla.insertAdjacentHTML("beforeend", fila);
        });
    }

    // 🔹 Crear o editar fuente
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const id = document.getElementById("fuenteId").value;
        const payload = {
            tipo: document.getElementById("tipo").value,
            nombre: document.getElementById("nombre").value,
            url: document.getElementById("url").value
        };
        const metodo = id ? "PATCH" : "POST";
        const url = id
            ? `${API_BASE}/colecciones/${handleColeccion}/fuentes/${id}`
            : `${API_BASE}/colecciones/${handleColeccion}/fuentes`;
        await fetch(url, {
            method: metodo,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        cerrarModal();
        await cargarFuentes();
    });

    // 🔹 Eliminar fuente
    window.eliminarFuente = async (id) => {
        if (confirm("¿Eliminar esta fuente?")) {
            await fetch(`${API_BASE}/colecciones/${handleColeccion}/fuentes/${id}`, { method: "DELETE" });
            await cargarFuentes();
        }
    };

    // 🔹 Editar fuente
    window.editarFuente = (fuente) => abrirModal(true, fuente);

    // Cargar inicial
    cargarFuentes();
});
