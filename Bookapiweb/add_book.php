<?php
include 'includes/db.php';
include 'includes/header.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $title = $_POST['title'];
    $genre = $_POST['genre'];
    $publication_date = $_POST['publication_date'];
    $available = isset($_POST['available']) ? 1 : 0;
    $id_author = $_POST['id_author'];

    $stmt = $pdo->prepare("INSERT INTO books (title, genre, publication_date, available, id_author) VALUES (?, ?, ?, ?, ?)");
    $stmt->execute([$title, $genre, $publication_date, $available, $id_author]);

    echo "<p class='alert alert-success'>Libro añadido correctamente.</p>";
}
?>

<h2>Agregar Libro</h2>
<form method="POST" action="add_book.php">
    <div class="row">
        <div class="col-md-6">
            <label>Título:</label>
            <input type="text" name="title" required class="form-control"><br>

            <label>Género:</label>
            <input type="text" name="genre" class="form-control"><br>

            <label>Fecha de Publicación:</label>
            <input type="date" name="publication_date" required class="form-control" lang="es"><br>

            <label>Disponible:</label>
            <input type="checkbox" name="available"><br>

            <label>Autor:</label>
            <select name="id_author" required class="form-control">
                <?php
                $stmt = $pdo->query("SELECT id, author_name FROM authors");
                while ($autor = $stmt->fetch(PDO::FETCH_ASSOC)) {
                    echo "<option value='{$autor['id']}'>{$autor['author_name']}</option>";
                }
                ?>
            </select><br>

            <button type="submit" class="btn custom-btn btn-lg">Agregar Libro</button>
        </div>
    </div>
</form>

<a href="index.php" class="btn btn-secondary mt-2">Volver</a>
<?php include 'includes/footer.php'; ?>

