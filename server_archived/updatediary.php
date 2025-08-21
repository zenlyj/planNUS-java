<?php
    require_once('Connections/connDB.php');
    header("Access-Control-Allow-Origin: *");
    header('Content-type:application/json;charset=utf-8');
    # params
    # usage http://localhost/updatediary.php?nusnet=e0407306&date=14-08-2020&note=HEEEEEEEEEEEEEEEEEHAHAHAHA
    try {
        $message = new \stdClass();
        $message->success = false;
        if (isset($_GET['nusnet']) && isset($_GET['date'])) {
            $queryUpdateTask = sprintf("UPDATE `diary` SET `note` = '%s'", isset($_GET['note']) ? $_GET['note'] : "");
            $date = $_GET['date'];
            $date = explode("-", $date);
            $date = $date[2] . '-' . $date[1] . '-' . $date[0];
            $querySearch = sprintf("SELECT * from `diary` WHERE `nusnet`='%s' and `date`='%s'", $_GET['nusnet'], $date);
            $result = $conn->query($querySearch);
            if ($result->num_rows > 0 ) {
                $queryUpdateTask = $queryUpdateTask . sprintf(" WHERE `nusnet`='%s' and `date`='%s'", $_GET['nusnet'], $date);
                $rsUpdateTask = mysqli_query($conn, $queryUpdateTask);
                if ($rsUpdateTask == 1) {
                    $message->success = true;
                } 
            } else {
                $queryInsertDiary = sprintf("INSERT INTO `diary` (`date`,`nusnet`,`note`) VALUES ('%s', '%s', '%s')", $date, $_GET['nusnet'], isset($_GET['note']) ? $_GET['note'] : "");
                echo $queryInsertDiary;
                $rsInsertTask = mysqli_query($conn, $queryInsertDiary);
                echo $queryInsertDiary;
                if ($rsInsertTask == 1) {
                    $message->success = true;
                }
            }
        } 
        echo json_encode($message);
    } catch(ErrorException $e) {
        echo $e->getMessage();
    }
?>