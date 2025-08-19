<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # usage http:/localhost/adddeadline.php?nusnet=e0407306&id=16-09-2020test&deadlineName=test&module=CP2106&deadline=16-09-2020&description=asdjasodjasdas
    try {
        $message = new \stdClass();
        $message->success = false;
        if (isset($_GET['nusnet']) && isset($_GET['id'])) {
            $date = null;
            if (isset($_GET['deadline'])) {
                $date = $_GET['deadline'];
                $date = explode('-', $date);
                $date = $date[2] . "-" . $date[1] . "-" . $date[0];
            }
            $queryInsertTask = sprintf("INSERT INTO `deadline` (nusnet, id, deadlineName, module, deadline, description)"
                . " VALUES ('%s', '%s', '%s', '%s', '%s' ,'%s')", $_GET['nusnet'], $_GET['id'], $_GET['deadlineName'], $_GET['module'], $date,
                $_GET['description']);
            $rsInsertTask = mysqli_query($conn, $queryInsertTask);
            if ($rsInsertTask == 1) {
                $message->success = true;
            }
        } 
        echo json_encode($message);
    } catch(ErrorException $e) {
        echo $e->getMessage();
    }
?>
