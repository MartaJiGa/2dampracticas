<?php global $pdo;
include 'includes/db.php'; ?>
<?php include 'includes/header.php'; ?>

<h2 class="mb-4">Lista de Libros</h2>

<table class="table table-striped">
    <thead class="table-dark">
    <tr>
        <th>ID</th>
        <th>Título</th>
        <th>Género</th>
        <th>Fecha de Publicación</th>
        <th>Disponible</th>
        <th>Autor</th>
    </tr>
    </thead>
    <tbody>
    <?php
    $stmt = $pdo->query("SELECT books.*, authors.author_name FROM books 
                             JOIN authors ON books.id_author = authors.id");

    while ($libro = $stmt->fetch(PDO::FETCH_ASSOC)) {


        echo "<tr>
                <td>{$libro['id']}</td>
                <td>{$libro['title']}</td>
                <td>{$libro['genre']}</td>
                <td>{$libro['publication_date']}</td>
                <td>" . ($libro['available'] ? 'Sí' : 'No') . "</td>
                <td>{$libro['author_name']}</td>
            </tr>";
    }
    ?>
    </tbody>
</table>
<a href="index.php" class="btn btn-secondary">Volver</a>

<?php include 'includes/footer.php'; ?>
