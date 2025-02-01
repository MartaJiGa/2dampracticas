<?php
include 'includes/db.php';

if ($_SERVER["REQUEST_METHOD"] === "POST") {
    $id = $_POST['id'];

    $stmt = $pdo->prepare("DELETE FROM authors WHERE id = ?");
    $stmt->execute([$id]);

    header("Location: authors.php"); // Redirige a la lista de autores
    exit();
}
?>
