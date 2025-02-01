<?php
global $pdo;
include 'includes/db.php';
include 'includes/header.php';
?>

<h2 class="mb-4">Lista de Autores</h2>

<table class="table table-striped">
    <thead class="table-dark">
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Nacionalidad</th>
        <th>Fecha de Nacimiento</th>
        <th>Activo</th>
        <th>Acciones</th> <!-- Nueva columna -->
    </tr>
    </thead>
    <tbody>
    <?php
    $stmt = $pdo->query("SELECT * FROM authors");

    while ($autor = $stmt->fetch(PDO::FETCH_ASSOC)) {
        echo "<tr>
                <td>{$autor['id']}</td>
                <td>{$autor['author_name']}</td>
                <td>{$autor['nacionality']}</td>
                <td>{$autor['birth_date']}</td>
                <td>" . ($autor['active_author'] ? 'Sí' : 'No') . "</td>
                <td>
                    <form method='POST' action='delete_author.php' onsubmit='return confirm(\"¿Estás seguro de eliminar este autor?\");'>
                        <input type='hidden' name='id' value='{$autor['id']}'>
                        <button type='submit' class='btn btn-danger btn-sm'>Eliminar</button>
                    </form>
                </td>
            </tr>";
    }
    ?>
    </tbody>
</table>

<a href="index.php" class="btn btn-secondary">Volver</a>

<?php include 'includes/footer.php'; ?>
