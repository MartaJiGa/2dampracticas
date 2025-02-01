<?php
global $pdo;
include 'includes/db.php';
include 'includes/header.php';

if ($_SERVER['REQUEST_METHOD'] === 'POST') {
    $author_name = $_POST['author_name'];
    $nacionality = $_POST['nacionality'];
    $birth_date = $_POST['birth_date'];
    $active_author = isset($_POST['active_author']) ? 1 : 0;

    $stmt = $pdo->prepare("INSERT INTO authors (author_name, nacionality, birth_date, active_author) VALUES (?, ?, ?, ?)");
    $stmt->execute([$author_name, $nacionality, $birth_date, $active_author]);

    echo "<p class='alert alert-success'>Autor añadido correctamente.</p>";
}
?>

<h2>Agregar Autor</h2>
<form method="POST" action="add_author.php">
    <div class="row">
        <div class="col-md-6">
            <label>Nombre:</label>
            <input type="text" name="author_name" required class="form-control"><br>

            <label>Nacionalidad:</label>
            <input type="text" name="nacionality" required class="form-control"><br>

            <label>Fecha de Nacimiento:</label>
            <input type="date" name="birth_date" required class="form-control"><br>

            <label>Autor Activo:</label>
            <input type="checkbox" name="active_author"><br>

            <button type="submit" class="btn custom-btn btn-lg">Agregar Autor</button>
        </div>
    </div>
</form>
<div class="container">

</div>
<a href="index.php" class="btn btn-secondary mt-2">Volver</a>

<?php include 'includes/footer.php'; ?>
