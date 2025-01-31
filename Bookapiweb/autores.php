<?php global $pdo;
include 'includes/db.php'; ?>
<?php include 'includes/header.php'; ?>

<h2 class="mb-4">Lista de Autores</h2>

<table class="table table-striped">
    <thead class="table-dark">
    <tr>
        <th>ID</th>
        <th>Nombre</th>
        <th>Nacionalidad</th>
        <th>Fecha de Nacimiento</th>
        <th>Activo</th>
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
            </tr>";
    }
    ?>
    </tbody>
</table>
<a href="index.php" class="btn btn-secondary">Volver</a>

<?php include 'includes/footer.php'; ?>
