document.addEventListener("DOMContentLoaded", () => {
    const API_BASE = "http://localhost:8080";
    const tabla = document.getElementById("tablaColecciones");
    const modal = document.getElementById("modalColeccion");
    const form = document.getElementById("formColeccion");
    const btnNueva = document.getElementById("btnNuevaColeccion");
    const btnCancelar = document.getElementById("btnCancelar");

    // 🔹 Mostrar y ocultar modal
    const abrirModal = (editar = false, coleccion = {}) => {
        modal.classList.remove("hidden");
        document.getElementById("modalTitulo").textContent = editar ? "Editar Colección" : "Nueva Colección";
        form.reset();
        document.getElementById("coleccionHandle").value = coleccion.handle || "";
        document.getElementById("titulo").value = coleccion.titulo || "";
        document.getElementById("descripcion").value = coleccion.descripcion || "";
        document.getElementById("algoritmo").value = coleccion.algoritmo || "DEFAULT";
    };

    const cerrarModal = () => modal.classList.add("hidden");

    btnNueva.addEventListener("click", () => abrirModal());
    btnCancelar.addEventListener("click", cerrarModal);

    // 🔹 Cargar todas las colecciones
    async function cargarColecciones() {
        const resp = await fetch(`${API_BASE}/colecciones`);
        const data = await resp.json();
        tabla.innerHTML = "";
        data.forEach(c => {
            const fila = `
                <tr>
                    <td>${c.handle || "-"}</td>
                    <td>${c.titulo || "-"}</td>
                    <td>${c.descripcion || "-"}</td>
                    <td>${c.algoritmo || "-"}</td>
                    <td>
                        <button class="btn btn-secondary" onclick='editarColeccion(${JSON.stringify(c)})'>Editar</button>
                        <button class="btn btn-danger" onclick='eliminarColeccion("${c.handle}")'>Eliminar</button>
                        <button class="btn btn-info" onclick='verFuentes("${c.handle}")'>Fuentes</button>
                    </td>
                </tr>`;
            tabla.insertAdjacentHTML("beforeend", fila);
        });
    }

    // 🔹 Crear o editar colección
    form.addEventListener("submit", async (e) => {
        e.preventDefault();
        const handle = document.getElementById("coleccionHandle").value;
        const payload = {
            titulo: document.getElementById("titulo").value,
            descripcion: document.getElementById("descripcion").value,
            algoritmo: document.getElementById("algoritmo").value
        };
        const metodo = handle ? "PATCH" : "POST";
        const url = handle ? `${API_BASE}/colecciones/${handle}` : `${API_BASE}/colecciones`;
        await fetch(url, {
            method: metodo,
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(payload)
        });
        cerrarModal();
        await cargarColecciones();
    });

    // 🔹 Eliminar colección
    window.eliminarColeccion = async (handle) => {
        if (confirm("¿Eliminar esta colección?")) {
            await fetch(`${API_BASE}/colecciones/${handle}`, { method: "DELETE" });
            await cargarColecciones();
        }
    };

    // 🔹 Editar colección
    window.editarColeccion = (coleccion) => abrirModal(true, coleccion);

    // Cargar inicial
    cargarColecciones();

    window.verFuentes = async (handle) => {
        window.location.href = `/admin/colecciones/${handle}/fuentes`;
    };

});
